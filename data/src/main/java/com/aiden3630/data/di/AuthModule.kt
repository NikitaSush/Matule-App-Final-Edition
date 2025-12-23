package com.aiden3630.data.di

import com.aiden3630.data.manager.TokenManager
import com.aiden3630.data.network.AuthApi
import com.aiden3630.data.repository.AuthRepositoryImpl
import com.aiden3630.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    // 👇 ВОТ ЭТОЙ ФУНКЦИИ HILT НЕ ХВАТАЛО
    // Она говорит: "Если кто-то просит AuthRepository, дай ему AuthRepositoryImpl"
    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApi,
        tokenManager: TokenManager
    ): AuthRepository { // 👈 Важно: Возвращаемый тип должен быть интерфейсом
        return AuthRepositoryImpl(api, tokenManager)
    }
}