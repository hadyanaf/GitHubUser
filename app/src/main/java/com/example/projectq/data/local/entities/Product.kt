package com.example.projectq.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    var brand: String? = null,
    var category: String? = null,
    var description: String? = null,
    var discountPercentage: Double? = null,
    @PrimaryKey(autoGenerate = false)
    var id: Int? = null,
    var images: List<String>? = null,
    var price: Int? = null,
    var rating: Double? = null,
    var stock: Int? = null,
    var thumbnail: String? = null,
    var title: String? = null
)
