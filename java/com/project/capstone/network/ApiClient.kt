package com.project.capstone.network

import com.project.capstone.network.services.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://eztrip-gateway-server-851108637025.asia-southeast2.run.app/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // Timeout untuk koneksi
        .readTimeout(30, TimeUnit.SECONDS)     // Timeout untuk membaca respons
        .writeTimeout(30, TimeUnit.SECONDS)    // Timeout untuk menulis data
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Menambahkan OkHttpClient dengan timeout
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
