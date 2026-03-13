package com.rach.co.homescreen.data.DataClass

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rach.co.quiz.data.dataClass.Question

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey
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