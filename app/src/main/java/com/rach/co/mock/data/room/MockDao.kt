package com.rach.co.mock.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rach.co.mock.data.repository.MockResultEntity

@Dao
interface MockDao {

    // insert result after submit
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertResult(result: MockResultEntity): Long  // returns generated id

    // fetch result by id for review screen
    @Query("SELECT * FROM mock_results WHERE id = :id")
    suspend fun getResultById(id: Int): MockResultEntity?

    // fetch all results by subjectId for history (future use)
    @Query("SELECT * FROM mock_results WHERE subjectId = :subjectId ORDER BY id DESC")
    suspend fun getResultsBySubject(subjectId: String): List<MockResultEntity>

    // fetch latest result by subjectId
    @Query("SELECT * FROM mock_results WHERE subjectId = :subjectId ORDER BY id DESC LIMIT 1")
    suspend fun getLatestResult(subjectId: String): MockResultEntity?
}