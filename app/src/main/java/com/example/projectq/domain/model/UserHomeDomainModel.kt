package com.example.projectq.domain.model

import com.example.projectq.data.local.entities.FavoriteUser

data class UserHomeDomainModel(
    val id: Int,
    val avatarUrl: String,
    val username: String
) {
    fun toFavoriteEntity() = FavoriteUser(
        id = id,
        avatarUrl = avatarUrl,
        username = username
    )
}