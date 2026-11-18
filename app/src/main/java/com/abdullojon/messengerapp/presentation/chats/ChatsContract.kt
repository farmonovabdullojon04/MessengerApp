package com.abdullojon.messengerapp.presentation.chats

import com.abdullojon.messengerapp.data.source.remote.response.UserResponseDto

data class ChatItem(
    val id: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val avatarInitials: String = "",
    val avatarBgColor: Long = 0xFF2B92E4,
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isMuted: Boolean = false,
    val isRead: Boolean = true
)

data class ChatsState(
    val searchQuery: String = "",
    val chats: List<ChatItem> = emptyList(),
    val searchedUsers: List<UserResponseDto> = emptyList(),
    val isSearching: Boolean = false,
    val isLoading: Boolean = false
)

sealed interface ChatsSideEffect {
    data class ShowToast(val message: String) : ChatsSideEffect
}

sealed interface ChatsIntent {
    data class OnSearchQueryChange(val query: String) : ChatsIntent
    data class OnChatItemClick(val chatId: String) : ChatsIntent
    data class OnUserClick(val userId: String) : ChatsIntent
    data object OnSettingsClick : ChatsIntent
}
