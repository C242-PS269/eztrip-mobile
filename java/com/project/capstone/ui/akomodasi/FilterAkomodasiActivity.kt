package com.project.capstone.ui.akomodasi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.capstone.R
import com.project.capstone.network.ApiClient
import com.project.capstone.network.models.AccommodationFilterRequest
import com.project.capstone.network.models.PlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilterAkomodasiActivity : AppCompatActivity() {

    private lateinit var etMaxPrice: EditText
    private lateinit var etMinRating: EditText
    private lateinit var etCity: EditText
    private lateinit var btnApplyFilter: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_akomodasi)

        // Initialize components
        etMaxPrice = findViewById(R.id.etMaxPrice)
        etMinRating = findViewById(R.id.etMinRating)
        etCity = findViewById(R.id.etCity)
        btnApplyFilter = findViewById(R.id.btnApplyFilter)
        progressBar = findViewById(R.id.progressBar)

        btnApplyFilter.setOnClickListener {
            val city = etCity.text.toString().trim()
            val maxPrice = etMaxPrice.text.toString().toIntOrNull()
            val minRating = etMinRating.text.toString().toDoubleOrNull()

            if (city.isEmpty()) {
                Toast.makeText(this, "City is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val filter = AccommodationFilterRequest(city, minRating, maxPrice)
            applyFilter(filter)
        }
    }

    private fun applyFilter(filter: AccommodationFilterRequest) {
        progressBar.visibility = View.VISIBLE
        val call = ApiClient.apiService.getFilteredAccommodations(filter)

        call.enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val places = response.body()?.items ?: emptyList()
                    if (places.isEmpty()) {
                        Toast.makeText(this@FilterAkomodasiActivity, "No accommodations found", Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(this@FilterAkomodasiActivity, AkomodasiActivity::class.java).apply {
                            putParcelableArrayListExtra("filtered_accommodations", ArrayList(places))
                        }
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(this@FilterAkomodasiActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@FilterAkomodasiActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("FilterAkomodasiActivity", "API Failure: ${t.message}", t)
            }
        })
    }
}
