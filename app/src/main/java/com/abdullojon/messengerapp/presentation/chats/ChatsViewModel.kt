package com.abdullojon.messengerapp.presentation.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullojon.messengerapp.domain.repository.DeviceRepository
import com.abdullojon.messengerapp.domain.repository.UserRepository
import com.abdullojon.messengerapp.navigation.AppNavigator
import com.abdullojon.messengerapp.presentation.profile.UserProfileScreen
import com.abdullojon.messengerapp.presentation.settings.SettingsScreen
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val deviceRepository: DeviceRepository,
    private val appNavigator: AppNavigator
) : ContainerHost<ChatsState, ChatsSideEffect>, ViewModel() {

    init {
        sendFcmToken()
    }

    private fun sendFcmToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val fcmToken = task.result ?: return@addOnCompleteListener
                viewModelScope.launch {
                    deviceRepository.updateFcmToken(fcmToken)
                }
            }
        }
    }

    override val container = container<ChatsState, ChatsSideEffect>(
        ChatsState(
            chats = listOf(
                ChatItem(
                    id = "1",
                    name = "Saved Messages",
                    lastMessage = "image.jpeg",
                    time = "Fri",
                    avatarInitials = "🔖",
                    avatarBgColor = 0xFF2B92E4,
                    isPinned = true
                ),
                ChatItem(
                    id = "2",
                    name = "Pixsellz Team",
                    lastMessage = "Hasan Web GIF",
                    time = "9/29",
                    avatarInitials = "PT",
                    avatarBgColor = 0xFF1E1E1E,
                    isPinned = true,
                    isRead = true
                ),
                ChatItem(
                    id = "3",
                    name = "Joshua Lawrence",
                    lastMessage = "Let's choose the first option",
                    time = "Sun",
                    avatarInitials = "JL",
                    avatarBgColor = 0xFFE91E63,
                    isPinned = true
                ),
                ChatItem(
                    id = "4",
                    name = "Telegram Designers",
                    lastMessage = "GIF, Suggested by @alex_21",
                    time = "10:42",
                    avatarInitials = "TD",
                    avatarBgColor = 0xFF007AFF,
                    unreadCount = 17
                ),
                ChatItem(
                    id = "5",
                    name = "Albert Lasker",
                    lastMessage = "Like your quote about...",
                    time = "13:51",
                    avatarInitials = "AL",
                    avatarBgColor = 0xFFFF9500
                ),
                ChatItem(
                    id = "6",
                    name = "Figma Plugins",
                    lastMessage = "👋 iOS 13 Design Kit. Turn your ideas into incredible...",
                    time = "Sat",
                    avatarInitials = "FP",
                    avatarBgColor = 0xFF1E1E1E,
                    unreadCount = 32,
                    isMuted = true
                ),
                ChatItem(
                    id = "7",
                    name = "UX Live",
                    lastMessage = "🎨 Sketch App. 👨‍💻",
                    time = "11:30",
                    avatarInitials = "UX",
                    avatarBgColor = 0xFF1E1E1E,
                    unreadCount = 153
                )
            )
        )
    )

    fun onSearchQueryChange(query: String) = intent {
        reduce { state.copy(searchQuery = query) }

        if (query.isBlank()) {
            reduce { state.copy(searchedUsers = emptyList(), isSearching = false) }
            return@intent
        }

        reduce { state.copy(isSearching = true) }
        val result = userRepository.searchUsers(query = query)

        result.onSuccess { users ->
            reduce { state.copy(searchedUsers = users, isSearching = false) }
        }.onFailure { throwable ->
            reduce { state.copy(isSearching = false) }
            postSideEffect(ChatsSideEffect.ShowToast(throwable.message ?: "Qidiruvda xatolik yuz berdi"))
        }
    }

    fun onUserClick(userId: String) = intent {
        appNavigator.navigateTo(UserProfileScreen(userId = userId))
    }

    fun onSettingsClick() = intent {
        appNavigator.navigateTo(SettingsScreen())
    }
}
