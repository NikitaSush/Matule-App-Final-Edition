package com.aiden3630.data.di

import com.aiden3630.data.manager.TokenManager
import com.aiden3630.data.repository.ProjectRepositoryImpl
import com.aiden3630.domain.repository.ProjectRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProjectModule {
    @Provides
    @Singleton
    fun provideProjectRepository(tokenManager: TokenManager): ProjectRepository {
        return ProjectRepositoryImpl(tokenManager)
    }
}