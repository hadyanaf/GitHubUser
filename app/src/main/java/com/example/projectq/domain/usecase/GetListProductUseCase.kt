package com.example.projectq.domain.usecase

import com.example.projectq.data.local.model.ProductStockDomain
import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.data.util.Resource
import com.example.projectq.domain.repository.ProductRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetListProductUseCase @Inject constructor(
    private val repository: ProductRepository,
    private val dispatcher: DispatcherProvider
) {
    suspend fun execute(): Resource<ProductStockDomain> {
        return withContext(dispatcher.io) {
            repository.getListProduct()
        }
    }
}