package com.project.capstone.network.models

import com.google.gson.annotations.SerializedName

data class AccommodationFilterRequest(
    @SerializedName("city") val city: String, // Nama kota yang dicari
    @SerializedName("min_rating") val minRating: Double? = null, // Rating minimal (opsional)
    @SerializedName("max_price") val maxPrice: Int? = null // Harga maksimal (opsional)
)
