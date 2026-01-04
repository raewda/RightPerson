package com.example.rightperson.roomDB.Tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Negative(
    @ColumnInfo
        @PrimaryKey(autoGenerate = true)
            val id: Int? = null,
    @ColumnInfo
        val title: String? = ""
)