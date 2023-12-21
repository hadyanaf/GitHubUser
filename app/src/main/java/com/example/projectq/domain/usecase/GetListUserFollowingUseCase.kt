package com.example.projectq.domain.usecase

import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.data.util.Resource
import com.example.projectq.domain.model.UserHomeDomainModel
import com.example.projectq.domain.repository.UserRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class GetListUserFollowingUseCaseParam(val username: String)
class GetListUserFollowingUseCase @Inject constructor(
    private val repository: UserRepository,
    private val dispatcher: DispatcherProvider
) {
    suspend fun execute(param: GetListUserFollowingUseCaseParam): Resource<List<UserHomeDomainModel>> {
        return withContext(dispatcher.io) {
            repository.getListUserFollowing(param.username)
        }
    }
}