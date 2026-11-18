package com.abdullojon.messengerapp.presentation.profile

import androidx.lifecycle.ViewModel
import com.abdullojon.messengerapp.domain.repository.UserRepository
import com.abdullojon.messengerapp.navigation.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appNavigator: AppNavigator
): ContainerHost<UserProfileState, UserProfileSideEffect>, ViewModel(){
    override val container = container<UserProfileState, UserProfileSideEffect>(UserProfileState())
    fun loadUserProfile(userId: String)=intent{
        reduce { state.copy(isLoading = true) }

        val result=userRepository.getUserById(userId)

        result.onSuccess { user->
            reduce { state.copy(isLoading = false,user=user) }
        }.onFailure { throwable ->
            reduce { state.copy(isLoading = false) }
            postSideEffect(UserProfileSideEffect.ShowToast(throwable.message?:"Profilni yuklab bo'lmadi"))
        }
    }

    fun onBackClick()=intent{
        appNavigator.back()
    }

}