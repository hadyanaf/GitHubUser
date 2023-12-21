package com.example.projectq.data.remote.datasource

import com.example.projectq.data.util.Resource
import com.example.projectq.domain.model.UserDetailDomainModel
import com.example.projectq.domain.model.UserHomeDomainModel

interface UserRemoteDataSource {
    suspend fun getListUser(username: String): Resource<List<UserHomeDomainModel>>
    suspend fun getUserDetail(username: String): Resource<UserDetailDomainModel>
    suspend fun getUserFollowing(username: String): Resource<List<UserHomeDomainModel>>
    suspend fun getUserFollowers(username: String): Resource<List<UserHomeDomainModel>>
}