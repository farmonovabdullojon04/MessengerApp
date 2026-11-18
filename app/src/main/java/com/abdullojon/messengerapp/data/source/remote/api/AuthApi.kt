package com.abdullojon.messengerapp.data.source.remote.api

import com.abdullojon.messengerapp.data.source.remote.request.PhoneRequestDto
import com.abdullojon.messengerapp.data.source.remote.request.RefreshTokenRequestDto
import com.abdullojon.messengerapp.data.source.remote.request.VerifyOtpRequestDto
import com.abdullojon.messengerapp.data.source.remote.response.AuthResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("v1/auth/otp/request")
    suspend fun requestOtp(@Body request: PhoneRequestDto): Response<Unit>

    @POST("v1/auth/otp/verify")
    suspend fun verifyOtp(@Body request: VerifyOtpRequestDto): Response<AuthResponseDto>

    @POST("v1/auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequestDto): Response<AuthResponseDto>

    @POST("v1/auth/logout")
    suspend fun logout(): Response<Unit>
}

