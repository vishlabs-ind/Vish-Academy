package com.rach.co.di

import com.rach.co.auth.data.repository.AuthRepositoryImpl
import com.rach.co.auth.domain.repository.AuthRepository
import com.rach.co.homescreen.data.RepoImpl.CourseRepositoryImpl
import com.rach.co.homescreen.domain.Repo.CourseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}
