package com.example.projectq.data.local.datasource

import com.example.projectq.data.local.room.ProductDao
import javax.inject.Inject

class ProductLocalDataSourceImpl @Inject constructor(productDao: ProductDao) :
    ProductLocalDataSource {
}