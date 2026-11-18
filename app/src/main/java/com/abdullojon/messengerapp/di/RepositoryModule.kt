package com.abdullojon.messengerapp.di

import com.abdullojon.messengerapp.data.repository.AuthRepositoryImpl
import com.abdullojon.messengerapp.data.repository.DeviceRepositoryImpl
import com.abdullojon.messengerapp.data.repository.UserRepositoryImpl
import com.abdullojon.messengerapp.domain.repository.AuthRepository
import com.abdullojon.messengerapp.domain.repository.DeviceRepository
import com.abdullojon.messengerapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    fun bindDeviceRepository(impl: DeviceRepositoryImpl): DeviceRepository
}
