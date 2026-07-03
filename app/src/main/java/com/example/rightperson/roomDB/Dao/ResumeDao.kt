package com.example.rightperson.roomDB.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.rightperson.roomDB.Tables.Negative
import com.example.rightperson.roomDB.Tables.Positive
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

    @Query("SELECT p.* FROM Positive p " +
            "INNER JOIN ResumePositive rp ON p.id = rp.positiveId " +
            "WHERE rp.personId = :personId AND rp.has = 1"
    )
    fun getPositiveByPersonId(personId: Int): Flow<List<Positive>>
}

@Dao
interface ResumeNegativeDao {
    @Query("SELECT * FROM ResumeNegative")
    fun getAllResume(): Flow<List<ResumeNegative>>

    @Insert
    suspend fun insert(item: ResumeNegative)

    @Delete
    suspend fun delete(item: ResumeNegative)

    @Query("SELECT p.* FROM Negative p " +
            "INNER JOIN ResumeNegative rp ON p.id = rp.negativeId " +
            "WHERE rp.personId = :personId AND rp.has = 1"
    )
    fun getNegativeByPersonId(personId: Int): Flow<List<Negative>>
}