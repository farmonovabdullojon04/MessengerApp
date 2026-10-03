package com.abdullojon.messengerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.abdullojon.messengerapp.navigation.AppAppNavigationDispatcher
import com.abdullojon.messengerapp.presentation.auth.phone.AuthPhoneScreen
import com.abdullojon.messengerapp.ui.theme.MessengerAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MessengerAppTheme {
                Navigator(AuthPhoneScreen()) { navigator ->
                    LaunchedEffect(Unit) {
                        AppAppNavigationDispatcher.backStack.collectLatest { param ->
                            param(navigator)
                        }
                    }

                    CurrentScreen()
                }
            }
        }
    }
}
