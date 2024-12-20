package com.project.capstone.ui.common

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.capstone.R
import com.project.capstone.network.models.Place
import com.project.capstone.ui.common.DetailActivity

class CategoryAdapter(private var items: List<Place>, private val context: Context) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvCity: TextView = itemView.findViewById(R.id.tvCity)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)  // TextView untuk Rating
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.name
        holder.tvCategory.text = "Category: ${item.category}"
        holder.tvCity.text = "City: ${item.city}"
        holder.tvPrice.text = "Price: Rp ${item.price_wna}"
        // Menampilkan rating
        holder.tvRating.text = "Rating: ${item.rating}"

        // Set click listener to navigate to DetailActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            // Kirim objek Place ke DetailActivity menggunakan Parcelable
            intent.putExtra("place", item)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Place>) {
        items = newItems
        notifyDataSetChanged()
    }
}
