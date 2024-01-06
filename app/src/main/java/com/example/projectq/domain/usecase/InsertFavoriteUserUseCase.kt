package com.example.projectq.domain.usecase

import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.domain.model.UserHomeDomainModel
import com.example.projectq.domain.repository.UserRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class InsertFavoriteUserUseCaseParam(val data: UserHomeDomainModel)
class InsertFavoriteUserUseCase @Inject constructor(
    private val repository: UserRepository,
    private val dispatcher: DispatcherProvider
) {
    suspend fun execute(param: InsertFavoriteUserUseCaseParam) {
        withContext(dispatcher.io) {
            repository.insertFavorite(param.data)
        }
    }
}