package com.abdullojon.messengerapp.data.source.remote.api

import com.abdullojon.messengerapp.data.source.remote.request.UpdateProfileRequestDto
import com.abdullojon.messengerapp.data.source.remote.response.UserResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("v1/users/me")
    suspend fun getMe(): Response<UserResponseDto>

    @PATCH("v1/users/me")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequestDto
    ): Response<UserResponseDto>

    @GET("v1/users/{userId}")
    suspend fun getUserById(
        @Path("userId") userId: String
    ): Response<UserResponseDto>

    @GET("v1/users/search")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("limit") limit:Int=20
    ): Response<List<UserResponseDto>>
}