package com.abdullojon.messengerapp.di

import com.abdullojon.messengerapp.data.source.local.prefs.LocalStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val localStorage: LocalStorage
): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder=chain.request().newBuilder()
        val token=localStorage.accessToken
        if (!token.isNullOrEmpty()){
            requestBuilder.addHeader("Authorization","Bearer $token")
        }
        return chain.proceed(requestBuilder.build())
    }
}