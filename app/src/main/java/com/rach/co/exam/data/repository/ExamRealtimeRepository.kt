package com.rach.co.exam.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rach.co.exam.data.dataClass.ExamResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ExamRealtimeRepository @Inject constructor() {

    private val db = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    // save result to Realtime Database
    suspend fun saveResult(result: ExamResult): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("User not logged in"))

            db.child("examResults")
                .child(userId)
                .child(result.subjectId)
                .setValue(result)
                .await()

            Log.d("ExamRealtime", "✅ Result saved — score: ${result.score}")
            Result.success(Unit)

        } catch (e: Exception) {
            Log.e("ExamRealtime", "❌ Failed to save: ${e.message}")
            Result.failure(e)
        }
    }

    // check if user already attempted this exam
    suspend fun hasAlreadyAttempted(courseId: String): Boolean {
        return try {
            val userId = auth.currentUser?.uid ?: return false

            val snapshot = db.child("examResults")
                .child(userId)
                .child(courseId)
                .get()
                .await()

            snapshot.exists().also {
                Log.d("ExamRealtime", "Attempted check — $courseId: $it")
            }

        } catch (e: Exception) {
            Log.e("ExamRealtime", "❌ Check failed: ${e.message}")
            false
        }
    }
}