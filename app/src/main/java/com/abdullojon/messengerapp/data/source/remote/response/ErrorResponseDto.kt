package com.abdullojon.messengerapp.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ErrorResponseDto(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("botUrl")
    val botUrl: String? = null
)
