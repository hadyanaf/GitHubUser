package com.example.projectq.data.repository

import com.example.projectq.data.util.DataStoreManager
import com.example.projectq.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : UserRepository {

    override suspend fun saveAccessToken(token: String) {
        dataStoreManager.saveAccessToken(token)
    }

    override val accessToken: Flow<String?>
        get() = dataStoreManager.accessToken
}