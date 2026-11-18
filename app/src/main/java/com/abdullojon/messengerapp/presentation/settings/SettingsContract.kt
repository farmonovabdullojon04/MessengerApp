package com.abdullojon.messengerapp.presentation.settings

data class SettingsState(
    val name: String = "Jacob W.",
    val phone: String = "+1 202 555 0147",
    val username: String = "@jacob_d",
    val profileImage: String? = null,
    val stickerBadgeCount: Int = 15,
    val searchQuery: String = ""
)

sealed interface SettingsSideEffect {
    data object NavigateToEditProfile : SettingsSideEffect
    data class ShowToast(val message: String) : SettingsSideEffect
}

sealed interface SettingsIntent {
    data class OnSearchQueryChange(val query: String) : SettingsIntent
    data object OnEditClick : SettingsIntent
}
