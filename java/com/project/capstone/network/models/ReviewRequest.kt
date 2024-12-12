package com.project.capstone.network.models

data class ReviewRequest(
    val user_id: String,
    val place_id: String,
    val place_type: String, // accommodations, tours, or culinaries
    val rating: Int,
    val reviews: String
)
