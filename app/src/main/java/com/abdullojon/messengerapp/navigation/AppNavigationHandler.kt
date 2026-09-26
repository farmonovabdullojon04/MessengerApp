package com.abdullojon.messengerapp.navigation

import androidx.lifecycle.LiveData

interface AppNavigationHandler {
    val backStack: LiveData<AppNavigationParam>
}