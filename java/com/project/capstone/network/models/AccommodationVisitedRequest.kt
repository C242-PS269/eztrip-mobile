package com.project.capstone.network.models

data class AccommodationVisitedRequest(
    val accommodation_name: String,
    val city_filter: String? = null,
    val max_price: Int? = null
)
