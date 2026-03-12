package com.rach.co.homescreen.data.DataClass

import com.rach.co.quiz.data.dataClass.Question

data class Course(
    val courseId: String = "",
    val title: String = "",
    val description: String = "",
    val subtitle: String = "",
    val displayName: String = "",
    val price: Int = 0,
    val order: Int = 0,
    val thumbnail: String = "",
    val courseTitle: String = "",
    val questions: List<Question> = emptyList()
)

data class Chapter(
    val name: String = ""
)

data class ChapterDetail(
    val ChapterName: String = "",
    val order: Int = 0,
    val ytlink: String = ""
)