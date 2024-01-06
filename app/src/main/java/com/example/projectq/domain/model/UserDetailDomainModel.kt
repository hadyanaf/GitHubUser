package com.example.projectq.domain.model

data class UserDetailDomainModel(
    val id: Int? = null,
    val avatarUrl: String? = null,
    val username: String? = null,
    val fullName: String? = null,
    val followersCount: Int? = null,
    val followingCount: Int? = null
)