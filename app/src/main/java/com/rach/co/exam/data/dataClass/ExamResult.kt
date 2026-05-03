package com.rach.co.exam.data.dataClass

data class ExamResult(
    val subjectId: String = "",
    val subjectTitle: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val submittedAt: Long = 0L
)