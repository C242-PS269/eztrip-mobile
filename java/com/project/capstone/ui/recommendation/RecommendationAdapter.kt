package com.project.capstone.ui.recommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.capstone.databinding.ItemRekomendasiBinding

data class Recommendation(val title: String, val price: String, val city: String)

class RecommendationAdapter(
    private val onSaveToFavorites: (Recommendation) -> Unit
) : RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    private val items = mutableListOf<Recommendation>()

    fun submitList(newItems: List<Recommendation>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemRekomendasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class RecommendationViewHolder(private val binding: ItemRekomendasiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recommendation: Recommendation) {
            binding.tvRecommendationTitle.text = recommendation.title
            binding.tvTitle.text = recommendation.title
            binding.tvPrice.text = recommendation.price
            binding.tvCity.text = recommendation.city

            binding.root.setOnClickListener {
                onSaveToFavorites(recommendation)
            }
        }
    }
}
