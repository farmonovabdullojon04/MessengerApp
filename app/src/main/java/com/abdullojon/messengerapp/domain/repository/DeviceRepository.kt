package com.abdullojon.messengerapp.domain.repository

interface DeviceRepository {
    suspend fun updateFcmToken(token: String): Result<Unit>
}