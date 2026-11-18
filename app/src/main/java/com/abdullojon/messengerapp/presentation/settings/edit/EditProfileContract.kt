package com.abdullojon.messengerapp.presentation.settings.edit

data class EditProfileState(
    val firstName: String = "Jacob W.",
    val lastName: String = "",
    val bio: String = "Digital goodies designer - Pixsellz",
    val phone: String = "+1 202 555 0147",
    val username: String = "@jacob_designer",
    val profileImage: String? = null,
    val isLoading: Boolean = false
)

sealed interface EditProfileSideEffect {
    data class ShowToast(val message: String) : EditProfileSideEffect
}

sealed interface EditProfileIntent {
    data class OnFirstNameChange(val value: String) : EditProfileIntent
    data class OnLastNameChange(val value: String) : EditProfileIntent
    data class OnBioChange(val value: String) : EditProfileIntent
    data object OnDone : EditProfileIntent
    data object OnLogOut : EditProfileIntent
}
