package com.abdullojon.messengerapp.di

import com.abdullojon.messengerapp.navigation.AppAppNavigationDispatcher
import com.abdullojon.messengerapp.navigation.AppNavigationHandler
import com.abdullojon.messengerapp.navigation.AppNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Provides
    @Singleton
    fun provideAppNavigator(): AppNavigator = AppAppNavigationDispatcher

    @Provides
    @Singleton
    fun provideAppNavigationHandler(): AppNavigationHandler = AppAppNavigationDispatcher
}

