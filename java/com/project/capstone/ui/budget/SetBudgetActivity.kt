package com.project.capstone.ui.budget

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.capstone.R
import com.project.capstone.databinding.ActivitySetBudgetBinding
import com.project.capstone.network.ApiClient
import com.project.capstone.network.models.ItineraryResponse
import com.project.capstone.ui.recommendation.Recommendation
import com.project.capstone.ui.recommendation.RecommendationAdapter
import com.project.capstone.ui.expenses.ExpensesActivity
import com.project.capstone.ui.favorites.FavoritesActivity
import com.project.capstone.ui.home.HomePageActivity
import com.project.capstone.ui.profile.ProfileActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetBudgetActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetBudgetBinding
    private lateinit var recommendationAdapter: RecommendationAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Int = -1

    private val defaultUserId = "3Z4QAPG6CBD7amLRN9D8KvHbmrfTpi2KKKqx"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE)
        // Retrieve userId from Intent
        userId = intent.getIntExtra("user_id", -1)

        // Setup RecyclerView
        setupRecyclerView()
        // Fetch and display itineraries from API
        fetchUserItineraries()
        // Setup Bottom Navigation
        setupBottomNavigation()

        // Handle manual budget button click
        binding.addManualLayout.setOnClickListener {
            navigateToInputBudgetActivity()
        }
    }

    private fun setupRecyclerView() {
        recommendationAdapter = RecommendationAdapter { recommendation ->
            saveToFavorites(recommendation)
        }
        binding.rvExpenses.apply {
            layoutManager = LinearLayoutManager(this@SetBudgetActivity)
            adapter = recommendationAdapter
        }
    }

    private fun fetchUserItineraries() {
        val apiUserId = if (userId == -1) defaultUserId else userId.toString()
        ApiClient.apiService.getUserItineraries(apiUserId).enqueue(object : Callback<ItineraryResponse> {
            override fun onResponse(call: Call<ItineraryResponse>, response: Response<ItineraryResponse>) {
                if (response.isSuccessful) {
                    val itineraryResponse = response.body()
                    if (itineraryResponse != null) {
                        displayItinerary(itineraryResponse)
                    }
                } else {
                    Toast.makeText(this@SetBudgetActivity, "Welcome to Iteneraries", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ItineraryResponse>, t: Throwable) {
                Toast.makeText(this@SetBudgetActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayItinerary(itineraryResponse: ItineraryResponse) {
        val itinerary = itineraryResponse.itinerary
        val recommendations = mutableListOf<Recommendation>()

        // Add places to recommendation list
        listOf(
            itinerary.accommodation,
            itinerary.culinary_1,
            itinerary.culinary_2,
            itinerary.culinary_3,
            itinerary.tour_1,
            itinerary.tour_2,
            itinerary.tour_3
        ).forEach { place ->
            recommendations.add(
                Recommendation(
                    title = place.name,
                    price = "Rp ${place.price}",
                    city = place.city
                )
            )
        }

        // Add remaining budget and total cost
        recommendations.add(
            Recommendation(
                title = "Remaining Budget",
                price = "Rp ${itinerary.remaining_budget}",
                city = ""
            )
        )
        recommendations.add(
            Recommendation(
                title = "Total Cost",
                price = "Rp ${itinerary.total_cost}",
                city = ""
            )
        )

        recommendationAdapter.submitList(recommendations)
    }

    private fun saveToFavorites(recommendation: Recommendation) {
        val editor = sharedPreferences.edit()
        val uniqueId = recommendation.title + System.currentTimeMillis()
        val value = "${recommendation.price};${recommendation.city}"
        editor.putString(uniqueId, value)
        editor.apply()

        Toast.makeText(this, "${recommendation.title} saved to favorites", Toast.LENGTH_SHORT).show()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> navigateToActivity(HomePageActivity::class.java)
                R.id.menu_expenses -> navigateToActivity(ExpensesActivity::class.java)
                R.id.menu_favorites -> navigateToActivity(FavoritesActivity::class.java)
                R.id.menu_profile -> navigateToActivity(ProfileActivity::class.java)
                R.id.menu_set_budget -> {} // Stay on the same page
            }
            true
        }
    }

    private fun <T> navigateToActivity(destination: Class<T>) {
        val intent = Intent(this, destination)
        intent.putExtra("user_id", userId)
        startActivity(intent)
        finish()
    }

    private fun navigateToInputBudgetActivity() {
        val intent = Intent(this, InputBudgetActivity::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
    }
}
