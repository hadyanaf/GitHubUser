package com.example.projectq.data.util

import com.example.projectq.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val preferencesRepository: PreferencesRepository) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { preferencesRepository.accessToken.firstOrNull() }

        val requestBuilder = chain.request().newBuilder()
        accessToken?.let {
            requestBuilder.addHeader("Authorization", it)
        }

        return chain.proceed(requestBuilder.build())
    }
}