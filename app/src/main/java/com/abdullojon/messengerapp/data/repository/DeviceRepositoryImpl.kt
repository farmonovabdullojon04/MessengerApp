package com.abdullojon.messengerapp.data.repository

import com.abdullojon.messengerapp.data.source.remote.api.DeviceApi
import com.abdullojon.messengerapp.data.source.remote.request.FcmTokenRequestDto
import com.abdullojon.messengerapp.data.source.remote.response.ErrorResponseDto
import com.abdullojon.messengerapp.domain.repository.DeviceRepository
import com.google.gson.Gson
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val deviceApi: DeviceApi,
    private val gson: Gson
) : DeviceRepository{
    override suspend fun updateFcmToken(token: String): Result<Unit> {
        return runCatching {
            val response=deviceApi.updateFcmToken(FcmTokenRequestDto(token=token))
            if (!response.isSuccessful){
                val errorBodyString=response.errorBody()?.string()
                val errorDto=errorBodyString?.let {
                    runCatching { gson.fromJson(it, ErrorResponseDto::class.java)}.getOrNull()
                }
                throw Exception(errorDto?.message?:"FCM Token yangilashda xatolik(${response.code()})")
            }
        }
    }
}