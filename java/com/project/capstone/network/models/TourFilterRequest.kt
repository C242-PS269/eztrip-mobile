package com.project.capstone.network.models

import com.google.gson.annotations.SerializedName

data class TourFilterRequest(
    @SerializedName("max_price") val maxPrice: Int,     // Ubah maxPrice menjadi max_price
    @SerializedName("min_rating") val minRating: Double, // Ubah minRating menjadi min_rating
    @SerializedName("category") val category: String,
    @SerializedName("city") val city: String
)
