package com.project.capstone.ui.common

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.capstone.R
import com.project.capstone.network.ApiClient
import com.project.capstone.network.models.Place
import com.project.capstone.network.models.Review
import com.project.capstone.ui.common.ReviewAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvCity: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvGoogleMaps: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvPlaceId: TextView
    private lateinit var btnRate: Button
    private lateinit var reviewsRecyclerView: RecyclerView

    private lateinit var placeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Initialize TextViews
        tvName = findViewById(R.id.tvName)
        tvCategory = findViewById(R.id.tvCategory)
        tvCity = findViewById(R.id.tvCity)
        tvPrice = findViewById(R.id.tvPrice)
        tvGoogleMaps = findViewById(R.id.tvGoogleMaps)
        tvAddress = findViewById(R.id.tvAddress)
        tvRating = findViewById(R.id.tvRating)
        tvPlaceId = findViewById(R.id.tvPlaceId)
        btnRate = findViewById(R.id.btnRate)
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView)

        // Get data from Intent
        val place = intent.getParcelableExtra<Place>("place")
        placeId = place?.id ?: ""

        // Set data to TextViews
        tvName.text = place?.name
        tvCategory.text = "Category: ${place?.category}"
        tvCity.text = "City: ${place?.city}"
        tvPrice.text = "Price: Rp ${place?.price_wna}"
        tvGoogleMaps.text = "Google Maps: ${place?.google_maps}"
        tvAddress.text = "Address: ${place?.address}"
        tvRating.text = "Rating: ${place?.rating}" // Display rating
        tvPlaceId.text = "Place ID: $placeId" // Display place_id

        // Set up button click listener to navigate to ActivityRating
        btnRate.setOnClickListener {
            val intent = Intent(this, ActivityRating::class.java)
            intent.putExtra("place_id", place?.id)
            startActivity(intent)
        }

        // Fetch reviews from API
        fetchReviews()
    }

    private fun fetchReviews() {
        // API call to get reviews based on placeId
        val call = ApiClient.apiService.getReviewsByPlaceId(placeId)
        call.enqueue(object : Callback<List<Review>> {
            override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                if (response.isSuccessful) {
                    val reviews = response.body() ?: emptyList()
                    val reviewAdapter = ReviewAdapter(reviews)
                    reviewsRecyclerView.layoutManager = LinearLayoutManager(this@DetailActivity)
                    reviewsRecyclerView.adapter = reviewAdapter
                } else {
                    Toast.makeText(this@DetailActivity, "Failed to load reviews", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
