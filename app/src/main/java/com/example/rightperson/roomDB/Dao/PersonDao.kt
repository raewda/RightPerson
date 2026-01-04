package com.example.rightperson.roomDB.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.rightperson.roomDB.Tables.Person
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Query("SELECT * FROM Person")
    fun getAllPerson(): Flow<List<Person>>

    @Insert
    suspend fun insert(item: Person)

    @Update(onConflict = REPLACE)
    suspend fun update(item: Person)

    @Delete
    suspend fun delete(item: Person)
}