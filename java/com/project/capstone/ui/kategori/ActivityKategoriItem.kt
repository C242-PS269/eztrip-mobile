package com.project.capstone.ui.kategori

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.capstone.R
import com.project.capstone.databinding.ActivityKategoriItemBinding
import com.project.capstone.network.ApiClient
import com.project.capstone.network.models.Place
import com.project.capstone.network.models.PlaceResponse
import com.project.capstone.network.models.TourVisitedRequest
import com.project.capstone.ui.common.CategoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityKategoriItem : AppCompatActivity() {

    private lateinit var binding: ActivityKategoriItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKategoriItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryTitle = intent.getStringExtra("category_title")
        binding.tvCategoryHeaderTitle.text = categoryTitle ?: "Wisata"

        fetchDataFromApi()
    }

    private fun fetchDataFromApi() {
        binding.progressBar.visibility = View.VISIBLE // Show ProgressBar

        val requestBody = TourVisitedRequest(
            tour_name = "Balangan Beach",
            city_filter = "Badung",
            max_price = 100000
        )

        ApiClient.apiService.getVisitedTours(requestBody).enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                binding.progressBar.visibility = View.GONE // Hide ProgressBar
                if (response.isSuccessful) {
                    val places = response.body()?.tours ?: emptyList()
                    setupRecyclerView(places)
                } else {
                    Toast.makeText(this@ActivityKategoriItem, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE // Hide ProgressBar
                Toast.makeText(this@ActivityKategoriItem, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView(places: List<Place>) {
        binding.rvCategories.layoutManager = LinearLayoutManager(this)
        binding.rvCategories.adapter = CategoryAdapter(places, this)
    }
}
