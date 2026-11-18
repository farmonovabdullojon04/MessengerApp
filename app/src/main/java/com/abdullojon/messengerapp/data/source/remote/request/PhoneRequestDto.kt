package com.abdullojon.messengerapp.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class PhoneRequestDto(
    @SerializedName("phone")
    val phone: String
)
