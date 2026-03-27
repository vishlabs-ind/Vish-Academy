package com.rach.co.homescreen.data.Model

import android.content.Context
import androidx.room.Room
import com.rach.co.homescreen.domain.Repo.CourseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "Vishacademic_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCourseDao(db: AppDatabase): CourseDao {
        return db.courseDao()
    }
}