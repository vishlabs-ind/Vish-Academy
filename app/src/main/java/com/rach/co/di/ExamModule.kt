package com.rach.co.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.rach.co.exam.domain.ExamRepository
import com.rach.co.exam.domain.repository.ExamFirebaseRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class ExamModule {

    @Binds
    @Singleton
    abstract fun bindExamRepository(
        impl: ExamRepository  // ← Implementation class
    ): ExamRepository  // ← Interface

    @Binds
    @Singleton
    abstract fun bindExamFirebaseRepository(
        impl: ExamFirebaseRepository  // ← Implementation class
    ): ExamFirebaseRepository  // ← Interface
}