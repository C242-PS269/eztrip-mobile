package com.project.capstone.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.project.capstone.R
import com.project.capstone.database.AppDatabase
import com.project.capstone.database.profile.Profile
import com.project.capstone.database.profile.ProfileRepository
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private var profileId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val db = AppDatabase.getDatabase(this)
        val profileRepository = ProfileRepository(db.profileDao())

        // Get the userId passed through Intent
        val userId = intent.getIntExtra("user_id", -1)
        if (userId == -1) {
            Toast.makeText(this, "Error: User ID tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Bind UI elements
        val profileImage = findViewById<ImageView>(R.id.profile_image)
        val etName = findViewById<EditText>(R.id.et_name)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPhone = findViewById<EditText>(R.id.et_phone)
        val btnSave = findViewById<Button>(R.id.btn_save)

        // Load the profile data
        lifecycleScope.launch {
            val profile = profileRepository.getProfileByUserId(userId)
            profile?.let {
                runOnUiThread {
                    profileId = it.profileId
                    etName.setText(it.name)
                    etEmail.setText(it.email)
                    etPhone.setText(it.phone)

                    // Use Glide to load the profile image if available
                    if (it.imageUri.isNotEmpty()) {
                        imageUri = Uri.parse(it.imageUri)
                        Glide.with(this@EditProfileActivity)
                            .load(it.imageUri)
                            .placeholder(R.drawable.ic_profile_placeholder)
                            .into(profileImage)
                    } else {
                        profileImage.setImageResource(R.drawable.ic_profile_placeholder)
                    }
                }
            }
        }

        // Open image picker on image click
        profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        // Save button click listener
        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val imageUriString = imageUri?.toString() ?: ""

            // Validate input fields
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Semua bidang harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save profile data to database
            lifecycleScope.launch {
                val updatedProfile = Profile(
                    profileId = profileId,
                    userId = userId,
                    name = name,
                    email = email,
                    phone = phone,
                    imageUri = imageUriString
                )
                val result = profileRepository.updateProfile(updatedProfile)
                runOnUiThread {
                    if (result > 0) {
                        Toast.makeText(this@EditProfileActivity, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                        // Go back to ProfileActivity
                        val intent = Intent(this@EditProfileActivity, ProfileActivity::class.java)
                        intent.putExtra("user_id", userId)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@EditProfileActivity, "Terjadi kesalahan saat menyimpan.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Handle the result of image picking (onActivityResult)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            val profileImage = findViewById<ImageView>(R.id.profile_image)

            // Load selected image using Glide
            Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.ic_profile_placeholder)
                .into(profileImage)
        }
    }
}
