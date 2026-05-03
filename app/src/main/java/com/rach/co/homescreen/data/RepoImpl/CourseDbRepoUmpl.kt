package com.rach.co.homescreen.data.RepoImpl

import com.rach.co.homescreen.data.DataClass.Course
import com.rach.co.homescreen.domain.Repo.CourseDao
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class CourseRepositoryDb @Inject constructor(
    private val dao: CourseDao
) {

    fun getCourses(): Flow<List<Course>> {
        return dao.getCourses()
    }

    suspend fun saveCourses(courses: List<Course>) {
        dao.insertCourses(courses)
    }
    suspend fun clearCourses() {
       dao.clearCourses()
    }
}