package com.project.capstone.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.capstone.R
import com.project.capstone.network.models.Review

class ReviewAdapter(private val reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.tvRating.text = "Rating: ${review.rating}"
        holder.tvReview.text = review.reviews
        holder.tvUsername.text = "By: ${review.username}"
        holder.tvSentiment.text = "Sentiment: ${review.sentiment}"
    }

    override fun getItemCount(): Int = reviews.size

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        val tvReview: TextView = itemView.findViewById(R.id.tvReview)
        val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        val tvSentiment: TextView = itemView.findViewById(R.id.tvSentiment)
    }
}
