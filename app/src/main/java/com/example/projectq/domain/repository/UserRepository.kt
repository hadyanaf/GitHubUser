package com.example.projectq.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveAccessToken(token: String)
    val accessToken: Flow<String?>
}