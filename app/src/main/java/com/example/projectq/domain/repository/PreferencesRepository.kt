package com.example.projectq.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun saveAccessToken(token: String)
    suspend fun setThemePreference(themeMode: Int)
    val accessToken: Flow<String?>
    val themePreference: Flow<Int>
}