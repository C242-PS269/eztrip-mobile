package com.project.capstone.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.project.capstone.MainActivity
import com.project.capstone.R
import com.project.capstone.ui.profile.ProfileActivity
import com.project.capstone.data.ThemeDataStore
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var themeDataStore: ThemeDataStore
    private var isThemeSwitchInitialized = false // Flag to prevent infinite loop

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ThemeDataStore
        themeDataStore = ThemeDataStore(applicationContext)

        // Apply theme before setting content view
        lifecycleScope.launch {
            themeDataStore.darkMode.collect { isDarkMode ->
                setAppTheme(isDarkMode)
                setContentView(R.layout.activity_setting)
                initViews()
            }
        }
    }

    private fun initViews() {
        val switchTheme = findViewById<SwitchCompat>(R.id.switchTheme)
        val switchNotification = findViewById<SwitchCompat>(R.id.switchNotification)

        // Load Dark Mode Setting
        lifecycleScope.launch {
            themeDataStore.darkMode.collect { isDarkMode ->
                // Prevent listener from being triggered on initial load
                if (!isThemeSwitchInitialized) {
                    switchTheme.isChecked = isDarkMode
                    isThemeSwitchInitialized = true
                }
            }
        }

        // Dark Mode Switch Listener
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isThemeSwitchInitialized) {
                lifecycleScope.launch {
                    themeDataStore.saveDarkMode(isChecked)
                    finish() // Finish current activity
                    startActivity(intent) // Restart the activity to apply theme
                }
            }
        }

        // Notification Switch
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        switchNotification.isChecked = sharedPreferences.getBoolean("notifications", true)
        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("notifications", isChecked).apply()
        }
//
//        // Change Account
//        val tvChangeAccount = findViewById<TextView>(R.id.tvChangeAccount)
//        tvChangeAccount.setOnClickListener {
//            val intent = Intent(this, ProfileActivity::class.java)
//            startActivity(intent)
//        }


        // Logout Button
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun setAppTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            setTheme(R.style.Theme_Capstone_Dark)
        } else {
            setTheme(R.style.Theme_Capstone)
        }
    }

    private fun logoutUser() {
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
