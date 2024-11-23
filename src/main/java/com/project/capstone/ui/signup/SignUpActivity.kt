package com.project.capstone.ui.signup

import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.project.capstone.R
import com.project.capstone.database.AppDatabase
import com.project.capstone.database.profile.Profile
import com.project.capstone.database.profile.ProfileRepository
import com.project.capstone.database.user.User
import com.project.capstone.database.user.UserRepository
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private var arePasswordsVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val db = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(db.userDao())
        val profileRepository = ProfileRepository(db.profileDao())

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val tvTogglePasswords = findViewById<TextView>(R.id.tvTogglePasswords)

        // Toggle visibility for password fields
        tvTogglePasswords.setOnClickListener {
            arePasswordsVisible = !arePasswordsVisible
            val inputType = if (arePasswordsVisible) {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            etPassword.inputType = inputType
            etConfirmPassword.inputType = inputType
            etPassword.setSelection(etPassword.text.length) // Retain cursor position
            etConfirmPassword.setSelection(etConfirmPassword.text.length) // Retain cursor position
            tvTogglePasswords.text = if (arePasswordsVisible) "Hide" else "Show"
        }

        btnSignUp.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Semua bidang harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password tidak cocok!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val existingUser = userRepository.getUserByUsername(username)
                if (existingUser != null) {
                    runOnUiThread {
                        Toast.makeText(this@SignUpActivity, "Username sudah digunakan!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val userId = userRepository.insertUser(User(username = username, password = password))
                    profileRepository.insertProfile(Profile(userId = userId.toInt()))
                    runOnUiThread {
                        Toast.makeText(this@SignUpActivity, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}
