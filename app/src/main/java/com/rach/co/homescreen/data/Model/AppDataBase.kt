package com.rach.co.homescreen.data.Model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.homescreen.domain.Repo.CourseDao
import com.rach.co.mock.data.repository.MockResultEntity
import com.rach.co.mock.data.room.MockDao


@Database(
    entities = [Course::class, MockResultEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun mockDao(): MockDao
}