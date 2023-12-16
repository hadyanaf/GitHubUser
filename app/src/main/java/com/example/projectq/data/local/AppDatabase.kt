package com.example.projectq.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projectq.data.local.entities.Product
import com.example.projectq.data.local.room.ProductDao

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ProductDao(): ProductDao
}