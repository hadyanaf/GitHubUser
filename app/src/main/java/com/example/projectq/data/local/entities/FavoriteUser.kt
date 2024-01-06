package com.example.projectq.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.projectq.domain.model.UserHomeDomainModel

@Entity
data class FavoriteUser(
    var avatarUrl: String? = null,
    @PrimaryKey(autoGenerate = false)
    var id: Int? = null,
    var username: String? = null
) {
    fun toDomain() = UserHomeDomainModel(
        id = id ?: 0,
        avatarUrl = avatarUrl.orEmpty(),
        username = username.orEmpty()
    )
}
