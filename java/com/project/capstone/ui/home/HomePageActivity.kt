package com.project.capstone.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.project.capstone.R
import com.project.capstone.ui.profile.ProfileActivity
import com.project.capstone.ui.expenses.ExpensesActivity
import com.project.capstone.ui.favorites.FavoritesActivity
import com.project.capstone.ui.wisata.WisataActivity
import com.project.capstone.ui.akomodasi.AkomodasiActivity
import com.project.capstone.ui.budget.SetBudgetActivity
import com.project.capstone.ui.restoran.RestoranActivity

class HomePageActivity : AppCompatActivity() {

    // UI Components
    private lateinit var tvGreeting: TextView
    private lateinit var ivNotification: ImageView
    private lateinit var etSearch: TextInputEditText
    private lateinit var rvRecommendations: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView

    // User ID
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Retrieve user ID from Intent
        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize UI components
        tvGreeting = findViewById(R.id.tvGreeting)
        ivNotification = findViewById(R.id.ivNotification)
        etSearch = findViewById(R.id.etSearch)
        rvRecommendations = findViewById(R.id.rvRecommendations)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Setup greeting
        val userName = "User" // Replace with actual username if available
        tvGreeting.text = getString(R.string.greeting_text, userName)

        // Notification click listener
        ivNotification.setOnClickListener {
            onNotificationClicked()
        }

        // Search bar functionality
        etSearch.setOnEditorActionListener { _, _, _ ->
            val query = etSearch.text.toString()
            if (query.isNotEmpty()) {
                performSearch(query)
            }
            true
        }

        // Setup RecyclerView for recommendations
        setupRecommendationsRecyclerView()

        // Setup bottom navigation
        setupBottomNavigation()

        // Setup category navigation for Wisata, Akomodasi, and Restoran
        setupCategoryNavigation()
    }

    private fun setupCategoryNavigation() {
        val wisataIcon = findViewById<ImageView>(R.id.ivWisata)
        val akomodasiIcon = findViewById<ImageView>(R.id.ivAkomodasi)
        val restoranIcon = findViewById<ImageView>(R.id.ivRestoran) // Add Restoran Icon

        wisataIcon.setOnClickListener {
            navigateToActivity(WisataActivity::class.java)
        }

        akomodasiIcon.setOnClickListener {
            navigateToActivity(AkomodasiActivity::class.java)
        }

        restoranIcon.setOnClickListener { // Add click listener for Restoran
            navigateToActivity(RestoranActivity::class.java)
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_expenses -> {
                    navigateToActivity(ExpensesActivity::class.java)
                    true
                }
                R.id.menu_favorites -> {
                    navigateToActivity(FavoritesActivity::class.java)
                    true
                }
                R.id.menu_set_budget -> {
                    navigateToActivity(SetBudgetActivity::class.java)
                    true
                }
                R.id.menu_profile -> {
                    navigateToActivity(ProfileActivity::class.java)
                    true
                }
                else -> false
            }
        }
    }

    private fun onNotificationClicked() {
        // Show a toast or navigate to the notification page
        Toast.makeText(this, "Notification clicked!", Toast.LENGTH_SHORT).show()
    }

    private fun performSearch(query: String) {
        // Implement search logic
        Toast.makeText(this, "Searching for: $query", Toast.LENGTH_SHORT).show()
    }

    private fun setupRecommendationsRecyclerView() {
        rvRecommendations.layoutManager = LinearLayoutManager(this)
        rvRecommendations.adapter = RecommendationsAdapter(getSampleRecommendations())
    }

    private fun getSampleRecommendations(): List<String> {
        return listOf("Recommendation 1", "Recommendation 2", "Recommendation 3")
    }

    private fun <T> navigateToActivity(destination: Class<T>) {
        val intent = Intent(this, destination)
        intent.putExtra("user_id", userId) // Pass user_id to the target activity
        startActivity(intent)
        finish()
    }
}
