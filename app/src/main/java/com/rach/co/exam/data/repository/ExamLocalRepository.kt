package com.rach.co.exam.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.rach.co.exam.data.dataClass.ExamData
import com.rach.co.exam.data.dataClass.Subject
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ExamLocalRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getSubjectById(subjectId: String): Subject? {
        return try {
            val jsonString = context.assets
                .open("exam_questions.json")
                .bufferedReader()
                .use { it.readText() }

            Log.d("ExamTest", "JSON loaded successfully")
            Log.d("ExamTest", "Looking for subjectId: $subjectId")

            val examData = Gson().fromJson(jsonString, ExamData::class.java)

            Log.d("ExamTest", "Total subjects in JSON: ${examData.subjects.size}")
            examData.subjects.find { it.subjectId == subjectId }.also {
                Log.d("ExamTest", "Found subject: ${it?.subjectId}")
                if (it == null) {
                    Log.e("ExamLocal", "❌ Subject not found: $subjectId")
                } else {
                    Log.d("ExamLocal", "✅ Subject found: ${it.subjectTitle}, Qs: ${it.questions.size}")
                }
            }

        } catch (e: Exception) {
            Log.e("ExamLocal", "❌ Failed to read JSON: ${e.message}")
            null
        }
    }
}