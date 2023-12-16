package com.example.projectq.data.remote.apiservice

import com.example.projectq.data.remote.model.response.ProductStockResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductApi {

    companion object {
        const val GET_PRODUCTS = "/products"
    }

    @GET(GET_PRODUCTS)
    suspend fun getListProduct() : Response<ProductStockResponse>
}