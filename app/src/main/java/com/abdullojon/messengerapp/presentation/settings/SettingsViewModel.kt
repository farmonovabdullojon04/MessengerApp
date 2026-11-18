package com.abdullojon.messengerapp.presentation.settings

import androidx.lifecycle.ViewModel
import com.abdullojon.messengerapp.domain.repository.UserRepository
import com.abdullojon.messengerapp.navigation.AppNavigator
import com.abdullojon.messengerapp.presentation.settings.edit.EditProfileScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appNavigator: AppNavigator
) : ContainerHost<SettingsState, SettingsSideEffect>, ViewModel() {

    override val container = container<SettingsState, SettingsSideEffect>(SettingsState()) {
        loadUserProfile()
    }

    fun loadUserProfile() = intent {
        val result = userRepository.getMe()
        result.onSuccess { user ->
            val fullName = listOfNotNull(user.firstName, user.lastName)
                .joinToString(" ")
                .ifEmpty { "Jacob W." }

            reduce {
                state.copy(
                    name = fullName,
                    phone = user.phone ?: state.phone,
                    username = if (!user.username.isNullOrEmpty()) "@${user.username}" else state.username,
                    profileImage = user.avatarUrl
                )
            }
        }.onFailure { throwable ->
            postSideEffect(SettingsSideEffect.ShowToast(throwable.message ?: "Failed to load user profile"))
        }
    }

    fun onSearchQueryChange(query: String) = intent {
        reduce { state.copy(searchQuery = query) }
    }

    fun onEditClick() = intent {
        appNavigator.navigateTo(EditProfileScreen())
    }
}
