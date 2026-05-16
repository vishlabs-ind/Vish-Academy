package com.rach.co.mock.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.rach.co.mock.data.dataClass.ExamData
import com.rach.co.mock.data.dataClass.Subject
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MockLocalRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val defaultDurationMinutes = 120
    private val fileName = "mock.json"  // ← single String not List

    private fun readAllSubjects(): List<Subject> {
        return try {
            val jsonString = context.assets
                .open(fileName)
                .bufferedReader()
                .use { it.readText() }

            val examData = Gson().fromJson(jsonString, ExamData::class.java)

            examData.subjects.map { subject ->
                subject.copy(
                    examDurationMinutes = if (subject.examDurationMinutes == 0)
                        defaultDurationMinutes else subject.examDurationMinutes
                )
            }.also {
                Log.d("MockLocal", "✅ Loaded ${it.size} subjects from $fileName")
            }

        } catch (e: Exception) {
            Log.e("MockLocal", "❌ Failed to read $fileName: ${e.message}")
            emptyList()
        }
    }

    fun getAllSubjects(): List<Subject> {
        return readAllSubjects().also {
            Log.d("MockLocal", "✅ Total subjects: ${it.size}")
        }
    }

    fun getSubjectById(subjectId: String): Subject? {
        return readAllSubjects().find { it.subjectId == subjectId }.also {
            if (it == null) Log.e("MockLocal", "❌ Not found: $subjectId")
            else Log.d("MockLocal", "✅ Found: ${it.subjectTitle}")
        }
    }
}