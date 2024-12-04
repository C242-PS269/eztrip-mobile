package com.project.capstone.database.profile

import androidx.room.*

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProfile(profile: Profile): Long

    @Query("SELECT * FROM profile WHERE userId = :userId LIMIT 1")
    suspend fun getProfileByUserId(userId: Int): Profile?

    @Update
    suspend fun updateProfile(profile: Profile): Int
}
