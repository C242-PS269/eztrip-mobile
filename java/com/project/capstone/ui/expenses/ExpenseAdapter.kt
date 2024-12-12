package com.project.capstone.ui.expenses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.project.capstone.R
import com.project.capstone.database.expenses.ExpenseRepository
import com.project.capstone.database.expenses.Expenses
import kotlinx.coroutines.launch

class ExpenseAdapter(
    private val expenseList: MutableList<Expenses>,
    private val expenseRepository: ExpenseRepository,
    private val lifecycleOwner: LifecycleOwner // Add lifecycleOwner to constructor
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

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
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory) // Add TextView for category
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        // Bind expense data to views
        fun bind(expense: Expenses) {
            tvAmount.text = "Rp ${expense.amount.formatAsCurrency()}"
            tvDescription.text = expense.description
            tvCategory.text = expense.category // Display the category

            // Set up the delete button
            btnDelete.setOnClickListener {
                // Launch a coroutine to delete the expense
                lifecycleOwner.lifecycleScope.launch {
                    try {
                        expenseRepository.deleteExpense(expense)
                        expenseList.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                        Toast.makeText(itemView.context, "Expense deleted", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(itemView.context, "Error deleting expense", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
