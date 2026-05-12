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
    // default 2 hours if not set in JSON
    private val defaultDurationMinutes = 120
    private val jsonFiles = listOf("gk.json", "english.json", "computer.json")

    private fun readSubjectsFromFile(fileName: String): List<Subject> {
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
        // read all files and merge all subjects into one list
        return jsonFiles.flatMap { fileName ->
            readSubjectsFromFile(fileName)
        }.also {
            Log.d("MockLocal", "✅ Total subjects loaded: ${it.size}")
        }
    }

    fun getSubjectById(subjectId: String): Subject? {
        // search across all files
        return jsonFiles.firstNotNullOfOrNull { fileName ->
            readSubjectsFromFile(fileName).find { it.subjectId == subjectId }
        }.also {
            if (it == null) Log.e("MockLocal", "❌ Subject not found: $subjectId")
            else Log.d("MockLocal", "✅ Found: ${it.subjectTitle}")
        }
    }
}