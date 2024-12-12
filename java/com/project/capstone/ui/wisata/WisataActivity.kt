package com.project.capstone.ui.wisata

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.capstone.R
import com.project.capstone.network.ApiClient
import com.project.capstone.network.models.Place
import com.project.capstone.network.models.PlaceResponse
import com.project.capstone.ui.budget.SetBudgetActivity
import com.project.capstone.ui.common.CategoryAdapter
import com.project.capstone.ui.expenses.ExpensesActivity
import com.project.capstone.ui.favorites.FavoritesActivity
import com.project.capstone.ui.home.HomePageActivity
import com.project.capstone.ui.profile.ProfileActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WisataActivity : AppCompatActivity() {

    private var userId: Int = -1
    private lateinit var rvCategories: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: CategoryAdapter
    private var placeList: List<Place> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wisata)

        // Ambil userId dari intent
        userId = intent.getIntExtra("user_id", -1)
        Log.d("WisataActivity", "User ID: $userId")

        if (userId == -1) {
            Toast.makeText(this, "Error: User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize UI components
        rvCategories = findViewById(R.id.rvCategories)
        rvCategories.layoutManager = LinearLayoutManager(this)
        searchBar = findViewById(R.id.etSearch)
        progressBar = findViewById(R.id.progressBar)

        // Cek apakah ada data filtered_places yang dikirim
        val filteredPlaces: List<Place>? = intent.getParcelableArrayListExtra("filtered_places")
        if (filteredPlaces != null && filteredPlaces.isNotEmpty()) {
            Log.d("WisataActivity", "Filtered places received: ${filteredPlaces.size} items.")
            placeList = filteredPlaces
            setupRecyclerView(placeList)
        } else {
            Log.d("WisataActivity", "No filtered places received, fetching random tours.")
            fetchTourData()
        }

        // Setup bottom navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        setupBottomNavigation(bottomNavigation)

        // Tombol filter untuk membuka FilterWisataActivity
        val filterIcon = findViewById<ImageView>(R.id.ivFilter)
        filterIcon.setOnClickListener {
            val intent = Intent(this, FilterWisataActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }
    }

    // Fungsi untuk mengambil data tur acak
    private fun fetchTourData() {
        Log.d("WisataActivity", "Fetching random tours data...")
        progressBar.visibility = View.VISIBLE
        val call = ApiClient.apiService.getRandomTours()
        call.enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    placeList = response.body()?.items ?: emptyList()
                    Log.d("WisataActivity", "Random tours fetched: ${placeList.size} items.")
                    setupRecyclerView(placeList)
                } else {
                    Log.e("WisataActivity", "Failed to fetch data: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@WisataActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("WisataActivity", "Error fetching tours: ${t.message}", t)
                Toast.makeText(this@WisataActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fungsi untuk mengatur RecyclerView dengan data yang diterima
    private fun setupRecyclerView(data: List<Place>) {
        Log.d("WisataActivity", "Setting up RecyclerView with ${data.size} items.")
        adapter = CategoryAdapter(data, this)
        rvCategories.adapter = adapter
    }

    // Fungsi untuk mengatur bottom navigation
    private fun setupBottomNavigation(bottomNavigation: BottomNavigationView) {
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

    // Fungsi umum untuk navigasi antar aktivitas dengan user_id
    private fun <T> navigateToActivity(destination: Class<T>) {
        val intent = Intent(this, destination)
        intent.putExtra("user_id", userId)
        startActivity(intent)
        finish()
    }

    // Fungsi untuk menangani aksi back pressed
    override fun onBackPressed() {
        val intent = Intent(this, HomePageActivity::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
        finish()
    }
}
