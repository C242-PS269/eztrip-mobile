package com.project.capstone.ui.expenses

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.project.capstone.R
import com.project.capstone.database.AppDatabase
import com.project.capstone.database.expenses.ExpenseRepository
import com.project.capstone.database.expenses.Expenses
import kotlinx.coroutines.launch

class ManualExpenseActivity : AppCompatActivity() {

    private lateinit var expenseAmountEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var addExpenseButton: Button
    private lateinit var expenseRepository: ExpenseRepository
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_expense)

        // Retrieve userId from Intent extras
        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID not found. Please ensure you are logged in.", Toast.LENGTH_LONG).show()
            finish() // Close the activity if userId is invalid
            return
        }

        // Find views
        expenseAmountEditText = findViewById(R.id.editTextNumber)
        descriptionEditText = findViewById(R.id.editTextKeterangan)
        addExpenseButton = findViewById(R.id.btnSubmit)

        // Initialize the database and repository
        val db = AppDatabase.getDatabase(this)
        expenseRepository = ExpenseRepository(db.expenseDao())

        // Handle button click to save expense
        addExpenseButton.setOnClickListener {
            val amount = expenseAmountEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()

            if (amount.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show()
            } else {
                val expense = Expenses(
                    userId = userId, // Use the actual userId passed from ExpensesActivity
                    amount = amount.toDouble(),
                    category = "Miscellaneous",  // Set a default category or allow the user to select
                    description = description,
                    date = System.currentTimeMillis().toString() // Use current timestamp for the date
                )

                // Insert expense into the database
                lifecycleScope.launch {
                    expenseRepository.insertExpense(expense)
                    // After inserting, notify the user and return to the previous screen
                    runOnUiThread {
                        Toast.makeText(this@ManualExpenseActivity, "Expense added!", Toast.LENGTH_SHORT).show()
                        val resultIntent = Intent()
                        resultIntent.putExtra("expense", expense) // Return the new expense to ExpensesActivity
                        setResult(RESULT_OK, resultIntent)
                        finish()  // Close the activity and return to the previous screen
                    }
                }
            }
        }
    }
}
