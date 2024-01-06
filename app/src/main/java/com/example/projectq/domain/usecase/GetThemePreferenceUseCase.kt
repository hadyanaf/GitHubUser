package com.example.projectq.domain.usecase

import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetThemePreferenceUseCase @Inject constructor(
    private val repository: PreferencesRepository,
    private val dispatcher: DispatcherProvider
) {
    fun execute(): Flow<Int> {
        return repository.themePreference.flowOn(dispatcher.io)
    }
}