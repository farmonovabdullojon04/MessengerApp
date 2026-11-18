package com.abdullojon.messengerapp.presentation.profile

import com.abdullojon.messengerapp.data.source.remote.response.UserResponseDto

data class UserProfileState(
    val isLoading: Boolean=false,
    val user: UserResponseDto?=null
)
sealed interface UserProfileSideEffect{
    data class ShowToast(val message: String): UserProfileSideEffect
}