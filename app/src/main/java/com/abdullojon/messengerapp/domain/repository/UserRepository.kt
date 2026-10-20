package com.abdullojon.messengerapp.domain.repository

import com.abdullojon.messengerapp.data.source.remote.request.UpdateProfileRequestDto
import com.abdullojon.messengerapp.data.source.remote.response.UserResponseDto

interface UserRepository {
    suspend fun getMe(): Result<UserResponseDto>
    suspend fun updateProfile(request: UpdateProfileRequestDto): Result<UserResponseDto>
}