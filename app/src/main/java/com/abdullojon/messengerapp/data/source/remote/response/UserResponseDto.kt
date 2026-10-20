package com.abdullojon.messengerapp.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    @SerializedName("id")
    val id: String?=null,

    @SerializedName("phone")
    val phone: String?=null,

    @SerializedName("firstName")
    val firstName: String?=null,

    @SerializedName("lastName")
    val lastName: String?=null,

    @SerializedName("username")
    val username: String?=null,

    @SerializedName("bio")
    val bio: String?=null,

    @SerializedName("avatarUrl")
    val avatarUrl: String?=null


)
