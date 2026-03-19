package com.rach.co.homescreen.data.Model


import com.google.firebase.firestore.FirebaseFirestore
import com.rach.co.homescreen.data.RepoImpl.CourseRepositoryImpl
import com.rach.co.homescreen.domain.Repo.CourseRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }



   
    
}