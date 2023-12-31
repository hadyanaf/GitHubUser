package com.example.projectq.data.remote.datasource

import com.example.projectq.data.remote.apiservice.UserApi
import com.example.projectq.data.util.Resource
import com.example.projectq.domain.model.UserDetailDomainModel
import com.example.projectq.domain.model.UserHomeDomainModel
import timber.log.Timber
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(private val userApi: UserApi) :
    UserRemoteDataSource {

    override suspend fun getListUser(username: String): Resource<List<UserHomeDomainModel>> {
        val response = userApi.getListUser(username)
        return if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                responseBody.detailUserResponses?.let { Resource.Success(data = it.map { data -> data.toUserHomeDomainModel() }) }
            } ?: Resource.Error(message = "Data is empty")
        } else {
            Resource.Error(message = response.message())
        }
    }

    override suspend fun getUserDetail(username: String): Resource<UserDetailDomainModel> {
        val response = userApi.getUserDetail(username)
        return if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                Resource.Success(data = responseBody.toUserDetailDomainModel())
            } ?: Resource.Error(message = "Data is empty")
        } else {
            Resource.Error(message = response.message())
        }
    }

    override suspend fun getListUserFollowing(username: String): Resource<List<UserHomeDomainModel>> {
        val response = userApi.getListUserFollowing(username)
        return if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                Resource.Success(data = responseBody.map { it.toUserHomeDomainModel() })
            } ?: Resource.Error(message = "Data is empty")
        } else {
            Resource.Error(message = response.message())
        }
    }

    override suspend fun getListUserFollowers(username: String): Resource<List<UserHomeDomainModel>> {
        val response = userApi.getListUserFollowers(username)
        return if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                Resource.Success(data = responseBody.map { it.toUserHomeDomainModel() })
            } ?: Resource.Error(message = "Data is empty")
        } else {
            Resource.Error(message = response.message())
        }
    }
}