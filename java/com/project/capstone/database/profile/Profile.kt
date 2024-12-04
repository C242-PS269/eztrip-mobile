package com.project.capstone.database.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val profileId: Int = 0,
    val userId: Int,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val imageUri: String = ""
)
