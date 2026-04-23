package com.rach.co.exam.data.dataClass

data class Subject (
    val subjectId: String = " ",
    val subjectTitle: String = " ",
    val examDurationMinutes: Int = 60,
    val questions: List<Question> = emptyList()
)

data class Question(
    val id: Int = 0,
    val question: String = " ",
    val options: List<String> = emptyList(),
    val correctAnswerIndex: Int = 0
)

data class ExamData(
    val subjects: List<Subject> = emptyList()
)