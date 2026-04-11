package com.rach.co.exam.domain

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rach.co.exam.data.ExamResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ExamRepository @Inject constructor() {

    private val db = FirebaseDatabase.getInstance().reference

    suspend fun saveResult(result: ExamResult) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.child("examResults")
            .child(userId)
            .child(result.subjectId)
            .setValue(result)
            .await()
    }
}