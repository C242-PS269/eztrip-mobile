package com.project.capstone.database.profile

class ProfileRepository(private val profileDao: ProfileDao) {
    suspend fun insertProfile(profile: Profile): Long = profileDao.insertProfile(profile)
    suspend fun getProfileByUserId(userId: Int): Profile? = profileDao.getProfileByUserId(userId)
    suspend fun updateProfile(profile: Profile): Int = profileDao.updateProfile(profile)
}
