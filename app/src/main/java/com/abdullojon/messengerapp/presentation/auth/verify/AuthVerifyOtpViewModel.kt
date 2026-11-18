package com.abdullojon.messengerapp.presentation.auth.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullojon.messengerapp.domain.repository.AuthRepository
import com.abdullojon.messengerapp.navigation.AppNavigator
import com.abdullojon.messengerapp.presentation.chats.ChatsScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AuthVerifyOtpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appNavigator: AppNavigator
) : ContainerHost<AuthVerifyOtpState, AuthVerifyOtpSideEffect>, ViewModel() {

    override val container = container<AuthVerifyOtpState, AuthVerifyOtpSideEffect>(AuthVerifyOtpState())

    private var timerJob: Job? = null

    fun init(phone: String) = intent {
        reduce { state.copy(phone = phone) }
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            intent { reduce { state.copy(timerSeconds = 82) } }
            while (container.stateFlow.value.timerSeconds > 0) {
                delay(1000)
                intent {
                    reduce {
                        if (state.timerSeconds > 0) {
                            state.copy(timerSeconds = state.timerSeconds - 1)
                        } else state
                    }
                }
            }
        }
    }

    fun onCodeChange(value: String) = intent {
        val digitsOnly = value.filter { it.isDigit() }
        if (digitsOnly.length <= 6) {
            reduce {
                state.copy(
                    code = digitsOnly,
                    errorMessage = null
                )
            }
            if (digitsOnly.length == 6) {
                verify(digitsOnly)
            }
        }
    }

    fun onVerify() = intent {
        if (state.isCodeValid) {
            verify(state.code)
        }
    }

    fun onResendCode() = intent {
        if (state.timerSeconds > 0) return@intent
        reduce { state.copy(isLoading = true, errorMessage = null) }
        val result = authRepository.requestOtp(state.phone)
        reduce { state.copy(isLoading = false) }
        result.onSuccess {
            postSideEffect(AuthVerifyOtpSideEffect.ShowToast("Code resent successfully!"))
            startTimer()
        }.onFailure { throwable ->
            val message = throwable.message ?: "Failed to resend code"
            reduce { state.copy(errorMessage = message) }
            postSideEffect(AuthVerifyOtpSideEffect.ShowToast(message))
        }
    }

    private fun verify(code: String) = intent {
        reduce { state.copy(isLoading = true, errorMessage = null) }

        val result = authRepository.verifyOtp(state.phone, code)

        reduce { state.copy(isLoading = false) }

        result.onSuccess {
            reduce { state.copy(isVerified = true) }
            postSideEffect(AuthVerifyOtpSideEffect.ShowToast("Muvaffaqiyatli avtorizatsiyadan o'tildi!"))
            appNavigator.replaceAll(ChatsScreen())
        }.onFailure { throwable ->
            val message = throwable.message ?: "Verification failed"
            reduce { state.copy(errorMessage = message) }
            postSideEffect(AuthVerifyOtpSideEffect.ShowToast(message))
        }
    }

    fun onBack() {
        appNavigator.back()
    }
}
