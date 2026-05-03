package com.rach.co.quiz.data.dataClass

data class Question(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)