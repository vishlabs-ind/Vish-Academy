package com.rach.co.homescreen.domain.Repo

import androidx.room.*
import com.rach.co.homescreen.data.DataClass.Course
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<Course>)

    @Query("SELECT * FROM courses ORDER BY `order` ASC")
    fun getCourses(): Flow<List<Course>>

    @Query("DELETE FROM courses")
    suspend fun clearCourses()
}