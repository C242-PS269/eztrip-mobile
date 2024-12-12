package com.project.capstone.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.capstone.R
import com.project.capstone.database.AppDatabase
import com.project.capstone.database.profile.ProfileRepository
import com.project.capstone.ui.budget.SetBudgetActivity
import com.project.capstone.ui.expenses.ExpensesActivity
import com.project.capstone.ui.favorites.FavoritesActivity
import com.project.capstone.ui.home.HomePageActivity
import com.project.capstone.ui.settings.SettingsActivity
import com.project.capstone.MainActivity
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileRepository: ProfileRepository
    private var userId: Int = -1
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize SharedPreferences for session management
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)

        // Get the user ID from the intent
        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val db = AppDatabase.getDatabase(this)
        profileRepository = ProfileRepository(db.profileDao())

        // Setup navigation and actions
        setupBottomNavigation(userId)
        setupEditProfileNavigation()

        // Load profile data
        loadProfileData()

        // Setup logout functionality
        setupLogoutButton()

        // Setup direct settings navigation
        setupSettingsNavigation()
    }

    private fun loadProfileData() {
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvPhone = findViewById<TextView>(R.id.tvPhone)
        val ivProfilePicture = findViewById<ImageView>(R.id.ivProfilePicture)

        lifecycleScope.launch {
            val profile = profileRepository.getProfileByUserId(userId)
            if (profile != null) {
                runOnUiThread {
                    // Set user details to UI
                    tvName.text = profile.name.ifEmpty { "Nama belum diisi" }
                    tvEmail.text = profile.email.ifEmpty { "Email belum diisi" }
                    tvPhone.text = profile.phone.ifEmpty { "Telepon belum diisi" }

                    // Load the profile image using Glide
                    if (profile.imageUri.isNotEmpty()) {
                        Glide.with(this@ProfileActivity)
                            .load(Uri.parse(profile.imageUri))
                            .placeholder(R.drawable.ic_profile_placeholder)
                            .error(R.drawable.ic_profile_placeholder)
                            .into(ivProfilePicture)
                    } else {
                        ivProfilePicture.setImageResource(R.drawable.ic_profile_placeholder)
                    }
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@ProfileActivity, "Profil tidak ditemukan!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupEditProfileNavigation() {
        val ivEditProfile = findViewById<ImageView>(R.id.ivEditProfile)
        ivEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }
    }

    private fun setupBottomNavigation(userId: Int) {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.menu_profile
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    navigateToActivity(HomePageActivity::class.java, userId)
                    true
                }
                R.id.menu_expenses -> {
                    navigateToActivity(ExpensesActivity::class.java, userId)
                    true
                }
                R.id.menu_favorites -> {
                    navigateToActivity(FavoritesActivity::class.java, userId)
                    true
                }
                R.id.menu_set_budget -> {
                    navigateToActivity(SetBudgetActivity::class.java, userId)
                    true
                }
                R.id.menu_profile -> {
                    Toast.makeText(this, "Anda sudah di halaman profil!", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun <T> navigateToActivity(destination: Class<T>, userId: Int) {
        val intent = Intent(this, destination)
        intent.putExtra("user_id", userId)
        startActivity(intent)
        finish()
    }

    private fun setupSettingsNavigation() {
        val iconSettings = findViewById<ImageView>(R.id.icon_settings)
        iconSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("user_id", userId) // Pass the user ID to Settings
            startActivity(intent)
        }
    }

    private fun setupLogoutButton() {
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        with(sharedPreferences.edit()) {
            remove("user_id")
            remove("is_logged_in")
            apply()
        }

        Toast.makeText(this, "Anda telah logout!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
