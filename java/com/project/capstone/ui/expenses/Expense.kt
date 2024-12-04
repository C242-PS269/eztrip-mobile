package com.project.capstone.ui.expenses

import java.io.Serializable

data class Expense(
    val amount: Double,
    val description: String
) : Serializable
