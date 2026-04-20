package com.rach.co.exam.data.dataClass

import com.google.firebase.Timestamp

data class ExamCourse(
    val courseId: String = "",
    val title: String = "",
    val startTime: Timestamp? = null,
    val endTime: Timestamp? = null,
    val isTestEnabled: Boolean = false
)