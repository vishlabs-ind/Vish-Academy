package com.rach.co.exam.data.DataClass

data class Subject(
    val subjectId: String,
    val subjectTitle: String,
    val examDurationMinutes: Int,
    val subjectDescription: String,
    val icon: String,           // emoji icon for simplicity
    val totalQuestions: Int,
    val questions: List<Question>
)

data class Question(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)