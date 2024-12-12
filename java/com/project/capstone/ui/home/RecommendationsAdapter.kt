package com.project.capstone.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.capstone.R

class RecommendationsAdapter(
    private val recommendations: List<String>,
    private val onClick: (String) -> Unit // Tambahkan callback untuk klik item
) : RecyclerView.Adapter<RecommendationsAdapter.ViewHolder>() {

    // ViewHolder untuk menyimpan referensi ke elemen UI
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRecommendation: TextView = itemView.findViewById(R.id.tvRecommendation)

        init {
            itemView.setOnClickListener {
                val recommendation = recommendations[adapterPosition]
                onClick(recommendation) // Panggil callback saat item diklik
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommendation, parent, false) // Layout item
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvRecommendation.text = recommendations[position] // Set teks rekomendasi
    }

    override fun getItemCount(): Int = recommendations.size
}
