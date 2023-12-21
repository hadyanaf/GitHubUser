package com.example.projectq.domain.repository

import com.example.projectq.data.util.Resource
import com.example.projectq.domain.model.UserDetailDomainModel
import com.example.projectq.domain.model.UserHomeDomainModel

interface UserRepository {
    suspend fun getListUser(username: String): Resource<List<UserHomeDomainModel>>
    suspend fun getUserDetail(username: String): Resource<UserDetailDomainModel>
    suspend fun getListUserFollowing(username: String): Resource<List<UserHomeDomainModel>>
    suspend fun getListUserFollowers(username: String): Resource<List<UserHomeDomainModel>>
}