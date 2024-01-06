package com.example.projectq.data.local.datasource

import com.example.projectq.data.local.entities.FavoriteUser
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    suspend fun insertFavorite(data: FavoriteUser)
    suspend fun deleteFavoriteUserByUsername(username: String)
    fun getFavoriteUsers(): Flow<List<FavoriteUser>>
    fun getFavoriteUser(username: String): Flow<FavoriteUser?>
}