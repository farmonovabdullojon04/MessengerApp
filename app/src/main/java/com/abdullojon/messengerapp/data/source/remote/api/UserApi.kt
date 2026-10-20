package com.abdullojon.messengerapp.data.source.remote.api

import com.abdullojon.messengerapp.data.source.remote.request.UpdateProfileRequestDto
import com.abdullojon.messengerapp.data.source.remote.response.UserResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApi {
    @GET("v1/users/me")
    suspend fun getMe(): Response<UserResponseDto>

    @PATCH("v1/users/me")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequestDto
    ): Response<UserResponseDto>
}