package com.example.projectq.data.local.datasource

import com.example.projectq.data.local.entities.FavoriteUser
import com.example.projectq.data.local.room.UserDao
import kotlinx.coroutines.flow.Flow

class UserLocalDataSourceImpl(private val userDao: UserDao) : UserLocalDataSource {
    override suspend fun insertFavorite(data: FavoriteUser) {
        userDao.insertFavorite(data)
    }

    override suspend fun deleteFavoriteUserByUsername(username: String) {
        userDao.deleteFavoriteUserByUsername(username)
    }

    override fun getFavoriteUsers(): Flow<List<FavoriteUser>> {
        return userDao.getFavoriteUsers()
    }

    override fun getFavoriteUser(username: String): Flow<FavoriteUser?> {
        return userDao.getFavoriteUser(username)
    }
}