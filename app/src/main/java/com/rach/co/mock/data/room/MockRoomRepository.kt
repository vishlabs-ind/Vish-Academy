package com.rach.co.mock.data.room

import android.util.Log
import com.rach.co.mock.data.room.MockDao
import com.rach.co.mock.data.repository.MockResultEntity
import javax.inject.Inject

class MockRoomRepository @Inject constructor(
    private val mockDao: MockDao
) {

    // save result after submit or auto save
    // returns generated id for navigation to review screen
    suspend fun saveResult(result: MockResultEntity): Result<Int> {
        return try {
            val id = mockDao.insertResult(result).toInt()
            Log.d("MockRoom", "✅ Result saved — id: $id, score: ${result.score}")
            Result.success(id)
        } catch (e: Exception) {
            Log.e("MockRoom", "❌ Failed to save: ${e.message}")
            Result.failure(e)
        }
    }

    // fetch result by id for review screen
    suspend fun getResultById(id: Int): Result<MockResultEntity> {
        return try {
            val result = mockDao.getResultById(id)
            if (result == null) {
                Log.e("MockRoom", "❌ Result not found for id: $id")
                Result.failure(Exception("Result not found"))
            } else {
                Log.d("MockRoom", "✅ Result fetched — id: $id")
                Result.success(result)
            }
        } catch (e: Exception) {
            Log.e("MockRoom", "❌ Exception: ${e.message}")
            Result.failure(e)
        }
    }
}