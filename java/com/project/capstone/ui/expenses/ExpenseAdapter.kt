package com.project.capstone.ui.expenses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.capstone.R
import com.project.capstone.database.expenses.Expenses

class ExpenseAdapter(private val expenseList: MutableList<Expenses>) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    // Create ViewHolder for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    // Bind data to each item view
    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenseList[position]
        holder.bind(expense)
    }

    // Get the total count of items in the list
    override fun getItemCount(): Int = expenseList.size

    // ViewHolder class to hold and bind individual expense data
    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)

        // Bind expense data to views
        fun bind(expense: Expenses) {
            tvAmount.text = "Rp ${expense.amount}" // Format as currency
            tvDescription.text = expense.description // Show description
        }
    }

    // Method to update the list of expenses
    fun updateExpenses(newExpenses: List<Expenses>) {
        expenseList.clear()
        expenseList.addAll(newExpenses) // Add all new items to the list
        notifyDataSetChanged() // Notify RecyclerView to update
    }
}
