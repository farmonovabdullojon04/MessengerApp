package com.abdullojon.messengerapp.presentation.settings.edit

import androidx.lifecycle.ViewModel
import com.abdullojon.messengerapp.data.source.remote.request.UpdateProfileRequestDto
import com.abdullojon.messengerapp.domain.repository.AuthRepository
import com.abdullojon.messengerapp.domain.repository.UserRepository
import com.abdullojon.messengerapp.navigation.AppNavigator
import com.abdullojon.messengerapp.presentation.auth.phone.AuthPhoneScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val appNavigator: AppNavigator
) : ContainerHost<EditProfileState, EditProfileSideEffect>, ViewModel() {

    override val container = container<EditProfileState, EditProfileSideEffect>(EditProfileState()) {
        loadUserProfile()
    }

    private fun loadUserProfile() = intent {
        reduce { state.copy(isLoading = true) }
        val result = userRepository.getMe()
        reduce { state.copy(isLoading = false) }

        result.onSuccess { user ->
            reduce {
                state.copy(
                    firstName = user.firstName ?: state.firstName,
                    lastName = user.lastName ?: state.lastName,
                    bio = user.bio ?: state.bio,
                    phone = user.phone ?: state.phone,
                    username = if (!user.username.isNullOrEmpty()) "@${user.username}" else state.username,
                    profileImage = user.avatarUrl
                )
            }
        }
    }

    fun onFirstNameChange(value: String) = intent {
        reduce { state.copy(firstName = value) }
    }

    fun onLastNameChange(value: String) = intent {
        reduce { state.copy(lastName = value) }
    }

    fun onBioChange(value: String) = intent {
        reduce { state.copy(bio = value) }
    }

    fun onBack() {
        appNavigator.back()
    }

    fun onLogOut() = intent {
        reduce { state.copy(isLoading = true) }
        authRepository.logout()
        reduce { state.copy(isLoading = false) }
        appNavigator.replaceAll(AuthPhoneScreen())
    }

    fun updateProfile() = intent {
        reduce { state.copy(isLoading = true) }

        val request = UpdateProfileRequestDto(
            displayName = "${state.firstName} ${state.lastName}".trim(),
            username = state.username.removePrefix("@"),
            bio = state.bio
        )

        val result = userRepository.updateProfile(request)
        reduce { state.copy(isLoading = false) }

        result.onSuccess {
            postSideEffect(EditProfileSideEffect.ShowToast("Profil muvaffaqiyatli yangilandi!"))
            appNavigator.back()
        }.onFailure { throwable ->
            postSideEffect(EditProfileSideEffect.ShowToast(throwable.message ?: "Xatolik yuz berdi"))
        }
    }
}
