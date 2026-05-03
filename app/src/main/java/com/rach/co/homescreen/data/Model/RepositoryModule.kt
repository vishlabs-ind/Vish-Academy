package com.rach.co.homescreen.data.Model


import com.rach.co.homescreen.data.RepoImpl.CourseRepositoryImpl
import com.rach.co.homescreen.domain.Repo.CourseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {



    @Binds
    abstract fun bindCourseRepo(
        impl: CourseRepositoryImpl
    ): CourseRepository
}