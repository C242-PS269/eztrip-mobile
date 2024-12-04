package com.project.capstone.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.project.capstone.R
import com.project.capstone.database.AppDatabase
import com.project.capstone.database.user.UserRepository
import com.project.capstone.ui.profile.ProfileActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(db.userDao())

        val etUsername = findViewById<EditText>(R.id.etLoginUsername)
        val etPassword = findViewById<EditText>(R.id.etLoginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvTogglePassword = findViewById<TextView>(R.id.tvTogglePassword)

        // Toggle visibility for password
        tvTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            etPassword.inputType = if (isPasswordVisible) {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            etPassword.setSelection(etPassword.text.length) // Retain cursor position
            tvTogglePassword.text = if (isPasswordVisible) "Hide" else "Show"
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Masukkan username dan password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = userRepository.checkUser(username, password)
                if (user != null) {
                    println("DEBUG: Logging in user with ID = ${user.id}")
                    val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
                    intent.putExtra("user_id", user.id)
                    startActivity(intent)
                    finish()
                } else {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Username atau password salah!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
