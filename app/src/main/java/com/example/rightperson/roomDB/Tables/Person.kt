package com.example.rightperson.roomDB.Tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.rightperson.roomDB.Result

@Entity
data class Person(
    @ColumnInfo
        @PrimaryKey(autoGenerate = true)
            val id: Int? = null,
    @ColumnInfo
        val name: String? = "",
    @ColumnInfo
        val result: Result? = Result.NeutralFlag
)


