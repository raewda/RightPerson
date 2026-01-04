package com.example.rightperson.roomDB.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.rightperson.roomDB.Tables.ResumeNegative
import com.example.rightperson.roomDB.Tables.ResumePositive
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumePositiveDao {
    @Query("SELECT * FROM ResumePositive")
    fun getAllResume(): Flow<List<ResumePositive>>

    @Insert
    suspend fun insert(item: ResumePositive)

    @Delete
    suspend fun delete(item: ResumePositive)
}

@Dao
interface ResumeNegativeDao {
    @Query("SELECT * FROM ResumeNegative")
    fun getAllResume(): Flow<List<ResumeNegative>>

    @Insert
    suspend fun insert(item: ResumeNegative)

    @Delete
    suspend fun delete(item: ResumeNegative)
}