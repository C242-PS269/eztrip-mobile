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
    @POST("api/data/features/itineraries")
    fun postItinerary(@Body request: ItineraryRequest): Call<ItineraryResponse>

    // Endpoint to get itineraries based on user_id
    @GET("api/data/features/itineraries/user/{user_id}")
    fun getUserItineraries(@Path("user_id") userId: String): Call<ItineraryResponse>

    @POST("api/data/reviews/submit")
    fun submitReview(@Body reviewRequest: ReviewRequest): Call<ReviewResponse>

    @GET("api/data/places/tours/all")
    fun getRandomTours(): Call<PlaceResponse>

    @GET("api/data/places/accommodations/all")
    fun getRandomAccommodations(): Call<PlaceResponse>

    @GET("api/data/places/culinaries/all")
    fun getRandomCulinaries(): Call<PlaceResponse>

    @GET("api/data/reviews/{place_id}")
    fun getReviewsByPlaceId(@Path("place_id") placeId: String): Call<List<Review>>

    // Endpoint untuk mendapatkan rekomendasi wisata berdasarkan filter
    @POST("api/model/recommendations/tours")
    fun getFilteredTours(@Body request: TourFilterRequest): Call<PlaceResponse>

    @POST("api/model/recommendations/accommodations")
    fun getFilteredAccommodations(@Body filter: AccommodationFilterRequest): Call<PlaceResponse>

    @POST("api/model/recommendations/culinaries")
    fun getFilteredCulinaries(@Body request: CulinaryFilterRequest): Call<PlaceResponse>

    @POST("api/model/recommendations/tours/visited")
    fun getVisitedTours(@Body request: TourVisitedRequest): Call<PlaceResponse>

    @POST("api/model/recommendations/accommodations/visited")
    fun getVisitedAccommodations(@Body request: AccommodationVisitedRequest): Call<AccommodationPlaceResponse>

    @POST("api/model/recommendations/culinaries/visited")
    fun getVisitedCulinaries(@Body request: CulinaryVisitedRequest): Call<PlaceResponse>

}
