package com.project.capstone.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.project.capstone.R
import com.project.capstone.database.AppDatabase
import com.project.capstone.database.user.UserRepository
import com.project.capstone.ui.profile.ProfileActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private var isPasswordVisible = false
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)

        val userId = sharedPreferences.getInt("user_id", -1)
        if (userId != -1) {
            val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
            finish()
        }

        val db = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(db.userDao())

        val etUsername = findViewById<EditText>(R.id.etLoginUsername)
        val etPassword = findViewById<EditText>(R.id.etLoginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvTogglePassword = findViewById<TextView>(R.id.tvTogglePassword)
        progressBar = findViewById(R.id.progressBar) // Tambahkan progress bar di layout

        progressBar.visibility = View.GONE

        tvTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            etPassword.inputType = if (isPasswordVisible) {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            etPassword.setSelection(etPassword.text.length)
            tvTogglePassword.text = if (isPasswordVisible) "Hide" else "Show"
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Masukkan username dan password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE // Tampilkan progress bar

            lifecycleScope.launch {
                delay(1000) // Delay 1 detik
                val user = userRepository.checkUser(username, password)
                if (user != null) {
                    with(sharedPreferences.edit()) {
                        putInt("user_id", user.id)
                        putBoolean("is_logged_in", true)
                        apply()
                    }
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
                        intent.putExtra("user_id", user.id)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Username atau password salah!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
