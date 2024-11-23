package com.project.capstone.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.capstone.R
import com.project.capstone.ui.expenses.ExpensesActivity
import com.project.capstone.ui.home.HomePageActivity
import com.project.capstone.ui.photo.PhotoTrackActivity
import com.project.capstone.ui.profile.ProfileActivity

class FavoritesActivity : AppCompatActivity() {

    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Retrieve user_id from Intent
        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Bottom Navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.menu_favorites
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    navigateToActivity(HomePageActivity::class.java)
                    true
                }
                R.id.menu_expenses -> {
                    navigateToActivity(ExpensesActivity::class.java)
                    true
                }
                R.id.menu_photo_track -> {
                    navigateToActivity(PhotoTrackActivity::class.java)
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

    private fun <T> navigateToActivity(destination: Class<T>) {
        val intent = Intent(this, destination)
        intent.putExtra("user_id", userId)
        startActivity(intent)
        finish()
    }
}
