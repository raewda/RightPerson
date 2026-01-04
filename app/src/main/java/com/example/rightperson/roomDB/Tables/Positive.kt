package com.example.rightperson.roomDB.Tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// аннотация, что этот класс - таблица в базе данных Room
@Entity()
data class Positive(
    @ColumnInfo // @ColumnInfo - каждый столбец таблицы (если не пометить аннотацией, то Room не добавит этот столбец в базу)
        @PrimaryKey(autoGenerate = true) // первичный ключ
            val id: Int? = null,
    @ColumnInfo
        val title: String = ""
)