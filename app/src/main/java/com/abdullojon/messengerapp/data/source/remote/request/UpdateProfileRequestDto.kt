package com.abdullojon.messengerapp.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequestDto(
    @SerializedName("displayName")
    val displayName:String?=null,

    @SerializedName("username")
    val username: String?=null,

    @SerializedName("bio")
    val bio: String?=null,

    @SerializedName("avatarMediaId")
    val avatarMediaId: String?=null
)
