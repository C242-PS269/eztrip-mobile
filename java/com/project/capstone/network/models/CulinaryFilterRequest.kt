package com.project.capstone.network.models

import com.google.gson.annotations.SerializedName

data class CulinaryFilterRequest(
    @SerializedName("max_price") val maxPrice: Int? = null,
    @SerializedName("min_rating") val minRating: Double? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("city") val city: String? = null
)
