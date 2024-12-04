package com.project.capstone.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileRepository: ProfileRepository
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val db = AppDatabase.getDatabase(this)
        profileRepository = ProfileRepository(db.profileDao())

        // Setup navigation
        setupBottomNavigation(userId)
        setupEditProfileNavigation()

        // Load profile data
        loadProfileData()
    }

    override fun onResume() {
        super.onResume()
        loadProfileData() // Reload profile data when returning from EditProfileActivity
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
                    tvName.text = profile.name.ifEmpty { "Nama belum diisi" }
                    tvEmail.text = profile.email.ifEmpty { "Email belum diisi" }
                    tvPhone.text = profile.phone.ifEmpty { "Telepon belum diisi" }

                    // Use Glide to load the image
                    if (profile.imageUri.isNotEmpty()) {
                        Glide.with(this@ProfileActivity)
                            .load(Uri.parse(profile.imageUri))
                            .placeholder(R.drawable.ic_profile_placeholder)
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
                    navigateToActivity(ExpensesActivity::class.java, userId) // Launch ExpensesActivity
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
}
