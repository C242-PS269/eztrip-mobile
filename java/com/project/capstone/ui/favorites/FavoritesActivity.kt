package com.project.capstone.ui.favorites

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.capstone.R
import com.project.capstone.databinding.ActivityFavoritesBinding
import com.project.capstone.model.FavoriteItem
import com.project.capstone.ui.budget.SetBudgetActivity
import com.project.capstone.ui.expenses.ExpensesActivity
import com.project.capstone.ui.home.HomePageActivity
import com.project.capstone.ui.profile.ProfileActivity

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var userId: Int = -1 // Default user_id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve user_id from Intent
        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "Menggunakan User ID default (-1)", Toast.LENGTH_SHORT).show()
            Log.d("FavoritesActivity", "User ID: $userId")
        }

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE)

        // Setup RecyclerView
        favoriteAdapter = FavoriteAdapter(mutableListOf(), sharedPreferences)
        binding.recyclerViewFavorites.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = favoriteAdapter
        }

        // Setup Bottom Navigation
        setupBottomNavigation()

        // Load favorites from SharedPreferences
        loadFavorites()
    }

    private fun loadFavorites() {
        val favorites = mutableListOf<FavoriteItem>()

        try {
            val allFavorites = sharedPreferences.all
            for ((key, value) in allFavorites) {
                val favoriteData = value as? String ?: continue
                val parts = favoriteData.split(";")
                if (parts.size == 3) { // Ensure we have title, price, and city
                    val favoriteItem = FavoriteItem(key, parts[0], parts[1], parts[2])
                    favorites.add(favoriteItem)
                }
            }

            // Update RecyclerView
            favoriteAdapter.updateFavorites(favorites)

            if (favorites.isEmpty()) {
                Toast.makeText(this, "Tidak ada item favorit yang tersimpan", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("FavoritesActivity", "Error loading favorites: ${e.message}", e)
            Toast.makeText(this, "Gagal memuat item favorit", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.menu_favorites

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> navigateToActivity(HomePageActivity::class.java)
                R.id.menu_expenses -> navigateToActivity(ExpensesActivity::class.java)
                R.id.menu_set_budget -> navigateToActivity(SetBudgetActivity::class.java)
                R.id.menu_profile -> navigateToActivity(ProfileActivity::class.java)
                else -> false
            }
            true
        }
    }

    private fun <T> navigateToActivity(destination: Class<T>) {
        val intent = Intent(this, destination).apply {
            putExtra("user_id", userId) // Kirim user_id dengan nama konsisten
        }
        startActivity(intent)
        finish()
    }
}
