package com.abdullojon.messengerapp.presentation.auth.verify

import java.util.Locale

data class AuthVerifyOtpState(
    val phone: String = "",
    val code: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isVerified: Boolean = false,
    val timerSeconds: Int = 82
) {
    val isCodeValid: Boolean
        get() = code.length == 6

    val formattedTimer: String
        get() = String.format(Locale.US, "%d:%02d", timerSeconds / 60, timerSeconds % 60)
}

sealed interface AuthVerifyOtpSideEffect {
    data class ShowToast(val message: String) : AuthVerifyOtpSideEffect
}

sealed interface AuthVerifyOtpIntent {
    data class OnCodeChange(val value: String) : AuthVerifyOtpIntent
    data object OnVerify : AuthVerifyOtpIntent
    data object OnResendCode : AuthVerifyOtpIntent
}
