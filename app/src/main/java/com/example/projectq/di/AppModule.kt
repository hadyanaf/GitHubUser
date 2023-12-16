package com.example.projectq.di

import android.content.Context
import com.example.projectq.data.remote.apiservice.ProductApi
import com.example.projectq.data.remote.datasource.ProductRemoteDataSource
import com.example.projectq.data.remote.datasource.ProductRemoteDataSourceImpl
import com.example.projectq.data.repository.ProductRepositoryImpl
import com.example.projectq.data.repository.UserRepositoryImpl
import com.example.projectq.data.util.DataStoreManager
import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.domain.repository.ProductRepository
import com.example.projectq.domain.repository.UserRepository
import com.example.projectq.domain.usecase.GetListProductUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideProductApi(): ProductApi {
        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProductRemoteDataSource(productApi: ProductApi): ProductRemoteDataSource {
        return ProductRemoteDataSourceImpl(productApi)
    }

    @Provides
    @Singleton
    fun provideProductRepository(productRemoteDataSource: ProductRemoteDataSource): ProductRepository {
        return ProductRepositoryImpl(productRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideGetListProductUseCase(
        productRepository: ProductRepository,
        dispatcherProvider: DispatcherProvider
    ): GetListProductUseCase {
        return GetListProductUseCase(productRepository, dispatcherProvider)
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    @Provides
    @Singleton
    fun provideUserRepository(dataStoreManager: DataStoreManager): UserRepository {
        return UserRepositoryImpl(dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main

        override val io: CoroutineDispatcher
            get() = Dispatchers.IO

        override val default: CoroutineDispatcher
            get() = Dispatchers.Default

        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}