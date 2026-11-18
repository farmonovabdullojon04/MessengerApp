package com.abdullojon.messengerapp.presentation.auth.phone

import androidx.lifecycle.ViewModel
import com.abdullojon.messengerapp.data.repository.TelegramNotLinkedException
import com.abdullojon.messengerapp.domain.repository.AuthRepository
import com.abdullojon.messengerapp.navigation.AppNavigator
import com.abdullojon.messengerapp.presentation.auth.verify.AuthVerifyOtpScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AuthPhoneViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appNavigator: AppNavigator
) : ContainerHost<AuthPhoneState, AuthPhoneSideEffect>, ViewModel() {

    override val container = container<AuthPhoneState, AuthPhoneSideEffect>(AuthPhoneState())

    fun onPhoneNumberChange(value: String) = intent {
        val digitsOnly = value.filter { it.isDigit() }
        if (digitsOnly.length <= 9) {
            reduce {
                state.copy(
                    phoneNumber = digitsOnly,
                    errorMessage = null
                )
            }
        }
    }

    fun onSubmit() = intent {
        if (!state.isPhoneValid) return@intent

        reduce { state.copy(isLoading = true, errorMessage = null) }

        val fullPhone = state.fullPhoneNumber
        val result = authRepository.requestOtp(fullPhone)

        reduce { state.copy(isLoading = false) }

        result.onSuccess {
            appNavigator.navigateTo(AuthVerifyOtpScreen(phone = fullPhone))
        }.onFailure { throwable ->
            if (throwable is TelegramNotLinkedException) {
                val botUrl = throwable.botUrl ?: "https://t.me/"
                reduce { state.copy(errorMessage = throwable.message) }
                postSideEffect(AuthPhoneSideEffect.OpenTelegramBot(botUrl))
            } else {
                val message = throwable.message ?: "An error occurred"
                reduce { state.copy(errorMessage = message) }
                postSideEffect(AuthPhoneSideEffect.ShowToast(message))
            }
        }
    }

    fun onClearError() = intent {
        reduce { state.copy(errorMessage = null) }
    }
}
