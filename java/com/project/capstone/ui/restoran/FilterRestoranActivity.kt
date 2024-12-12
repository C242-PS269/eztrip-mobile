package com.project.capstone.ui.restoran

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
import com.project.capstone.network.models.CulinaryFilterRequest
import com.project.capstone.network.models.PlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilterRestoranActivity : AppCompatActivity() {

    private lateinit var etMaxPrice: EditText
    private lateinit var etMinRating: EditText
    private lateinit var etCategory: EditText
    private lateinit var etCity: EditText
    private lateinit var btnApplyFilter: Button
    private lateinit var progressBar: ProgressBar
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_restoran)

        userId = intent.getIntExtra("user_id", -1)

        etMaxPrice = findViewById(R.id.etMaxPrice)
        etMinRating = findViewById(R.id.etMinRating)
        etCategory = findViewById(R.id.etCategory)
        etCity = findViewById(R.id.etCity)
        btnApplyFilter = findViewById(R.id.btnApplyFilter)
        progressBar = findViewById(R.id.progressBar)

        btnApplyFilter.setOnClickListener {
            val maxPrice = etMaxPrice.text.toString().toIntOrNull()
            val minRating = etMinRating.text.toString().toDoubleOrNull()
            val category = etCategory.text.toString().trim()
            val city = etCity.text.toString().trim()

            val filterRequest = CulinaryFilterRequest(maxPrice, minRating, category, city)
            sendFilterRequest(filterRequest)
        }
    }

    private fun sendFilterRequest(filterRequest: CulinaryFilterRequest) {
        progressBar.visibility = View.VISIBLE
        val call = ApiClient.apiService.getFilteredCulinaries(filterRequest)
        call.enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val filteredPlaces = response.body()?.getPlaces() ?: emptyList()

                    if (filteredPlaces.isEmpty()) {
                        Toast.makeText(this@FilterRestoranActivity, "No results found", Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(this@FilterRestoranActivity, RestoranActivity::class.java).apply {
                            putParcelableArrayListExtra("filtered_places", ArrayList(filteredPlaces))
                            putExtra("user_id", userId)
                        }
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(this@FilterRestoranActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@FilterRestoranActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("FilterRestoranActivity", "API Failure: ${t.message}", t)
            }
        })
    }
}
