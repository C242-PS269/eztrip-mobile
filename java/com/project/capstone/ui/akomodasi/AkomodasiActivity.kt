package com.project.capstone.ui.akomodasi

import android.content.Intent
import android.os.Bundle
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
import com.project.capstone.ui.expenses.ExpensesActivity
import com.project.capstone.ui.favorites.FavoritesActivity
import com.project.capstone.ui.home.HomePageActivity
import com.project.capstone.ui.profile.ProfileActivity
import com.project.capstone.ui.common.CategoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class AkomodasiActivity : AppCompatActivity() {

    private var userId: Int = -1
    private lateinit var rvCategories: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var btnFilter: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: CategoryAdapter
    private var accommodationList: List<Place> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akomodasi)

        // Retrieve user ID from Intent
        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            Log.e("AkomodasiActivity", "User ID not found in intent")
            finish()
            return
        }

        // Initialize UI Components
        rvCategories = findViewById(R.id.rvCategories)
        rvCategories.layoutManager = LinearLayoutManager(this)
        searchBar = findViewById(R.id.etSearch)
        btnFilter = findViewById(R.id.ivFilter)
        progressBar = findViewById(R.id.progressBar)

        // Set OnClickListener untuk tombol Filter
        btnFilter.setOnClickListener {
            val intent = Intent(this, FilterAkomodasiActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }

        // Check if filtered accommodations are passed
        val filteredAccommodations =
            intent.getParcelableArrayListExtra<Place>("filtered_accommodations")
        if (filteredAccommodations != null && filteredAccommodations.isNotEmpty()) {
            accommodationList = filteredAccommodations
            setupRecyclerView(accommodationList)
        } else {
            // Fetch data from API if no filtered data is provided
            fetchAccommodationData()
        }

        // Initialize Bottom Navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        setupBottomNavigation(bottomNavigation)
    }

    private fun fetchAccommodationData() {
        progressBar.visibility = View.VISIBLE
        val call = ApiClient.apiService.getRandomAccommodations()
        call.enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    accommodationList = response.body()?.items?.takeIf { it.isNotEmpty() } ?: emptyList()
                    setupRecyclerView(accommodationList)
                } else {
                    Toast.makeText(
                        this@AkomodasiActivity,
                        "Failed to fetch accommodations",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("AkomodasiActivity", "API Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@AkomodasiActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
                Log.e("AkomodasiActivity", "API Failure: ${t.message}", t)
            }
        })
    }

    private fun setupRecyclerView(data: List<Place>) {
        if (data.isEmpty()) {
            Toast.makeText(this, "No accommodations available", Toast.LENGTH_SHORT).show()
        }
        adapter = CategoryAdapter(data, this)
        rvCategories.adapter = adapter

        searchBar.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecyclerView(s.toString())
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun filterRecyclerView(query: String) {
        val filteredList = if (query.isEmpty()) {
            accommodationList
        } else {
            accommodationList.filter { it.name.contains(query, ignoreCase = true) }
        }
        adapter.updateData(filteredList)
    }

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

    private fun <T> navigateToActivity(destination: Class<T>) {
        val intent = Intent(this, destination)
        intent.putExtra("user_id", userId)
        startActivity(intent)
    }

    override fun onBackPressed() {
        val intent = Intent(this, HomePageActivity::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
        finish()
    }
}