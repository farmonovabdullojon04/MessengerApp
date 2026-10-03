package com.abdullojon.messengerapp.navigation

import kotlinx.coroutines.flow.SharedFlow

interface AppNavigationHandler {
    val backStack: SharedFlow<AppNavigationParam>
}
