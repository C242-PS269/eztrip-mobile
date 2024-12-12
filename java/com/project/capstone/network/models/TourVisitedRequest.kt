package com.project.capstone.network.models

data class TourVisitedRequest(
    val tour_name: String,
    val city_filter: String? = null,
    val max_price: Int? = null
)
