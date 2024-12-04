package com.project.capstone.database.expenses

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    // Insert a new expense into the database
    suspend fun insertExpense(expense: Expenses) {
        expenseDao.insertExpense(expense)
    }

    // Get all expenses for a specific user
    suspend fun getExpensesForUser(userId: Int): List<Expenses> {
        return expenseDao.getExpensesForUser(userId)
    }

    // Delete all expenses for a specific user
    suspend fun deleteExpensesForUser(userId: Int) {
        expenseDao.deleteExpensesForUser(userId)
    }
}
