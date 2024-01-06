package com.example.projectq.domain.usecase

import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.domain.model.UserHomeDomainModel
import com.example.projectq.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetListFavoriteUserUseCase @Inject constructor(
    private val repository: UserRepository,
    private val dispatcher: DispatcherProvider
) {
    fun execute(): Flow<List<UserHomeDomainModel>> {
        return repository.getFavoriteUsers().flowOn(dispatcher.io)
    }
}