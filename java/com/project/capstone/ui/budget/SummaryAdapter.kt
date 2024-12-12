package com.project.capstone.ui.budget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.capstone.databinding.ItemSummaryBinding

data class Summary(
    val totalCost: String,
    val remainingBudget: String,
    val city: String
)

class SummaryAdapter : RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder>() {

    private val summaryList = mutableListOf<Summary>()

    fun submitList(newList: List<Summary>) {
        summaryList.clear()
        summaryList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val binding = ItemSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SummaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        holder.bind(summaryList[position])
    }

    override fun getItemCount(): Int = summaryList.size

    inner class SummaryViewHolder(private val binding: ItemSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(summary: Summary) {
            binding.tvTotalCost.text = "Total Cost: Rp ${summary.totalCost}"
            binding.tvRemainingBudget.text = "Remaining Budget: Rp ${summary.remainingBudget}"
            binding.tvCity.text = "City: ${summary.city}"
        }
    }
}
