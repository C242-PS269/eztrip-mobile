package com.project.capstone.ui.kategori

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.capstone.databinding.ActivityKategoriItemBinding
import com.project.capstone.network.ApiClient
import com.project.capstone.network.models.AccommodationPlace
import com.project.capstone.network.models.AccommodationPlaceResponse
import com.project.capstone.network.models.AccommodationVisitedRequest
import com.project.capstone.ui.accommodation.AccommodationCategoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityKategoriItem2 : AppCompatActivity() {

    private lateinit var binding: ActivityKategoriItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKategoriItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryTitle = intent.getStringExtra("category_title")
        binding.tvCategoryHeaderTitle.text = categoryTitle ?: "Akomodasi"

        fetchDataFromApi()
    }

    private fun fetchDataFromApi() {
        binding.progressBar.visibility = View.VISIBLE // Show ProgressBar

        val requestBody = AccommodationVisitedRequest(
            accommodation_name = "The Seiryu Boutique Villas",
            city_filter = "Badung",
            max_price = 500000
        )

        ApiClient.apiService.getVisitedAccommodations(requestBody)
            .enqueue(object : Callback<AccommodationPlaceResponse> {
                override fun onResponse(
                    call: Call<AccommodationPlaceResponse>,
                    response: Response<AccommodationPlaceResponse>
                ) {
                    binding.progressBar.visibility = View.GONE // Hide ProgressBar
                    if (response.isSuccessful) {
                        val accommodations = response.body()?.accommodations ?: emptyList()
                        setupRecyclerView(accommodations)
                    } else {
                        Toast.makeText(this@ActivityKategoriItem2, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AccommodationPlaceResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE // Hide ProgressBar
                    Toast.makeText(this@ActivityKategoriItem2, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupRecyclerView(accommodations: List<AccommodationPlace>) {
        binding.rvCategories.layoutManager = LinearLayoutManager(this)
        binding.rvCategories.adapter = AccommodationCategoryAdapter(accommodations, this)
    }
}
