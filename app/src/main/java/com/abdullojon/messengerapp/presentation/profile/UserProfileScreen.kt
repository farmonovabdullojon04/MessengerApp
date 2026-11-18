package com.abdullojon.messengerapp.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

data class UserProfileScreen(val userId: String) : Screen {

    @Composable
    override fun Content() {
        val viewModel: UserProfileViewModel = getViewModel()
        val state by viewModel.collectAsState()
        val context = LocalContext.current

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is UserProfileSideEffect.ShowToast -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        LaunchedEffect(userId) {
            viewModel.loadUserProfile(userId)
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                state.user?.let { user ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${user.firstName ?: ""} ${user.lastName ?: ""}")
                        Text(text = "@${user.username ?: ""}")
                        Text(text = user.phone ?: "")
                        Text(text = user.bio ?: "")
                    }
                }
            }
        }
    }
}