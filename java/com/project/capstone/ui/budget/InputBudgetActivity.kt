package com.project.capstone.ui.budget

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.project.capstone.databinding.ActivityInputBudgetBinding
import com.project.capstone.network.ApiClient
import com.project.capstone.network.models.ItineraryRequest
import com.project.capstone.network.models.ItineraryResponse
import com.project.capstone.ui.recommendation.RecommendationActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputBudgetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputBudgetBinding
    private val userId = "3Z4QAPG6CBD7amLRN9D8KvHbmrfTpi2KKKqx" // Contoh user_id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            val budget = binding.editTextNumber.text.toString().trim()
            val city = binding.editTextKeterangan.text.toString().trim()

            if (budget.isNotEmpty()) {
                postItinerary(budget.toInt(), city)
            } else {
                Toast.makeText(this, "Budget harus diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun postItinerary(budget: Int, city: String) {
        showProgressBar(true) // Menampilkan ProgressBar
        val request = ItineraryRequest(userId, budget, city)
        ApiClient.apiService.postItinerary(request).enqueue(object : Callback<ItineraryResponse> {
            override fun onResponse(call: Call<ItineraryResponse>, response: Response<ItineraryResponse>) {
                showProgressBar(false) // Menyembunyikan ProgressBar
                if (response.isSuccessful) {
                    val itineraryResponse = response.body()
                    navigateToRecommendation(itineraryResponse)
                } else {
                    Toast.makeText(this@InputBudgetActivity, "Gagal mendapatkan itinerary", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ItineraryResponse>, t: Throwable) {
                showProgressBar(false) // Menyembunyikan ProgressBar
                Toast.makeText(this@InputBudgetActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToRecommendation(itinerary: ItineraryResponse?) {
        val intent = Intent(this, RecommendationActivity::class.java)
        intent.putExtra("itinerary_data", itinerary)
        intent.putExtra("user_id", userId)
        startActivity(intent)
    }

    private fun showProgressBar(isVisible: Boolean) {
        binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}
