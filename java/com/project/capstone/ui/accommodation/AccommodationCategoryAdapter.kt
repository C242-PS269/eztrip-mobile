package com.project.capstone.ui.accommodation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.capstone.R
import com.project.capstone.network.models.AccommodationPlace

class AccommodationCategoryAdapter(
    private var items: List<AccommodationPlace>, // Make it mutable to allow updates
    private val context: Context
) : RecyclerView.Adapter<AccommodationCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvCity: TextView = itemView.findViewById(R.id.tvCity)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.name
        holder.tvCategory.text = "Category: ${item.category}"
        holder.tvCity.text = "City: ${item.city}"
        holder.tvPrice.text = "Price: Rp ${item.price_wna}"
        holder.tvRating.text = "Rating: ${item.rating}"
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<AccommodationPlace>) {
        items = newItems // Update data
        notifyDataSetChanged() // Notify RecyclerView
    }
}
