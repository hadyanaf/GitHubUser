package com.example.projectq.di

import android.content.Context
import com.example.projectq.data.local.datasource.UserLocalDataSource
import com.example.projectq.data.local.datasource.UserLocalDataSourceImpl
import com.example.projectq.data.local.room.UserDao
import com.example.projectq.data.remote.apiservice.UserApi
import com.example.projectq.data.remote.datasource.UserRemoteDataSource
import com.example.projectq.data.remote.datasource.UserRemoteDataSourceImpl
import com.example.projectq.data.repository.PreferencesRepositoryImpl
import com.example.projectq.data.repository.UserRepositoryImpl
import com.example.projectq.data.util.DataStoreManager
import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.domain.repository.PreferencesRepository
import com.example.projectq.domain.repository.UserRepository
import com.example.projectq.domain.usecase.DeleteFavoriteUserByUsernameUseCase
import com.example.projectq.domain.usecase.GetFavoriteUserUseCase
import com.example.projectq.domain.usecase.GetListFavoriteUserUseCase
import com.example.projectq.domain.usecase.GetListUserFollowersUseCase
import com.example.projectq.domain.usecase.GetListUserFollowingUseCase
import com.example.projectq.domain.usecase.GetListUserUseCase
import com.example.projectq.domain.usecase.GetThemePreferenceUseCase
import com.example.projectq.domain.usecase.GetUserDetailUseCase
import com.example.projectq.domain.usecase.InsertFavoriteUserUseCase
import com.example.projectq.domain.usecase.SaveAccessTokenUseCase
import com.example.projectq.domain.usecase.SetThemePreferenceUseCase
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
    fun provideUserRepository(
        remoteDataSource: UserRemoteDataSource,
        localDataSource: UserLocalDataSource
    ): UserRepository {
        return UserRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(userApi: UserApi): UserRemoteDataSource {
        return UserRemoteDataSourceImpl(userApi)
    }

    @Provides
    @Singleton
    fun provideUserLocalDataSource(userDao: UserDao): UserLocalDataSource {
        return UserLocalDataSourceImpl(userDao)
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
    fun provideGetUserDetailUseCase(
        userRepository: UserRepository,
        dispatcherProvider: DispatcherProvider
    ): GetUserDetailUseCase {
        return GetUserDetailUseCase(userRepository, dispatcherProvider)
    }

    @Provides
    @Singleton
    fun provideGetListUserFollowingUseCase(
        userRepository: UserRepository,
        dispatcherProvider: DispatcherProvider
    ): GetListUserFollowingUseCase {
        return GetListUserFollowingUseCase(userRepository, dispatcherProvider)
    }

    @Provides
    @Singleton
    fun provideGetListUserFollowersUseCase(
        userRepository: UserRepository,
        dispatcherProvider: DispatcherProvider
    ): GetListUserFollowersUseCase {
        return GetListUserFollowersUseCase(userRepository, dispatcherProvider)
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
    fun provideInsertFavoriteUserUseCase(
        userRepository: UserRepository,
        dispatcherProvider: DispatcherProvider
    ): InsertFavoriteUserUseCase {
        return InsertFavoriteUserUseCase(userRepository, dispatcherProvider)
    }

    @Provides
    @Singleton
    fun provideDeleteFavoriteUserUseCase(
        userRepository: UserRepository,
        dispatcherProvider: DispatcherProvider
    ): DeleteFavoriteUserByUsernameUseCase {
        return DeleteFavoriteUserByUsernameUseCase(userRepository, dispatcherProvider)
    }

    @Provides
    @Singleton
    fun provideGetListFavoriteUserUseCase(
        userRepository: UserRepository,
        dispatcherProvider: DispatcherProvider
    ): GetListFavoriteUserUseCase {
        return GetListFavoriteUserUseCase(userRepository, dispatcherProvider)
    }

    @Provides
    @Singleton
    fun provideGetFavoriteUserUseCase(
        userRepository: UserRepository,
        dispatcherProvider: DispatcherProvider
    ): GetFavoriteUserUseCase {
        return GetFavoriteUserUseCase(userRepository, dispatcherProvider)
    }

    @Provides
    @Singleton
    fun provideSetThemePreferenceUseCase(
        preferencesRepository: PreferencesRepository,
        dispatcherProvider: DispatcherProvider
    ): SetThemePreferenceUseCase {
        return SetThemePreferenceUseCase(preferencesRepository, dispatcherProvider)
    }

    @Provides
    @Singleton
    fun provideGetThemePreferenceUseCase(
        preferencesRepository: PreferencesRepository,
        dispatcherProvider: DispatcherProvider
    ): GetThemePreferenceUseCase {
        return GetThemePreferenceUseCase(preferencesRepository, dispatcherProvider)
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