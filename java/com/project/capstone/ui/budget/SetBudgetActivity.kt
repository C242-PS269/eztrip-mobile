package com.project.capstone.ui.budget

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.project.capstone.R
import com.project.capstone.databinding.ActivitySetBudgetBinding
import com.project.capstone.ui.expenses.ExpensesActivity
import com.project.capstone.ui.home.HomePageActivity
import com.project.capstone.ui.profile.ProfileActivity
import com.project.capstone.ui.favorites.FavoritesActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SetBudgetActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivitySetBudgetBinding

    // ViewModel
    private val budgetViewModel: BudgetViewModel by viewModels()

    // User ID
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivitySetBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve user ID from Intent
        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Setup bottom navigation
        setupBottomNavigation()

        // Observe expenses data
        budgetViewModel.expenses.observe(this, Observer { expenses ->
            // Update UI with the expenses data
            binding.tvTotalAmount.text = "Total: Rp ${budgetViewModel.getTotalAmount()}"
        })

        // Load expenses data
        budgetViewModel.loadExpenses()

        // Handle the "Set Budget" button click to navigate to InputBudgetActivity
        binding.addManualLayout.setOnClickListener {
            navigateToInputBudgetActivity()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> navigateToActivity(HomePageActivity::class.java)
                R.id.menu_expenses -> navigateToActivity(ExpensesActivity::class.java)
                R.id.menu_set_budget -> navigateToActivity(SetBudgetActivity::class.java)
                R.id.menu_favorites -> navigateToActivity(FavoritesActivity::class.java)
                R.id.menu_profile -> navigateToActivity(ProfileActivity::class.java)
                else -> false
            }
            true
        }
    }

    private fun <T> navigateToActivity(destination: Class<T>): Boolean {
        val intent = Intent(this, destination)
        intent.putExtra("user_id", userId) // Pass user_id to the target activity
        startActivity(intent)
        finish() // Optionally finish current activity to prevent navigating back
        return true
    }

    // New function to navigate to InputBudgetActivity
    private fun navigateToInputBudgetActivity() {
        val intent = Intent(this, InputBudgetActivity::class.java)
        intent.putExtra("user_id", userId) // Pass user_id to the target activity
        startActivity(intent)
    }
}
