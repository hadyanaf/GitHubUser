package com.example.projectq.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.projectq.data.local.entities.Product

@Dao
interface ProductDao {

    @Insert
    fun insert(product: Product)

    @Delete
    fun delete(product: Product)

    @Query("SELECT * FROM product")
    fun getListProduct(): List<Product>
}