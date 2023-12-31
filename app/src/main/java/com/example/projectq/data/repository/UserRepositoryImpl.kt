package com.example.projectq.data.repository

import com.example.projectq.data.local.datasource.UserLocalDataSource
import com.example.projectq.data.remote.datasource.UserRemoteDataSource
import com.example.projectq.data.util.Resource
import com.example.projectq.domain.model.UserDetailDomainModel
import com.example.projectq.domain.model.UserHomeDomainModel
import com.example.projectq.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource
) : UserRepository {

    override suspend fun getListUser(username: String): Resource<List<UserHomeDomainModel>> {
        return remoteDataSource.getListUser(username)
    }

    override suspend fun getUserDetail(username: String): Resource<UserDetailDomainModel> {
        return remoteDataSource.getUserDetail(username)
    }

    override suspend fun getListUserFollowing(username: String): Resource<List<UserHomeDomainModel>> {
        return remoteDataSource.getListUserFollowing(username)
    }

    override suspend fun getListUserFollowers(username: String): Resource<List<UserHomeDomainModel>> {
        return remoteDataSource.getListUserFollowers(username)
    }

    override suspend fun insertFavorite(data: UserHomeDomainModel) {
        localDataSource.insertFavorite(data.toFavoriteEntity())
    }

    override suspend fun deleteFavoriteUserByUsername(username: String) {
        localDataSource.deleteFavoriteUserByUsername(username)
    }

    override fun getFavoriteUsers(): Flow<List<UserHomeDomainModel>> {
        return localDataSource.getFavoriteUsers().map { usersFromDb ->
            usersFromDb.map { userEntity -> userEntity.toDomain() }
        }
    }

    override fun getFavoriteUser(username: String): Flow<UserHomeDomainModel?> {
        return localDataSource.getFavoriteUser(username).map { it?.toDomain() }
    }
}