package com.example.projectq.domain.usecase

import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.domain.model.UserHomeDomainModel
import com.example.projectq.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
class GetFavoriteUserUseCaseTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var getFavoriteUserUseCase: GetFavoriteUserUseCase

    @BeforeEach
    fun setUp() {
        getFavoriteUserUseCase = GetFavoriteUserUseCase(userRepository, dispatcherProvider)
    }

    @Test
    fun `execute should return correct data`(): Unit = runBlocking {
        val mockUser = UserHomeDomainModel(id = 0, avatarUrl = "", username = "testUser")
        val param = GetFavoriteUserUseCaseParam(username = "testUser")
        whenever(userRepository.getFavoriteUser(param.username)).thenReturn(flowOf(mockUser))
        whenever(dispatcherProvider.io).thenReturn(Dispatchers.Unconfined)

        val result: Flow<UserHomeDomainModel?> = getFavoriteUserUseCase.execute(param)

        result.collect { data ->
            assert(data == mockUser)
        }
    }
}