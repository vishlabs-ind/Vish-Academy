package com.rach.co.homescreen.domain.Repo

import com.rach.co.homescreen.data.DataClass.Chapter
import com.rach.co.homescreen.data.DataClass.ChapterDetail
import com.rach.co.homescreen.data.DataClass.Course

interface CourseRepository {

    suspend fun getCourses(): List<Course>

    suspend fun buyCourse(order: Int): Course?

    suspend fun getPurchasedCourseIds(): List<String>
    suspend fun getCoursesByIds(
        ids: List<String>
    ): List<Course>

    suspend fun getChapters(
        courseId: String
    ): List<Chapter>

    suspend fun getChapterDetails(
        courseId: String,
        subjectName: String
    ): List<ChapterDetail>
}