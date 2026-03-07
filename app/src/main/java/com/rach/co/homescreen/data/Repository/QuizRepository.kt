package com.rach.co.homescreen.data.Repository

import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.homescreen.data.local.QuizLocalDataSource

class QuizRepository(
    private val localDataSource: QuizLocalDataSource
) {

    // Load all courses from JSON
    fun getAllCourses(): List<Course> {
        return localDataSource.getAllCourses()
    }

    // Get specific course by courseId
    fun getCourseById(courseId: String): Course? {
        return localDataSource
            .getAllCourses()
            .find { it.courseId == courseId }
    }
}