package com.project.capstone.ui.restoran

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.capstone.R
import com.project.capstone.ui.budget.SetBudgetActivity
import com.project.capstone.ui.home.HomePageActivity
import com.project.capstone.ui.expenses.ExpensesActivity
import com.project.capstone.ui.favorites.FavoritesActivity
import com.project.capstone.ui.profile.ProfileActivity
import com.project.capstone.ui.common.CategoryAdapter

class RestoranActivity : AppCompatActivity() {

    private var userId: Int = -1
    private lateinit var rvCategories: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var adapter: CategoryAdapter
    private val originalData = listOf(
        "Restoran Sederhana",
        "Bale Udang Mang Engking",
        "Warung Bu Kris",
        "Cafe D'Pakar",
        "Kopi Kangen"
    )

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

        // Initialize RecyclerView
        rvCategories = findViewById(R.id.rvCategories)
        setupRecyclerView()

        // Initialize Search Bar
        searchBar = findViewById(R.id.etSearch)
        setupSearchBar()

        // Initialize Bottom Navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        setupBottomNavigation(bottomNavigation)
    }

    private fun setupRecyclerView() {
        rvCategories.layoutManager = LinearLayoutManager(this)
        adapter = CategoryAdapter(originalData.toMutableList())
        rvCategories.adapter = adapter
    }

    private fun setupSearchBar() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecyclerView(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterRecyclerView(query: String) {
        val filteredList = if (query.isEmpty()) {
            originalData
        } else {
            originalData.filter { it.contains(query, ignoreCase = true) }
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
        intent.putExtra("user_id", userId) // Pass the user_id to the target activity
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        val intent = Intent(this, HomePageActivity::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
        finish()
    }
}
