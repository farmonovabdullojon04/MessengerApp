package com.abdullojon.messengerapp.presentation.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmsFailed
import androidx.compose.material.icons.filled.Storage
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getViewModel<SettingsViewModel>()
        val state by viewModel.collectAsState()
        val context = LocalContext.current

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is SettingsSideEffect.ShowToast -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        SettingsContent(
            state = state,
            onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
            onEditClick = { viewModel.onEditClick() }
        )
    }
}

@Composable
fun SettingsContent(
    state: SettingsState,
    onSearchQueryChange: (String) -> Unit,
    onEditClick: () -> Unit
) {
    val backgroundColor = Color(0xFFF2F2F7)

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = { TelegramBottomNavigationBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Settings",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )

                Text(
                    text = "Edit",
                    color = Color(0xFF007AFF),
                    fontSize = 17.sp,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { onEditClick() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .background(Color(0xFFE3E3E8), shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                                    Text("Search", color = Color(0xFF8E8E93), fontSize = 16.sp)
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Profile Header Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable { onEditClick() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF9500)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("JW", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = state.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Text(
                        text = state.phone,
                        fontSize = 14.sp,
                        color = Color(0xFF8E8E93)
                    )
                    Text(
                        text = state.username,
                        fontSize = 14.sp,
                        color = Color(0xFF8E8E93)
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFFC7C7CC)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Secondary Accounts Group
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF34C759)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("JD", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Jacob Design", fontSize = 16.sp, color = Color.Black)
                }

                HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = Color(0xFFE5E5EA), thickness = 0.8.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Account",
                        tint = Color(0xFF007AFF),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    Text("Add Account", fontSize = 16.sp, color = Color(0xFF007AFF))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Menu Group
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                SettingsMenuItem(
                    icon = Icons.Default.Bookmark,
                    iconBgColor = Color(0xFF007AFF),
                    title = "Saved Messages",
                    onClick = {}
                )
                HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = Color(0xFFE5E5EA), thickness = 0.8.dp)
                SettingsMenuItem(
                    icon = Icons.Default.Phone,
                    iconBgColor = Color(0xFF34C759),
                    title = "Recent Calls",
                    onClick = {}
                )
                HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = Color(0xFFE5E5EA), thickness = 0.8.dp)
                SettingsMenuItem(
                    icon = Icons.Default.SmsFailed,
                    iconBgColor = Color(0xFFFF9500),
                    title = "Stickers",
                    badgeCount = state.stickerBadgeCount,
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Settings Inset Group
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                SettingsMenuItem(
                    icon = Icons.Default.Notifications,
                    iconBgColor = Color(0xFFFF3B30),
                    title = "Notifications and Sounds",
                    onClick = {}
                )
                HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = Color(0xFFE5E5EA), thickness = 0.8.dp)
                SettingsMenuItem(
                    icon = Icons.Default.Lock,
                    iconBgColor = Color(0xFF8E8E93),
                    title = "Privacy and Security",
                    onClick = {}
                )
                HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = Color(0xFFE5E5EA), thickness = 0.8.dp)
                SettingsMenuItem(
                    icon = Icons.Default.Storage,
                    iconBgColor = Color(0xFF34C759),
                    title = "Data and Storage",
                    onClick = {}
                )
                HorizontalDivider(modifier = Modifier.padding(start = 58.dp), color = Color(0xFFE5E5EA), thickness = 0.8.dp)
                SettingsMenuItem(
                    icon = Icons.Default.Palette,
                    iconBgColor = Color(0xFF5AC8FA),
                    title = "Appearance",
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SettingsMenuItem(
    icon: ImageVector,
    iconBgColor: Color,
    title: String,
    badgeCount: Int? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        if (badgeCount != null && badgeCount > 0) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF007AFF), shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badgeCount.toString(),
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color(0xFFC7C7CC),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun TelegramBottomNavigationBar() {
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
            selected = false,
            onClick = {},
            icon = {
                BadgedBox(
                    badge = {
                        Badge(containerColor = Color(0xFFFF3B30), contentColor = Color.White) {
                            Text("2")
                        }
                    }
                ) {
                    Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Chats", tint = Color(0xFF8E8E93))
                }
            },
            label = { Text("Chats", fontSize = 11.sp, color = Color(0xFF8E8E93)) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF007AFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            },
            label = { Text("Settings", fontSize = 11.sp, color = Color(0xFF007AFF)) },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsContent(
        state = SettingsState(),
        onSearchQueryChange = {},
        onEditClick = {}
    )
}
