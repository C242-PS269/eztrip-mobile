package com.project.capstone.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.project.capstone.R
import com.project.capstone.ui.kategori.ActivityKategoriItem
import com.project.capstone.ui.kategori.ActivityKategoriItem2
import com.project.capstone.ui.kategori.ActivityKategoriItem3
import com.project.capstone.ui.expenses.ExpensesActivity
import com.project.capstone.ui.favorites.FavoritesActivity
import com.project.capstone.ui.budget.SetBudgetActivity
import com.project.capstone.ui.profile.ProfileActivity
import com.project.capstone.ui.wisata.WisataActivity
import com.project.capstone.ui.akomodasi.AkomodasiActivity
import com.project.capstone.ui.restoran.RestoranActivity
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class HomePageActivity : AppCompatActivity() {

    private lateinit var tvGreeting: TextView
    private lateinit var ivNotification: ImageView
    private lateinit var etSearch: TextInputEditText
    private lateinit var rvRecommendations: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView

    private lateinit var viewPager: ViewPager2
    private lateinit var dotsIndicator: WormDotsIndicator
    private lateinit var imageSlideAdapter: ImageSlideAdapter

    private var userId: Int = -1

    private val imageList = listOf(
        R.drawable.toursbanner,
        R.drawable.culinariesbanner
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Retrieve user ID from the Intent
        userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Initialize UI components
        tvGreeting = findViewById(R.id.tvGreeting)
        ivNotification = findViewById(R.id.ivNotification)
        etSearch = findViewById(R.id.etSearch)
        rvRecommendations = findViewById(R.id.rvRecommendations)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        viewPager = findViewById(R.id.viewPagerRecommendations)
        dotsIndicator = findViewById(R.id.dotsIndicator)

        // Set greeting text
        tvGreeting.text = getString(R.string.greeting_text, "User")

        // Setup Image Slider
        setupImageSlider()

        // Setup Recommendations RecyclerView
        setupRecommendationsRecyclerView()

        // Setup Category Navigation
        setupCategoryNavigation()

        // Setup Bottom Navigation
        setupBottomNavigation()
    }

    private fun setupImageSlider() {
        // Set adapter untuk ViewPager2
        imageSlideAdapter = ImageSlideAdapter(imageList)
        viewPager.adapter = imageSlideAdapter
        dotsIndicator.setViewPager2(viewPager)

        // Automatically scroll the image slider
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            var currentPage = 0
            override fun run() {
                if (currentPage == imageList.size) currentPage = 0
                viewPager.setCurrentItem(currentPage++, true)
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(runnable, 3000)
    }

    private fun setupRecommendationsRecyclerView() {
        rvRecommendations.layoutManager = LinearLayoutManager(this)
        rvRecommendations.adapter = RecommendationsAdapter(getSampleRecommendations()) { recommendation ->
            onRecommendationClicked(recommendation)
        }
    }

    private fun getSampleRecommendations(): List<String> {
        return listOf("Wisata", "Akomodasi", "Restoran")
    }

    private fun onRecommendationClicked(recommendation: String) {
        val intent = when (recommendation) {
            "Wisata" -> Intent(this, ActivityKategoriItem::class.java)
            "Akomodasi" -> Intent(this, ActivityKategoriItem2::class.java)
            "Restoran" -> Intent(this, ActivityKategoriItem3::class.java)
            else -> {
                Toast.makeText(this, "Kategori belum tersedia", Toast.LENGTH_SHORT).show()
                return
            }
        }
        intent.putExtra("category_title", recommendation)
        startActivity(intent)
    }

    private fun setupCategoryNavigation() {
        val wisataIcon = findViewById<ImageView>(R.id.ivWisata)
        val akomodasiIcon = findViewById<ImageView>(R.id.ivAkomodasi)
        val restoranIcon = findViewById<ImageView>(R.id.ivRestoran)

        wisataIcon.setOnClickListener {
            navigateToActivity(WisataActivity::class.java)
        }

        akomodasiIcon.setOnClickListener {
            navigateToActivity(AkomodasiActivity::class.java)
        }

        restoranIcon.setOnClickListener {
            navigateToActivity(RestoranActivity::class.java)
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_expenses -> navigateToActivity(ExpensesActivity::class.java)
                R.id.menu_favorites -> navigateToActivity(FavoritesActivity::class.java)
                R.id.menu_set_budget -> navigateToActivity(SetBudgetActivity::class.java)
                R.id.menu_profile -> navigateToActivity(ProfileActivity::class.java)
                else -> false
            }
        }
    }

    private fun <T> navigateToActivity(destination: Class<T>): Boolean {
        val intent = Intent(this, destination)
        intent.putExtra("user_id", userId)
        startActivity(intent)
        return true
    }
}
