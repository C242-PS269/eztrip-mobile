package com.project.capstone.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.capstone.R

class ImageSlideAdapter(private val imageList: List<Int>) :
    RecyclerView.Adapter<ImageSlideAdapter.ImageSlideViewHolder>() {

    class ImageSlideViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageSlide)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSlideViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_slider, parent, false)
        return ImageSlideViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageSlideViewHolder, position: Int) {
        Glide.with(holder.imageView.context)
            .load(imageList[position])
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = imageList.size
}
