package com.project.capstone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.capstone.database.profile.Profile
import com.project.capstone.database.profile.ProfileDao
import com.project.capstone.database.user.User
import com.project.capstone.database.user.UserDao

@Database(entities = [User::class, Profile::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "capstone_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
