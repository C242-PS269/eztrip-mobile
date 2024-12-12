package com.project.capstone.ui.favorites

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.project.capstone.databinding.ItemRekomendasiBinding
import com.project.capstone.model.FavoriteItem

class FavoriteAdapter(
    private var favorites: MutableList<FavoriteItem>,
    private val sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    fun updateFavorites(newFavorites: List<FavoriteItem>) {
        favorites.clear()
        favorites.addAll(newFavorites)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ItemRekomendasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteItem = favorites[position]
        holder.bind(favoriteItem, position + 1)
    }

    override fun getItemCount(): Int = favorites.size

    inner class FavoriteViewHolder(private val binding: ItemRekomendasiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteItem: FavoriteItem, recommendationNumber: Int) {
            binding.tvRecommendationTitle.text = "Recommendation $recommendationNumber"
            binding.tvTitle.text = favoriteItem.title
            binding.tvPrice.text = favoriteItem.price
            binding.tvCity.text = favoriteItem.city

            // Tombol Delete


        }
    }
}
