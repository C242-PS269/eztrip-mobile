package com.project.capstone.ui.restoran

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

class RestoranActivity : AppCompatActivity() {

    private var userId: Int = -1
    private lateinit var rvCategories: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: CategoryAdapter
    private var originalData = mutableListOf<Place>() // List untuk menyimpan data asli dari API
    private lateinit var filterIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restoran)

        // Retrieve user ID from Intent
        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize UI Components
        rvCategories = findViewById(R.id.rvCategories)
        searchBar = findViewById(R.id.etSearch)
        filterIcon = findViewById(R.id.ivFilter)
        progressBar = findViewById(R.id.progressBar)

        // Setup RecyclerView and Search Bar
        setupRecyclerView()
        setupSearchBar()

        // Handle filtered data if exists
        val filteredPlaces = intent.getParcelableArrayListExtra<Place>("filtered_places")
        if (filteredPlaces != null) {
            // Display filtered data
            originalData.clear()
            originalData.addAll(filteredPlaces)
            adapter.updateData(originalData)
            Toast.makeText(this, "Menampilkan hasil filter", Toast.LENGTH_SHORT).show()
        } else {
            // Load data from API if no filter is applied
            loadCulinaryData()
        }

        // Set up filter icon click listener
        filterIcon.setOnClickListener {
            navigateToFilterActivity()
        }

        // Initialize Bottom Navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        setupBottomNavigation(bottomNavigation)
    }

    // Setup RecyclerView
    private fun setupRecyclerView() {
        rvCategories.layoutManager = LinearLayoutManager(this)
        adapter = CategoryAdapter(originalData, this)
        rvCategories.adapter = adapter
    }

    // Setup Search Bar untuk filter lokal
    private fun setupSearchBar() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecyclerView(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Filter data berdasarkan input di Search Bar
    private fun filterRecyclerView(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalData
        } else {
            originalData.filter { it.name.contains(query, ignoreCase = true) }
        }
        adapter.updateData(filteredList)
    }

    // Load data kuliner dari API
    private fun loadCulinaryData() {
        progressBar.visibility = View.VISIBLE
        val call = ApiClient.apiService.getRandomCulinaries()
        call.enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val places = response.body()?.getPlaces() ?: emptyList()
                    originalData.clear()
                    originalData.addAll(places)
                    adapter.updateData(originalData)
                } else {
                    Toast.makeText(this@RestoranActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@RestoranActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Navigate to FilterRestoranActivity
    private fun navigateToFilterActivity() {
        val intent = Intent(this, FilterRestoranActivity::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
    }

    // Setup Bottom Navigation
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

    // Fungsi untuk navigasi ke halaman lain
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
