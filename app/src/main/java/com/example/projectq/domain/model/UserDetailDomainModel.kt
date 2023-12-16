package com.example.projectq.domain.model

data class UserDetailDomainModel(
    val avatarUrl: String,
    val username: String,
    val fullName: String,
    val followersCount: Int,
    val followingCount: Int
)