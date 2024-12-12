package com.project.capstone.ui.recommendation

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.capstone.databinding.ActivityRecommendationBinding
import com.project.capstone.network.ApiClient
import com.project.capstone.network.models.ItineraryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationBinding
    private lateinit var recommendationAdapter: RecommendationAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var userIdString: String = "" // Untuk user_id sebagai String
    private var userIdInt: Int = -1 // Untuk user_id sebagai Int
    private var currentRecommendation: Recommendation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve user_id from Intent (String dan Int)
        userIdString = intent.getStringExtra("user_id") ?: ""
        userIdInt = intent.getIntExtra("user_id_int", -1)

        if (userIdString.isEmpty() && userIdInt == -1) {
            Toast.makeText(this, "User ID tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE)

        // Setup RecyclerView
        recommendationAdapter = RecommendationAdapter { recommendation ->
            saveToFavorites(recommendation)
        }
        binding.recyclerViewRecommendations.apply {
            layoutManager = LinearLayoutManager(this@RecommendationActivity)
            adapter = recommendationAdapter
        }

        // Set Button Click Listener
        binding.btnSave.setOnClickListener {
            currentRecommendation?.let {
                saveToFavorites(it)
            } ?: run {
                Toast.makeText(this, "Pilih rekomendasi terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        // Display POST result if available, otherwise fetch GET data
        val itineraryResponse = intent.getSerializableExtra("itinerary_data") as? ItineraryResponse
        if (itineraryResponse != null) {
            displayItineraryFromPost(itineraryResponse)
        } else {
            fetchUserItineraries()
        }
    }

    private fun displayItineraryFromPost(itineraryResponse: ItineraryResponse) {
        val itinerary = itineraryResponse.itinerary
        val recommendations = listOf(
            Recommendation(itinerary.accommodation.name, "Rp ${itinerary.accommodation.price}", itinerary.accommodation.city),
            Recommendation(itinerary.culinary_1.name, "Rp ${itinerary.culinary_1.price}", itinerary.culinary_1.city),
            Recommendation(itinerary.tour_1.name, "Rp ${itinerary.tour_1.price}", itinerary.tour_1.city),
            Recommendation(itinerary.culinary_2.name, "Rp ${itinerary.culinary_2.price}", itinerary.culinary_2.city),
            Recommendation(itinerary.culinary_3.name, "Rp ${itinerary.culinary_3.price}", itinerary.culinary_3.city),
            Recommendation(itinerary.tour_2.name, "Rp ${itinerary.tour_2.price}", itinerary.tour_2.city),
            Recommendation(itinerary.tour_3.name, "Rp ${itinerary.tour_3.price}", itinerary.tour_3.city)
        )
        recommendationAdapter.submitList(recommendations)

        // Display budget information
        binding.tvPrice.text = "Total Biaya: Rp ${itinerary.total_cost}"
        binding.tvRemainingBudget.text = "Sisa Budget: Rp ${itinerary.remaining_budget}"
        binding.tvCity.text = itinerary.accommodation.city
    }

    private fun fetchUserItineraries() {
        val userIdForApi = if (userIdInt != -1) userIdInt.toString() else userIdString

        ApiClient.apiService.getUserItineraries(userIdForApi).enqueue(object : Callback<ItineraryResponse> {
            override fun onResponse(call: Call<ItineraryResponse>, response: Response<ItineraryResponse>) {
                if (response.isSuccessful) {
                    val itineraryResponse = response.body()
                    if (itineraryResponse != null) {
                        displayItineraryFromPost(itineraryResponse)
                    }
                } else {
                    Toast.makeText(this@RecommendationActivity, "Gagal memuat data itinerary", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ItineraryResponse>, t: Throwable) {
                Toast.makeText(this@RecommendationActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveToFavorites(recommendation: Recommendation) {
        val editor = sharedPreferences.edit()
        val uniqueId = recommendation.title + System.currentTimeMillis()
        val value = "${recommendation.price};${recommendation.city}"
        editor.putString(uniqueId, value)
        editor.apply()

        Toast.makeText(this, "${recommendation.title} disimpan ke favorit", Toast.LENGTH_SHORT).show()

        // Navigate to FavoritesActivity
        val intent = Intent(this, com.project.capstone.ui.favorites.FavoritesActivity::class.java)
        intent.putExtra("user_id", userIdString) // Mengirim user_id sebagai String
        intent.putExtra("user_id_int", userIdInt) // Mengirim user_id sebagai Int
        startActivity(intent)
    }
}
