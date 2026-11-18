package com.abdullojon.messengerapp.presentation.chats

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

import com.abdullojon.messengerapp.data.source.remote.response.UserResponseDto

class ChatsScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getViewModel<ChatsViewModel>()
        val state by viewModel.collectAsState()
        val context = LocalContext.current

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is ChatsSideEffect.ShowToast -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        ChatsContent(
            state = state,
            onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
            onUserClick = { userId -> viewModel.onUserClick(userId) },
            onSettingsClick = { viewModel.onSettingsClick() }
        )
    }
}

@Composable
fun ChatsContent(
    state: ChatsState,
    onSearchQueryChange: (String) -> Unit,
    onUserClick: (String) -> Unit = {},
    onSettingsClick: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = { TelegramChatsBottomNavigationBar(onSettingsClick = onSettingsClick) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Top Navigation Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Edit",
                    color = Color(0xFF007AFF),
                    fontSize = 17.sp,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                Text(
                    text = "Chats",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )

                Icon(
                    imageVector = Icons.Default.EditNote,
                    contentDescription = "New Message",
                    tint = Color(0xFF007AFF),
                    modifier = Modifier
                        .size(26.dp)
                        .align(Alignment.CenterEnd)
                )
            }

            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .background(Color(0xFFE3E3E8), shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF8E8E93),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = state.searchQuery,
                        onValueChange = onSearchQueryChange,
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box {
                                if (state.searchQuery.isEmpty()) {
                                    Text("Search for messages or users", color = Color(0xFF8E8E93), fontSize = 15.sp)
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chats or Search Results List
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.searchQuery.isNotEmpty()) {
                    if (state.searchedUsers.isNotEmpty()) {
                        item {
                            Text(
                                text = "Global Search",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8E8E93),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                        items(state.searchedUsers, key = { it.id ?: it.hashCode().toString() }) { user ->
                            UserSearchItemRow(user = user, onClick = {
                                user.id?.let { id -> onUserClick(id) }
                            })
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 76.dp),
                                color = Color(0xFFE5E5EA),
                                thickness = 0.8.dp
                            )
                        }
                    } else if (!state.isSearching) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No users found", color = Color.Gray, fontSize = 15.sp)
                            }
                        }
                    }
                } else {
                    items(state.chats, key = { it.id }) { chat ->
                        ChatItemRow(chat = chat)
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 76.dp),
                            color = Color(0xFFE5E5EA),
                            thickness = 0.8.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserSearchItemRow(user: UserResponseDto, onClick: () -> Unit) {
    val displayName = listOfNotNull(user.firstName, user.lastName).joinToString(" ").ifBlank { user.username ?: "User" }
    val initials = displayName.take(2).uppercase()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color(0xFF2B92E4)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = displayName,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            if (!user.username.isNullOrEmpty()) {
                Text(
                    text = "@${user.username}",
                    fontSize = 14.sp,
                    color = Color(0xFF007AFF)
                )
            }
        }
    }
}

@Composable
private fun ChatItemRow(chat: ChatItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar Circle
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color(chat.avatarBgColor)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = chat.avatarInitials,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Chat Info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (chat.isMuted) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.NotificationsOff,
                        contentDescription = "Muted",
                        tint = Color(0xFF8E8E93),
                        modifier = Modifier.size(14.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = chat.time,
                    fontSize = 14.sp,
                    color = if (chat.unreadCount > 0) Color(0xFF007AFF) else Color(0xFF8E8E93)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.lastMessage,
                    fontSize = 14.sp,
                    color = Color(0xFF8E8E93),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (chat.isPinned) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = "Pinned",
                        tint = Color(0xFF8E8E93),
                        modifier = Modifier.size(16.dp)
                    )
                } else if (chat.unreadCount > 0) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (chat.isMuted) Color(0xFFB0BEC5) else Color(0xFF007AFF),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = chat.unreadCount.toString(),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TelegramChatsBottomNavigationBar(onSettingsClick: () -> Unit) {
    NavigationBar(
        containerColor = Color(0xFFF9F9F9),
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Person, contentDescription = "Contacts", tint = Color(0xFF8E8E93)) },
            label = { Text("Contacts", fontSize = 11.sp, color = Color(0xFF8E8E93)) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Call, contentDescription = "Calls", tint = Color(0xFF8E8E93)) },
            label = { Text("Calls", fontSize = 11.sp, color = Color(0xFF8E8E93)) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = {
                BadgedBox(
                    badge = {
                        Badge(containerColor = Color(0xFFFF3B30), contentColor = Color.White) {
                            Text("2")
                        }
                    }
                ) {
                    Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Chats", tint = Color(0xFF007AFF))
                }
            },
            label = { Text("Chats", fontSize = 11.sp, color = Color(0xFF007AFF)) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = false,
            onClick = onSettingsClick,
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color(0xFF8E8E93)) },
            label = { Text("Settings", fontSize = 11.sp, color = Color(0xFF8E8E93)) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatsScreenPreview() {
    ChatsContent(
        state = ChatsState(
            chats = listOf(
                ChatItem(
                    id = "1",
                    name = "Saved Messages",
                    lastMessage = "image.jpeg",
                    time = "Fri",
                    avatarInitials = "🔖",
                    isPinned = true
                ),
                ChatItem(
                    id = "2",
                    name = "Telegram Designers",
                    lastMessage = "GIF, Suggested by @alex_21",
                    time = "10:42",
                    avatarInitials = "TD",
                    unreadCount = 17
                )
            )
        ),
        onSearchQueryChange = {},
        onSettingsClick = {}
    )
}
