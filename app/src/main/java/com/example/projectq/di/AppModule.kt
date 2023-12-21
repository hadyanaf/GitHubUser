package com.example.projectq.di

import android.content.Context
import com.example.projectq.data.remote.apiservice.UserApi
import com.example.projectq.data.remote.datasource.UserRemoteDataSource
import com.example.projectq.data.remote.datasource.UserRemoteDataSourceImpl
import com.example.projectq.data.repository.PreferencesRepositoryImpl
import com.example.projectq.data.repository.UserRepositoryImpl
import com.example.projectq.data.util.DataStoreManager
import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.domain.repository.PreferencesRepository
import com.example.projectq.domain.repository.UserRepository
import com.example.projectq.domain.usecase.GetListUserUseCase
import com.example.projectq.domain.usecase.SaveAccessTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(dataStoreManager: DataStoreManager): PreferencesRepository {
        return PreferencesRepositoryImpl(dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideUserRepository(remoteDataSource: UserRemoteDataSource): UserRepository {
        return UserRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(userApi: UserApi): UserRemoteDataSource {
        return UserRemoteDataSourceImpl(userApi)
    }

    @Provides
    @Singleton
    fun provideGetListUserUseCase(
        userRepository: UserRepository,
        dispatcherProvider: DispatcherProvider
    ): GetListUserUseCase {
        return GetListUserUseCase(userRepository, dispatcherProvider)
    }

    @Provides
    @Singleton
    fun provideSaveAccessTokenUseCase(
        preferencesRepository: PreferencesRepository,
        dispatcherProvider: DispatcherProvider
    ): SaveAccessTokenUseCase {
        return SaveAccessTokenUseCase(preferencesRepository, dispatcherProvider)
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