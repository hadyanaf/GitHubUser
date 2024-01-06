package com.example.projectq.domain.usecase

import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.domain.model.UserHomeDomainModel
import com.example.projectq.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
class InsertFavoriteUserUseCaseTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var insertFavoriteUserUseCase: InsertFavoriteUserUseCase

    @BeforeEach
    fun setUp() {
        whenever(dispatcherProvider.io).thenReturn(Dispatchers.Unconfined)
        insertFavoriteUserUseCase = InsertFavoriteUserUseCase(userRepository, dispatcherProvider)
    }

    @Test
    fun `execute calls repository insertFavorite method`() = runBlocking {
        val param = InsertFavoriteUserUseCaseParam(
            UserHomeDomainModel(
                id = 0,
                avatarUrl = "",
                username = ""
            )
        )
        insertFavoriteUserUseCase.execute(param)

        verify(userRepository).insertFavorite(param.data)
    }
}