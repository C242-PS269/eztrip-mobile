package com.project.capstone.ui.budget

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.project.capstone.R
import com.project.capstone.databinding.ActivityInputBudgetBinding

class InputBudgetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputBudgetBinding
    private val budgetViewModel: BudgetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityInputBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up button click listener
        binding.btnSubmit.setOnClickListener {
            val budgetAmountString = binding.editTextNumber.text.toString()
            val keterangan = binding.editTextKeterangan.text.toString()

            // Check if budget is provided
            if (budgetAmountString.isNotEmpty() && keterangan.isNotEmpty()) {
                try {
                    val budgetAmount = budgetAmountString.toInt()
                    // Save budget and keterangan (city)
                    budgetViewModel.addBudget(budgetAmount, keterangan)
                    Toast.makeText(this, "Budget berhasil diset: Rp $budgetAmount untuk $keterangan", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity after submission
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Harap masukkan jumlah budget yang valid", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Harap masukkan budget dan keterangan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
