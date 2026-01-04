package com.example.rightperson.roomDB.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.rightperson.roomDB.Tables.Negative
import kotlinx.coroutines.flow.Flow

@Dao
interface NegativeDao {
    @Query("SELECT * FROM Negative")
    fun getAllNegative(): Flow<List<Negative>>

    @Insert
    suspend fun insert(item: Negative)

    @Update(onConflict = REPLACE)
    suspend fun update(item: Negative)

    @Delete
    suspend fun delete(item: Negative)
}