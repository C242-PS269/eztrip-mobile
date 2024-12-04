package com.project.capstone.ui.budget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BudgetViewModel : ViewModel() {

    private val _expenses = MutableLiveData<List<Expense>>()
    val expenses: LiveData<List<Expense>> get() = _expenses

    private val _budget = MutableLiveData<Int>()
    val budget: LiveData<Int> get() = _budget

    private val _city = MutableLiveData<String>()
    val city: LiveData<String> get() = _city

    // Method to load expenses (can be replaced with database or API call)
    fun loadExpenses() {
        // Sample expenses data
        _expenses.value = listOf(
            Expense("Groceries", 0),
        )
    }

    // Calculate total amount
    fun getTotalAmount(): Int {
        return _expenses.value?.sumOf { it.amount } ?: 0
    }

    // Add budget and city (keterangan)
    fun addBudget(amount: Int, city: String) {
        _budget.value = amount
        _city.value = city
    }
}

// Expense data class
data class Expense(val name: String, val amount: Int)
