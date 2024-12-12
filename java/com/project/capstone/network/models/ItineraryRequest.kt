package com.project.capstone.network.models

data class ItineraryRequest(
    val user_id: String,
    val budget: Int,
    val city: String? = null
)
