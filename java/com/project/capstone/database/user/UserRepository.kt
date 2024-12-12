package com.project.capstone.database.user

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    suspend fun checkUser(username: String, password: String): User? = userDao.checkUser(username, password)
    suspend fun getUserByUsername(username: String): User? = userDao.getUserByUsername(username)
}
