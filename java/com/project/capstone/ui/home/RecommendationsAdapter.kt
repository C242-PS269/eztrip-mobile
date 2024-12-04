package com.project.capstone.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.capstone.R

class RecommendationsAdapter(private val recommendations: List<String>) :
    RecyclerView.Adapter<RecommendationsAdapter.ViewHolder>() {

    // ViewHolder class to hold references to the views in each item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRecommendation: TextView = itemView.findViewById(R.id.tvRecommendation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout for each RecyclerView item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommendation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to the TextView in each item
        holder.tvRecommendation.text = recommendations[position]
    }

    override fun getItemCount(): Int = recommendations.size
}
