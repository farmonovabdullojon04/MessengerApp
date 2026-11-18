package com.abdullojon.messengerapp.data.repository

import com.abdullojon.messengerapp.data.source.local.prefs.LocalStorage
import com.abdullojon.messengerapp.data.source.remote.api.AuthApi
import com.abdullojon.messengerapp.data.source.remote.request.PhoneRequestDto
import com.abdullojon.messengerapp.data.source.remote.request.RefreshTokenRequestDto
import com.abdullojon.messengerapp.data.source.remote.request.VerifyOtpRequestDto
import com.abdullojon.messengerapp.data.source.remote.response.AuthResponseDto
import com.abdullojon.messengerapp.data.source.remote.response.ErrorResponseDto
import com.abdullojon.messengerapp.domain.repository.AuthRepository
import com.google.gson.Gson
import javax.inject.Inject

class TelegramNotLinkedException(val botUrl: String?, message: String) : Exception(message)

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val localStorage: LocalStorage,
    private val gson: Gson
) : AuthRepository {

    override suspend fun requestOtp(phone: String): Result<Unit> {
        return runCatching {
            val response = authApi.requestOtp(PhoneRequestDto(phone = phone))
            if (response.isSuccessful) {
                Unit
            } else {
                val errorBodyString = response.errorBody()?.string()
                val errorDto = errorBodyString?.let {
                    runCatching { gson.fromJson(it, ErrorResponseDto::class.java) }.getOrNull()
                }
                when (response.code()) {
                    409 -> throw TelegramNotLinkedException(
                        botUrl = errorDto?.botUrl,
                        message = errorDto?.message ?: "Telegram account not linked."
                    )
                    429 -> throw Exception("Rate limit exceeded. Please try again later.")
                    503 -> throw Exception("OTP delivery unavailable. Please try again later.")
                    else -> throw Exception(errorDto?.message ?: "Failed to send OTP (${response.code()})")
                }
            }
        }
    }

    override suspend fun verifyOtp(phone: String, code: String): Result<AuthResponseDto> {
        return runCatching {
            val response = authApi.verifyOtp(VerifyOtpRequestDto(phone = phone, code = code))
            if (response.isSuccessful) {
                val authResponse = response.body() ?: throw Exception("Empty response body")
                localStorage.accessToken = authResponse.accessToken
                localStorage.refreshToken = authResponse.refreshToken
                localStorage.userId = authResponse.userId
                localStorage.deviceId = authResponse.deviceId
                authResponse
            } else {
                val errorBodyString = response.errorBody()?.string()
                val errorDto = errorBodyString?.let {
                    runCatching { gson.fromJson(it, ErrorResponseDto::class.java) }.getOrNull()
                }
                throw Exception(errorDto?.message ?: "Verification failed (${response.code()})")
            }
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthResponseDto> {
        return runCatching {
            val response = authApi.refreshToken(RefreshTokenRequestDto(refreshToken = refreshToken))
            if (response.isSuccessful) {
                val authResponse = response.body() ?: throw Exception("Response body is empty")

                authResponse.accessToken?.let { localStorage.accessToken = it }
                authResponse.refreshToken?.let { localStorage.refreshToken = it }

                authResponse
            } else {
                val errorBodyString = response.errorBody()?.string()
                val errorDto = errorBodyString?.let {
                    runCatching { gson.fromJson(it, ErrorResponseDto::class.java) }.getOrNull()
                }

                if (response.code() == 401) {
                    localStorage.clear()
                }

                throw Exception(errorDto?.message ?: "Token refresh failed (${response.code()})")
            }
        }
    }

    override suspend fun logout(): Result<Unit> {
        return runCatching {
            val response=authApi.logout()
            localStorage.clear()
            if (response.isSuccessful || response.code()==401){
                Unit
            }else{
                val errorBodyString=response.errorBody()?.string()
                val errorDto=errorBodyString?.let{
                    runCatching { gson.fromJson(it, ErrorResponseDto::class.java) }.getOrNull()
                }
                throw Exception(errorDto?.message?:"Logout failed (${response.code()})")
            }
        }
    }
}
