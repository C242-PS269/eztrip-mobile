package com.project.capstone.network.models

import com.google.gson.annotations.SerializedName

data class AccommodationPlaceResponse(
    @SerializedName("accomodations") val accommodations: List<AccommodationPlace>? = null
)

// Define AccommodationPlace as a standalone data class
data class AccommodationPlace(
    val name: String,
    val city: String,
    val price_wna: Double,
    val rating: Double,
    val category: String // Tambahkan jika diperlukan
)
