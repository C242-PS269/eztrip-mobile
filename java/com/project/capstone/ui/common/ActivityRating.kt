package com.project.capstone.ui.common

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.capstone.R
import com.project.capstone.network.ApiClient
import com.project.capstone.network.models.ReviewRequest
import com.project.capstone.network.models.ReviewResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.ArrayAdapter

class ActivityRating : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var reviewDescription: EditText
    private lateinit var submitButton: Button
    private lateinit var placeTypeSpinner: Spinner  // Declare Spinner for place_type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        // Initialize UI elements
        ratingBar = findViewById(R.id.ratingBar)
        reviewDescription = findViewById(R.id.reviewDescription)
        submitButton = findViewById(R.id.submitButton)
        placeTypeSpinner = findViewById(R.id.placeTypeSpinner) // Initialize Spinner

        // Populate Spinner with place types (Accommodations, Tours, Culinaries)
        val placeTypes = arrayOf("Accommodations", "Tours", "Culinaries")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, placeTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        placeTypeSpinner.adapter = adapter

        // Get place_id and user_id from intent (passed from DetailActivity)
        val placeId = intent.getStringExtra("place_id") ?: ""
        val userId = "3Z4QAPG6CBD7amLRN9D8KvHbmrfTpi2KKKqx"  // Example user ID, replace it with the actual ID

        // Handle submit button click
        submitButton.setOnClickListener {
            val rating = ratingBar.rating
            val description = reviewDescription.text.toString()

            if (rating == 0f || description.isEmpty()) {
                Toast.makeText(this, "Please provide a rating and review.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Get the selected place type from the Spinner
            val placeType = placeTypeSpinner.selectedItem.toString().toLowerCase()

            // Create the request object to send to API
            val reviewRequest = ReviewRequest(
                user_id = userId,
                place_id = placeId,
                place_type = placeType,  // Use the selected place type
                rating = rating.toInt(),
                reviews = description
            )

            // Send the review to the API
            submitReview(reviewRequest)
        }
    }

    private fun submitReview(reviewRequest: ReviewRequest) {
        val call = ApiClient.apiService.submitReview(reviewRequest)
        call.enqueue(object : Callback<ReviewResponse> {
            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ActivityRating, "Review submitted successfully!", Toast.LENGTH_SHORT).show()
                    finish()  // Optionally navigate back to the previous activity or home page
                } else {
                    Toast.makeText(this@ActivityRating, "Failed to submit review", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                Toast.makeText(this@ActivityRating, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

