package com.abdullojon.messengerapp.data.source.remote.request

import android.os.Build
import com.google.gson.annotations.SerializedName

data class VerifyOtpRequestDto(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("deviceName")
    val deviceName: String = Build.MODEL ?: "Android Device"
)
