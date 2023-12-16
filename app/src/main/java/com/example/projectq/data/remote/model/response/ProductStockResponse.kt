package com.example.projectq.data.remote.model.response

import com.example.projectq.data.local.model.ProductStockDomain
import com.google.gson.annotations.SerializedName

data class ProductStockResponse(
    @SerializedName("limit")
    val limit: Int? = null,
    @SerializedName("products")
    val productResponses: List<ProductResponse>? = null,
    @SerializedName("skip")
    val skip: Int? = null,
    @SerializedName("total")
    val total: Int? = null
) {
    fun toDomain() = ProductStockDomain(
        limit = limit ?: 0,
        productResponses = productResponses?.map { it.toDomain() } ?: listOf(),
        skip = skip ?: 0,
        total = total ?: 0
    )
}