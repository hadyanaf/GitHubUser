package com.example.projectq.domain.usecase

import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.data.util.Resource
import com.example.projectq.domain.model.UserDetailDomainModel
import com.example.projectq.domain.repository.UserRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class GetUserDetailUseCaseParam(val username: String)
class GetUserDetailUseCase @Inject constructor(
    private val repository: UserRepository,
    private val dispatcher: DispatcherProvider
) {
    suspend fun execute(param: GetUserDetailUseCaseParam): Resource<UserDetailDomainModel> {
        return withContext(dispatcher.io) {
            repository.getUserDetail(param.username)
        }
    }
}