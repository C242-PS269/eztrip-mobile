package com.project.capstone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.project.capstone.database.expenses.ExpenseDao
import com.project.capstone.database.expenses.Expenses
import com.project.capstone.database.profile.Profile
import com.project.capstone.database.profile.ProfileDao
import com.project.capstone.database.user.User
import com.project.capstone.database.user.UserDao

@Database(entities = [User::class, Expenses::class, Profile::class], version = 3) // Updated version
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Migration from version 1 to 2
        val MIGRATION_1_3 = object : Migration(1, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the userId column to the expenses table
                database.execSQL("ALTER TABLE expenses ADD COLUMN userId INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "capstone-database"
                )
                    .addMigrations(MIGRATION_1_3)  // Apply migration here
                    .fallbackToDestructiveMigration() // Add this line to clear the database on schema changes (for testing)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
