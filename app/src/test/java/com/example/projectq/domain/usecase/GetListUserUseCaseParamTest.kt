package com.example.projectq.domain.usecase

import com.example.projectq.data.util.DispatcherProvider
import com.example.projectq.data.util.Resource
import com.example.projectq.domain.model.UserHomeDomainModel
import com.example.projectq.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
class GetListUserUseCaseTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var getListUserUseCase: GetListUserUseCase

    @BeforeEach
    fun setUp() {
        getListUserUseCase = GetListUserUseCase(userRepository, dispatcherProvider)
    }

    @Test
    fun `execute should return list of users`() = runBlocking {
        val username = "testUser"
        val mockData = listOf(UserHomeDomainModel(id = 0, avatarUrl = "", username = username))
        val param = GetListUserUseCaseParam(username)
        whenever(userRepository.getListUser(username)).thenReturn(Resource.Success(mockData))
        whenever(dispatcherProvider.io).thenReturn(Dispatchers.Unconfined)

        val result = getListUserUseCase.execute(param)

        assertEquals(mockData, result.data)
    }
}