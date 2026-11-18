package com.abdullojon.messengerapp.data.repository

import com.abdullojon.messengerapp.data.source.local.prefs.LocalStorage
import com.abdullojon.messengerapp.data.source.remote.api.UserApi
import com.abdullojon.messengerapp.data.source.remote.request.UpdateProfileRequestDto
import com.abdullojon.messengerapp.data.source.remote.response.ErrorResponseDto
import com.abdullojon.messengerapp.data.source.remote.response.UserResponseDto
import com.abdullojon.messengerapp.domain.repository.UserRepository
import com.google.gson.Gson
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val localStorage: LocalStorage,
    private val gson: Gson
) : UserRepository {
    override suspend fun getMe(): Result<UserResponseDto> {
        return runCatching {
            val response = userApi.getMe()
            if (response.isSuccessful) {
                val user = response.body() ?: throw Exception("User response is empty")
                user.phone?.let { localStorage.phone = it }
                user.id?.let { localStorage.userId = it }
                user
            } else {
                val errorBodyString = response.errorBody()?.string()
                val errorDto = errorBodyString?.let {
                    runCatching { gson.fromJson(it, ErrorResponseDto::class.java) }.getOrNull()
                }
                throw Exception(errorDto?.message ?: "Failed to get user profile (${response.code()})")
            }
        }
    }

    override suspend fun updateProfile(
        request: UpdateProfileRequestDto
    ): Result<UserResponseDto> {
        return runCatching {
            val response=userApi.updateProfile(request)
            if (response.isSuccessful){
                response.body()?:throw Exception("Response body is empty")
            }else{
                val errorBodyString=response.errorBody()?.string()
                val errorDto=errorBodyString?.let {
                    runCatching { gson.fromJson(it, ErrorResponseDto::class.java) }.getOrNull()
                }
                if (response.code()==409){
                    throw Exception("Ushbu username band.Boshqa username tanlang.")
                }
                throw Exception(errorDto?.message?:"Profilni yangilashda xatolik ?(${response.code()})")
            }
        }
    }

    override suspend fun getUserById(userId: String): Result<UserResponseDto> {
        return runCatching {
            val response=userApi.getUserById(userId)
            if (response.isSuccessful){
                response.body()?:throw Exception("Foydalanuvchi ma'lumotlari bo'sh")
            }else{
                val errorBodyString=response.errorBody()?.string()
                val errorDto=errorBodyString?.let {
                    runCatching { gson.fromJson(it, ErrorResponseDto::class.java) }.getOrNull()
                }
                throw Exception(errorDto?.message?:"Foydalanuvchi topilmadi (${response.code()})")
            }
        }
    }

    override suspend fun searchUsers(
        query: String,
        limit: Int
    ): Result<List<UserResponseDto>> {
        return runCatching {
            val response=userApi.searchUsers(query,limit)
            if (response.isSuccessful){
                response.body()?:emptyList()
            }else{
                val errorBodyString=response.errorBody()?.string()
                val errorDto=errorBodyString?.let {
                    runCatching { gson.fromJson(it, ErrorResponseDto::class.java) }.getOrNull()
                }
                throw Exception(errorDto?.message?:"Qidirishda xatolik(${response.code()})")
            }
        }
    }

}
