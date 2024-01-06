package com.example.projectq.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projectq.data.local.entities.FavoriteUser
import com.example.projectq.data.local.room.UserDao

@Database(
    entities = [FavoriteUser::class],
    version = 1,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}