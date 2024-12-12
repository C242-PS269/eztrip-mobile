package com.project.capstone.ui.wisata

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.capstone.R
import com.project.capstone.network.ApiClient
import com.project.capstone.network.models.PlaceResponse
import com.project.capstone.network.models.TourFilterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilterWisataActivity : AppCompatActivity() {

    private lateinit var etMaxPrice: EditText
    private lateinit var etMinRating: EditText
    private lateinit var etCategory: EditText
    private lateinit var etCity: EditText
    private lateinit var btnApplyFilter: Button
    private lateinit var progressBar: ProgressBar // Tambahkan ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_wisata)

        // Inisialisasi komponen UI
        etMaxPrice = findViewById(R.id.etMaxPrice)
        etMinRating = findViewById(R.id.etMinRating)
        etCategory = findViewById(R.id.etCategory)
        etCity = findViewById(R.id.etCity)
        btnApplyFilter = findViewById(R.id.btnApplyFilter)
        progressBar = findViewById(R.id.progressBar) // ProgressBar

        // Set listener untuk tombol "Apply Filter"
        btnApplyFilter.setOnClickListener {
            val maxPrice = etMaxPrice.text.toString().toIntOrNull() ?: 0
            val minRating = etMinRating.text.toString().toDoubleOrNull() ?: 0.0
            val category = etCategory.text.toString().trim()
            val city = etCity.text.toString().trim()

            Log.d("FilterWisataActivity", "Filter Values -> maxPrice: $maxPrice, minRating: $minRating, category: $category, city: $city")

            val filter = TourFilterRequest(maxPrice, minRating, category, city)

            // Tampilkan ProgressBar sebelum memulai panggilan API
            progressBar.visibility = View.VISIBLE

            // Panggil API untuk filter
            val call = ApiClient.apiService.getFilteredTours(filter)
            call.enqueue(object : Callback<PlaceResponse> {
                override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                    progressBar.visibility = View.GONE // Sembunyikan ProgressBar setelah mendapat respons

                    if (response.isSuccessful) {
                        val places = response.body()?.getPlaces() ?: emptyList()
                        Log.d("FilterWisataActivity", "API Response: ${places.size} items received.")

                        if (places.isEmpty()) {
                            Toast.makeText(this@FilterWisataActivity, "Tidak ada data sesuai filter", Toast.LENGTH_SHORT).show()
                        } else {
                            // Navigasi ke WisataActivity dengan data yang difilter
                            val intent = Intent(this@FilterWisataActivity, WisataActivity::class.java).apply {
                                putParcelableArrayListExtra("filtered_places", ArrayList(places))
                                putExtra("user_id", intent.getIntExtra("user_id", -1))
                            }
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("FilterWisataActivity", "API Error: ${response.code()} - $errorBody")
                        Toast.makeText(this@FilterWisataActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE // Sembunyikan ProgressBar jika gagal
                    Log.e("FilterWisataActivity", "API Failure: ${t.message}", t)
                    Toast.makeText(this@FilterWisataActivity, "Gagal menghubungi server: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
