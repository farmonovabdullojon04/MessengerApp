package com.abdullojon.messengerapp.presentation.auth.phone

data class AuthPhoneState(
    val countryCode: String = "+998",
    val countryName: String = "Uzbekistan",
    val countryFlag: String = "🇺🇿",
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val fullPhoneNumber: String
        get() = "$countryCode$phoneNumber"

    val isPhoneValid: Boolean
        get() = phoneNumber.length == 9
}

sealed interface AuthPhoneSideEffect {
    data class OpenTelegramBot(val url: String) : AuthPhoneSideEffect
    data class ShowToast(val message: String) : AuthPhoneSideEffect
}

sealed interface AuthPhoneIntent {
    data class OnPhoneNumberChange(val value: String) : AuthPhoneIntent
    data object OnSubmit : AuthPhoneIntent
    data object OnClearError : AuthPhoneIntent
}
