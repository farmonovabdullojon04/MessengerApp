package com.abdullojon.messengerapp.navigation

import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object AppAppNavigationDispatcher : AppNavigator, AppNavigationHandler {

    private val _backStack = MutableSharedFlow<AppNavigationParam>(extraBufferCapacity = 1)
    override val backStack = _backStack.asSharedFlow()

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private fun navigate(param: AppNavigationParam) {
        scope.launch {
            _backStack.emit(param)
        }
    }

    override fun navigateTo(screen: Screen) = navigate {
        push(screen)
    }

    override fun back() = navigate {
        pop()
    }

    override fun replace(screen: Screen) = navigate {
        replace(screen)
    }

    override fun replaceAll(screen: Screen) = navigate {
        replaceAll(screen)
    }
}
