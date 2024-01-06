package com.example.projectq.data.repository

import com.example.projectq.data.util.DataStoreManager
import com.example.projectq.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : PreferencesRepository {

    override suspend fun saveAccessToken(token: String) {
        dataStoreManager.saveAccessToken(token)
    }

    override suspend fun setThemePreference(themeMode: Int) {
        dataStoreManager.setThemePreference(themeMode)
    }

    override val accessToken: Flow<String?>
        get() = dataStoreManager.accessToken
    override val themePreference: Flow<Int>
        get() = dataStoreManager.themePreference
}