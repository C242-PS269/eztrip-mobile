package com.project.capstone.network.services

import com.project.capstone.network.models.ItineraryRequest
import com.project.capstone.network.models.ItineraryResponse
import com.project.capstone.network.models.PlaceResponse
import com.project.capstone.network.models.ReviewRequest
import com.project.capstone.network.models.ReviewResponse
import com.project.capstone.network.models.Review
import com.project.capstone.network.models.TourFilterRequest
import com.project.capstone.network.models.TourVisitedRequest
import com.project.capstone.network.models.AccommodationFilterRequest
import com.project.capstone.network.models.AccommodationVisitedRequest
import com.project.capstone.network.models.CulinaryFilterRequest
import com.project.capstone.network.models.CulinaryVisitedRequest
import com.project.capstone.network.models.AccommodationPlaceResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // Endpoint to create an itinerary based on user ID, budget, and optional city
    @POST("")
    fun postItinerary(@Body request: ItineraryRequest): Call<ItineraryResponse>

    // Endpoint to get itineraries based on user_id
    @GET("")
    fun getUserItineraries(@Path("user_id") userId: String): Call<ItineraryResponse>

    @POST("")
    fun submitReview(@Body reviewRequest: ReviewRequest): Call<ReviewResponse>

    @GET("")
    fun getRandomTours(): Call<PlaceResponse>

    @GET("")
    fun getRandomAccommodations(): Call<PlaceResponse>

    @GET("")
    fun getRandomCulinaries(): Call<PlaceResponse>

    @GET("")
    fun getReviewsByPlaceId(@Path("place_id") placeId: String): Call<List<Review>>

    // Endpoint untuk mendapatkan rekomendasi wisata berdasarkan filter
    @POST("")
    fun getFilteredTours(@Body request: TourFilterRequest): Call<PlaceResponse>

    @POST("")
    fun getFilteredAccommodations(@Body filter: AccommodationFilterRequest): Call<PlaceResponse>

    @POST("")
    fun getFilteredCulinaries(@Body request: CulinaryFilterRequest): Call<PlaceResponse>

    @POST("")
    fun getVisitedTours(@Body request: TourVisitedRequest): Call<PlaceResponse>

    @POST("")
    fun getVisitedAccommodations(@Body request: AccommodationVisitedRequest): Call<AccommodationPlaceResponse>

    @POST("api/model/recommendations/culinaries/visited")
    fun getVisitedCulinaries(@Body request: CulinaryVisitedRequest): Call<PlaceResponse>

}
