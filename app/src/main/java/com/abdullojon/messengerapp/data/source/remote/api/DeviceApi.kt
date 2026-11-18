package com.abdullojon.messengerapp.data.source.remote.api

import com.abdullojon.messengerapp.data.source.remote.request.FcmTokenRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface DeviceApi {
    @PUT("v1/devices/me/fcm-token")
    suspend fun updateFcmToken(
        @Body request: FcmTokenRequestDto
    ): Response<Unit>
}