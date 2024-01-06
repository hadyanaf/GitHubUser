package com.example.projectq.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.projectq.data.local.entities.FavoriteUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(data: FavoriteUser)

    @Query("DELETE FROM FavoriteUser WHERE username = :username")
    suspend fun deleteFavoriteUserByUsername(username: String)

    @Query("SELECT * FROM FavoriteUser")
    fun getFavoriteUsers(): Flow<List<FavoriteUser>>

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getFavoriteUser(username: String): Flow<FavoriteUser?>
}