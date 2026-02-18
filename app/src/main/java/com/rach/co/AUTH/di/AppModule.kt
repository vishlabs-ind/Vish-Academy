package com.rach.co.AUTH.di

import com.google.firebase.auth.FirebaseAuth
import com.rach.co.AUTH.data.remote.FirebaseAuthSource
import com.rach.co.AUTH.domain.repository.AuthRepository
import com.rach.co.AUTH.domain.usecase.CheckEmailVerifiedUseCase
import com.rach.co.AUTH.domain.usecase.SendVerificationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSendVerificationUseCase(
        repo: AuthRepository
    ): SendVerificationUseCase {
        return SendVerificationUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideCheckEmailVerifiedUseCase(
        repo: AuthRepository
    ): CheckEmailVerifiedUseCase {
        return CheckEmailVerifiedUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthSource(
        auth: FirebaseAuth
    ): FirebaseAuthSource {
        return FirebaseAuthSource(auth)
    }



}
