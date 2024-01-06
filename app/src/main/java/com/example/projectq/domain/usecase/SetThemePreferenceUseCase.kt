package com.example.projectq.domain.usecase

import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.domain.repository.PreferencesRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class SetThemePreferenceUseCaseParam(val themeMode: Int)
class SetThemePreferenceUseCase @Inject constructor(
    private val repository: PreferencesRepository,
    private val dispatcher: DispatcherProvider
) {
    suspend fun execute(param: SetThemePreferenceUseCaseParam) {
        withContext(dispatcher.io) {
            repository.setThemePreference(param.themeMode)
        }
    }
}