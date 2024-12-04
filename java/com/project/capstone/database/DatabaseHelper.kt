//package com.project.capstone.database
//
//import android.content.ContentValues
//import android.content.Context
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//
//class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//
//    companion object {
//        private const val DATABASE_NAME = "UserDatabase.db"
//        private const val DATABASE_VERSION = 6 // Increment version for schema changes
//
//        // Tabel users
//        const val TABLE_USERS = "users"
//        const val COLUMN_ID = "id"
//        const val COLUMN_USERNAME = "username"
//        const val COLUMN_PASSWORD = "password"
//
//        // Tabel profile
//        const val TABLE_PROFILE = "profile"
//        const val COLUMN_PROFILE_ID = "profile_id"
//        const val COLUMN_PROFILE_USER_ID = "user_id"
//        const val COLUMN_NAME = "name"
//        const val COLUMN_EMAIL = "email"
//        const val COLUMN_PHONE = "phone"
//        const val COLUMN_IMAGE_URI = "image_uri"
//    }
//
//    override fun onCreate(db: SQLiteDatabase?) {
//        // Tabel Users
//        val createUsersTable = """
//            CREATE TABLE $TABLE_USERS (
//                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
//                $COLUMN_USERNAME TEXT UNIQUE NOT NULL,
//                $COLUMN_PASSWORD TEXT NOT NULL
//            )
//        """.trimIndent()
//
//        // Tabel Profile
//        val createProfileTable = """
//            CREATE TABLE $TABLE_PROFILE (
//                $COLUMN_PROFILE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
//                $COLUMN_PROFILE_USER_ID INTEGER NOT NULL,
//                $COLUMN_NAME TEXT,
//                $COLUMN_EMAIL TEXT,
//                $COLUMN_PHONE TEXT,
//                $COLUMN_IMAGE_URI TEXT,
//                FOREIGN KEY($COLUMN_PROFILE_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID) ON DELETE CASCADE
//            )
//        """.trimIndent()
//
//        db?.execSQL(createUsersTable)
//        db?.execSQL(createProfileTable)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        if (oldVersion < 6) {
//            db?.execSQL("DROP TABLE IF EXISTS $TABLE_PROFILE")
//            db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
//            onCreate(db)
//        }
//    }
//
//    // Fungsi untuk memasukkan data user ke tabel users
//    fun insertUser(username: String, password: String): Long {
//        val db = this.writableDatabase
//        val values = ContentValues().apply {
//            put(COLUMN_USERNAME, username)
//            put(COLUMN_PASSWORD, password)
//        }
//        return db.insert(TABLE_USERS, null, values)
//    }
//
//    // Fungsi untuk memasukkan data profil ke tabel profile
//    fun insertProfile(userId: Long, name: String, email: String, phone: String, imageUri: String): Long {
//        val db = this.writableDatabase
//        val values = ContentValues().apply {
//            put(COLUMN_PROFILE_USER_ID, userId)
//            put(COLUMN_NAME, name)
//            put(COLUMN_EMAIL, email)
//            put(COLUMN_PHONE, phone)
//            put(COLUMN_IMAGE_URI, imageUri)
//        }
//        return db.insert(TABLE_PROFILE, null, values)
//    }
//
//    // Fungsi untuk memeriksa kredensial user
//    fun checkUser(username: String, password: String): Int? {
//        val db = this.readableDatabase
//        val query = "SELECT $COLUMN_ID FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
//        val cursor = db.rawQuery(query, arrayOf(username, password))
//
//        val userId = if (cursor.moveToFirst()) cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)) else null
//        cursor.close()
//        return userId
//    }
//
//    // Fungsi untuk mendapatkan user ID berdasarkan username
//    fun getUserId(username: String): Int? {
//        val db = this.readableDatabase
//        val query = "SELECT $COLUMN_ID FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
//        val cursor = db.rawQuery(query, arrayOf(username))
//        val userId = if (cursor.moveToFirst()) cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)) else null
//        cursor.close()
//        return userId
//    }
//
//    // Fungsi untuk mendapatkan data profil berdasarkan user_id
//    fun getProfile(userId: Int): Profile? {
//        val db = this.readableDatabase
//        val query = "SELECT * FROM $TABLE_PROFILE WHERE $COLUMN_PROFILE_USER_ID = ?"
//        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
//
//        if (cursor.moveToFirst()) {
//            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
//            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
//            val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
//            val imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI))
//            cursor.close()
//            return Profile(userId, name, email, phone, imageUri)
//        }
//        cursor.close()
//        return null
//    }
//
//    // Fungsi untuk memperbarui data profil
//    fun updateProfile(userId: Int, name: String, email: String, phone: String, imageUri: String): Int {
//        val db = this.writableDatabase
//        val values = ContentValues().apply {
//            put(COLUMN_NAME, name)
//            put(COLUMN_EMAIL, email)
//            put(COLUMN_PHONE, phone)
//            put(COLUMN_IMAGE_URI, imageUri)
//        }
//        return db.update(TABLE_PROFILE, values, "$COLUMN_PROFILE_USER_ID = ?", arrayOf(userId.toString()))
//    }
//}
//
//// Data class untuk Profile
//data class Profile(
//    val userId: Int,
//    val name: String,
//    val email: String,
//    val phone: String,
//    val imageUri: String
//)
