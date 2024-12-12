package com.project.capstone.ui.signup

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
import com.project.capstone.database.profile.Profile
import com.project.capstone.database.profile.ProfileRepository
import com.project.capstone.database.user.User
import com.project.capstone.database.user.UserRepository
import com.project.capstone.database.expenses.Expenses
import com.project.capstone.database.expenses.ExpenseRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {

    private var arePasswordsVisible = false
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val db = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(db.userDao())
        val profileRepository = ProfileRepository(db.profileDao())
        val expenseRepository = ExpenseRepository(db.expenseDao())

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val tvTogglePasswords = findViewById<TextView>(R.id.tvTogglePasswords)
        progressBar = findViewById(R.id.progressBar) // Tambahkan progress bar di layout

        progressBar.visibility = View.GONE

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
            etPassword.setSelection(etPassword.text.length)
            etConfirmPassword.setSelection(etConfirmPassword.text.length)
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

            progressBar.visibility = View.VISIBLE // Tampilkan progress bar

            lifecycleScope.launch {
                delay(1000) // Delay 1 detik

                val existingUser = userRepository.getUserByUsername(username)
                if (existingUser != null) {
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@SignUpActivity, "Username sudah digunakan!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val userId = userRepository.insertUser(User(username = username, password = password))

                    profileRepository.insertProfile(Profile(userId = userId.toInt()))

                    val defaultCategory = "General"
                    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val defaultExpense = Expenses(
                        userId = userId.toInt(),
                        amount = 0.0,
                        description = "Initial Expense",
                        category = defaultCategory,
                        date = currentDate
                    )
                    expenseRepository.insertExpense(defaultExpense)

                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@SignUpActivity, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}
