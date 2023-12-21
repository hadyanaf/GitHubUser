package com.example.projectq.domain.usecase

import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.data.util.Resource
import com.example.projectq.domain.model.UserHomeDomainModel
import com.example.projectq.domain.repository.UserRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class GetListUserFollowersUseCaseParam(val username: String)
class GetListUserFollowersUseCase @Inject constructor(
    private val repository: UserRepository,
    private val dispatcher: DispatcherProvider
) {
    suspend fun execute(param: GetListUserFollowersUseCaseParam): Resource<List<UserHomeDomainModel>> {
        return withContext(dispatcher.io) {
            repository.getListUserFollowers(param.username)
        }
    }
}