package com.project.capstone.network.models

data class CulinaryVisitedRequest(
    val culinary_name: String,
    val city_filter: String? = null,
    val max_price: Int? = null
)
