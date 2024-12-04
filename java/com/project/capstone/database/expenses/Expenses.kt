package com.project.capstone.database.expenses

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "expenses")
data class Expenses(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Primary Key
    var userId: Int, // Change from val to var
    val amount: Double,
    val category: String,
    val description: String,
    val date: String
) : Serializable
