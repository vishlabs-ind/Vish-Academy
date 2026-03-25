package com.rach.co.homescreen.data.Model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.homescreen.domain.Repo.CourseDao

@Database(
    entities = [Course::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao
}