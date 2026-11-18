package com.abdullojon.messengerapp.domain.repository

import com.abdullojon.messengerapp.data.source.remote.response.AuthResponseDto

interface AuthRepository {
    suspend fun requestOtp(phone: String): Result<Unit>
    suspend fun verifyOtp(phone: String, code: String): Result<AuthResponseDto>
    suspend fun refreshToken(refreshToken: String): Result<AuthResponseDto>
    suspend fun logout(): Result<Unit>
}
