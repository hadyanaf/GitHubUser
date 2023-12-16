package com.example.projectq.data.remote.datasource

import com.example.projectq.data.local.model.ProductStockDomain
import com.example.projectq.data.remote.apiservice.ProductApi
import com.example.projectq.data.util.Resource
import javax.inject.Inject

class ProductRemoteDataSourceImpl @Inject constructor(private val productApi: ProductApi) :
    ProductRemoteDataSource {

    override suspend fun getListProduct(): Resource<ProductStockDomain> {
        val response = productApi.getListProduct()
        return if (response.isSuccessful && response.body() != null) {
            Resource.Success(data = response.body()!!.toDomain())
        } else {
            Resource.Error(message = response.message())
        }
    }
}