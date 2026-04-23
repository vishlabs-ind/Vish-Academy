package com.rach.co.exam.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.rach.co.exam.data.dataClass.ExamCourse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ExamFirestoreRepository @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getEnabledExamCourses(): Result<List<ExamCourse>> {
        return try {
            val snapshot = db.collection("quizstart")
                .whereEqualTo("isTestEnabled", true)
                .get()
                .await()

            val courses = snapshot.documents.map { doc ->
                ExamCourse(
                    courseId = doc.id,
                    title = doc.getString("title") ?: "",
                    startTime = doc.getTimestamp("startTime"),
                    endTime = doc.getTimestamp("endTime"),
                    isTestEnabled = doc.getBoolean("isTestEnabled") ?: false
                )
            }

            Log.d("ExamFirestore", "✅ Fetched ${courses.size} courses")
            Result.success(courses)

        } catch (e: Exception) {
            Log.e("ExamFirestore", "❌ Failed: ${e.message}")
            Result.failure(e)
        }
    }
}