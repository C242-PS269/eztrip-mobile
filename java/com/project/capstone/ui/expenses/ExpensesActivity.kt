package com.project.capstone.ui.expenses

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.capstone.R
import com.project.capstone.database.AppDatabase
import com.project.capstone.database.expenses.ExpenseRepository
import com.project.capstone.database.expenses.Expenses
import com.project.capstone.ui.budget.SetBudgetActivity
import com.project.capstone.ui.favorites.FavoritesActivity
import com.project.capstone.ui.home.HomePageActivity
import com.project.capstone.ui.profile.ProfileActivity
import kotlinx.coroutines.launch

class ExpensesActivity : AppCompatActivity() {

    private var userId: Int = -1 // Ensure userId is retrieved correctly
    private lateinit var expensesRecyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private val expensesList = mutableListOf<Expenses>()
    private lateinit var expenseRepository: ExpenseRepository
    private lateinit var totalAmountTextView: TextView // Reference to the Total Amount TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)

        // Retrieve userId from Intent extras
        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID not found. Please ensure you are logged in.", Toast.LENGTH_LONG).show()
            finish() // Close the activity if userId is invalid
            return
        }

        // Initialize repository
        val db = AppDatabase.getDatabase(this)
        expenseRepository = ExpenseRepository(db.expenseDao())

        // Reference the Total Amount TextView
        totalAmountTextView = findViewById(R.id.tvTotalAmount)

        // Setup Bottom Navigation
        setupBottomNavigation()

        // Handle "Add Manual" button click
        val addManualLayout = findViewById<LinearLayout>(R.id.addManualLayout)
        addManualLayout.setOnClickListener {
            navigateToManualExpenseActivity()
        }

        // Setup RecyclerView for displaying expenses
        expensesRecyclerView = findViewById(R.id.rvExpenses)
        expenseAdapter = ExpenseAdapter(expensesList)
        expensesRecyclerView.layoutManager = LinearLayoutManager(this)
        expensesRecyclerView.adapter = expenseAdapter

        // Load expenses from the database and calculate total
        loadExpensesFromDatabase()
    }

    private fun loadExpensesFromDatabase() {
        // Use lifecycleScope to load expenses asynchronously
        lifecycleScope.launch {
            try {
                val expenses = expenseRepository.getExpensesForUser(userId)
                expensesList.clear()
                expensesList.addAll(expenses)

                // Calculate the total amount
                val totalAmount = expenses.sumOf { it.amount }

                // Update the total amount TextView
                runOnUiThread {
                    totalAmountTextView.text = "Total: Rp ${totalAmount.formatAsCurrency()}"
                    expenseAdapter.notifyDataSetChanged() // Update the UI
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@ExpensesActivity, "Error loading expenses: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToManualExpenseActivity() {
        val intent = Intent(this, ManualExpenseActivity::class.java)
        intent.putExtra("user_id", userId) // Pass userId to ManualExpenseActivity
        startActivityForResult(intent, REQUEST_CODE_ADD_EXPENSE)
    }

    // Handle the result from ManualExpenseActivity when a new expense is added
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_EXPENSE && resultCode == RESULT_OK) {
            val expense = data?.getSerializableExtra("expense") as? Expenses
            expense?.let {
                // Update the total amount and reload expenses from database
                loadExpensesFromDatabase() // This will ensure we get the latest data from the database
            }
        }
    }

    private fun setupBottomNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.menu_expenses
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    navigateToActivity(HomePageActivity::class.java)
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
        intent.putExtra("user_id", userId) // Pass userId to the destination activity
        startActivity(intent)
    }

    companion object {
        private const val REQUEST_CODE_ADD_EXPENSE = 1
    }
}

// Extension function to format numbers as currency
fun Double.formatAsCurrency(): String {
    return String.format("%,.2f", this)
}
