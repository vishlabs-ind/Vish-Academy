package com.rach.co.mock.data.repository

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mock_results")
data class MockResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subjectId: String,
    val subjectTitle: String,
    val score: Int,
    val timeTakenSeconds: Long,
    val answers: String  // format: "0:1,1:0,2:-1,3:2,4:1"
)