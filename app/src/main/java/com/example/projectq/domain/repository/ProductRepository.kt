package com.example.projectq.domain.repository

import com.example.projectq.data.local.model.ProductStockDomain
import com.example.projectq.data.util.Resource

interface ProductRepository {
    suspend fun getListProduct(): Resource<ProductStockDomain>
}