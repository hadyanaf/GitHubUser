package com.example.projectq.data.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_data_store")

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val THEME_KEY = intPreferencesKey("theme_preference")
    }

    suspend fun saveAccessToken(accessToken: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    suspend fun setThemePreference(themeMode: Int) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = themeMode
        }
    }

    val accessToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN_KEY]
    }

    val themePreference: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}