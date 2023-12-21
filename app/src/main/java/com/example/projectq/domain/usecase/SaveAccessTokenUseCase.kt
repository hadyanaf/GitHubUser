package com.example.projectq.domain.usecase

import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.domain.repository.PreferencesRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class SaveAccessTokenUseCaseParam(val token: String)
class SaveAccessTokenUseCase @Inject constructor(
    private val repository: PreferencesRepository,
    private val dispatcher: DispatcherProvider
) {
    suspend fun execute(param: SaveAccessTokenUseCaseParam) {
        withContext(dispatcher.io) {
            repository.saveAccessToken(param.token)
        }
    }
}