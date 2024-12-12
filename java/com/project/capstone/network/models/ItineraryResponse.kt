package com.project.capstone.network.models

import java.io.Serializable

data class ItineraryResponse(
    val message: String,
    val remaining_budget: Double,
    val total_cost: Double,
    val itinerary: Itinerary
) : Serializable {

    data class Itinerary(
        val accommodation: Place,
        val culinary_1: Place,
        val culinary_2: Place,
        val culinary_3: Place,
        val tour_1: Place,
        val tour_2: Place,
        val tour_3: Place,
        val tour_free_36: Place,
        val tour_free_39: Place,
        val tour_free_9: Place,
        val remaining_budget: Double,
        val total_cost: Double
    ) : Serializable

    data class Place(
        val city: String,
        val name: String,
        val price: Double
    ) : Serializable
}
