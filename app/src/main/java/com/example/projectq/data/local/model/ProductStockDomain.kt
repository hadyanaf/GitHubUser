package com.example.projectq.data.local.model

data class ProductStockDomain(
    val limit: Int,
    val productResponses: List<ProductDomain>,
    val skip: Int,
    val total: Int
)