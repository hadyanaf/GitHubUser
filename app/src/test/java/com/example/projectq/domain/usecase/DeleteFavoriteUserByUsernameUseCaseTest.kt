package com.example.projectq.domain.usecase

import com.example.projectq.data.util.DispatcherProvider
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
class DeleteFavoriteUserByUsernameUseCaseTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var deleteFavoriteUserByUsernameUseCase: DeleteFavoriteUserByUsernameUseCase

    @BeforeEach
    fun setUp() {
        whenever(dispatcherProvider.io).thenReturn(Dispatchers.Unconfined)
        deleteFavoriteUserByUsernameUseCase =
            DeleteFavoriteUserByUsernameUseCase(userRepository, dispatcherProvider)
    }

    @Test
    fun `execute calls repository deleteFavorite method`() = runBlocking {
        val param = DeleteFavoriteUserByUsernameUseCaseParam(username = "")
        deleteFavoriteUserByUsernameUseCase.execute(param)

        verify(userRepository).deleteFavoriteUserByUsername(param.username)
    }
}