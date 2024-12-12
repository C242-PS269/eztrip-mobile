package com.project.capstone.database.expenses

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {

    // Insert a new expense into the database
    @Insert
    suspend fun insertExpense(expense: Expenses)

    // Retrieve all expenses for a specific user based on userId
    @Query("SELECT * FROM expenses WHERE userId = :userId")
    suspend fun getExpensesForUser(userId: Int): List<Expenses>

    // Delete all expenses for a specific user
    @Query("DELETE FROM expenses WHERE userId = :userId")
    suspend fun deleteExpensesForUser(userId: Int)
}
