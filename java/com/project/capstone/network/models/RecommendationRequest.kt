package com.project.capstone.network.models

data class RecommendationRequest(
    val max_price: Int,
    val min_rating: Int,
    val category: String,
    val city: String
)
