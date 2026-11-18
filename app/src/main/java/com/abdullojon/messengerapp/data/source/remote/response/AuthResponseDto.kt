package com.abdullojon.messengerapp.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("accessToken")
    val accessToken: String? = null,
    @SerializedName("refreshToken")
    val refreshToken: String? = null,
    @SerializedName("userId")
    val userId: String? = null,
    @SerializedName("deviceId")
    val deviceId: String? = null,
    @SerializedName("isNewUser")
    val isNewUser: Boolean? = null
)
