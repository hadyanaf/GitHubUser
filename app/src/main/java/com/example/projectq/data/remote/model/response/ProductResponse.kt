package com.example.projectq.data.remote.model.response

import com.example.projectq.data.local.model.ProductDomain
import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("brand")
    val brand: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("discountPercentage")
    val discountPercentage: Double? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("images")
    val images: List<String>? = null,
    @SerializedName("price")
    val price: Int? = null,
    @SerializedName("rating")
    val rating: Double? = null,
    @SerializedName("stock")
    val stock: Int? = null,
    @SerializedName("thumbnail")
    val thumbnail: String? = null,
    @SerializedName("title")
    val title: String? = null
) {
    fun toDomain() = ProductDomain(
        brand = brand.orEmpty(),
        category = category.orEmpty(),
        description = description.orEmpty(),
        discountPercentage = discountPercentage ?: 0.0,
        id = id ?: 0,
        images = images ?: listOf(),
        price = price ?: 0,
        rating = rating ?: 0.0,
        stock = stock ?: 0,
        thumbnail = thumbnail.orEmpty(),
        title = title.orEmpty()
    )
}