package com.example.projectq.data.repository

import com.example.projectq.data.local.model.ProductStockDomain
import com.example.projectq.data.remote.datasource.ProductRemoteDataSource
import com.example.projectq.data.util.Resource
import com.example.projectq.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProductRemoteDataSource
) : ProductRepository {

    override suspend fun getListProduct(): Resource<ProductStockDomain> {
        return remoteDataSource.getListProduct()
    }
}