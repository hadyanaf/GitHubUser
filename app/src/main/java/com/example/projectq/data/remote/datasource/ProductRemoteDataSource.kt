package com.example.projectq.data.remote.datasource

import com.example.projectq.data.local.model.ProductStockDomain
import com.example.projectq.data.util.Resource

interface ProductRemoteDataSource {
    suspend fun getListProduct(): Resource<ProductStockDomain>
}