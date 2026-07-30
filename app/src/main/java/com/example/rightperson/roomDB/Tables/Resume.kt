package com.example.rightperson.roomDB.Tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Person::class, // родительская таблица
            parentColumns = ["id"], // поле в родительской таблице
            childColumns = ["personId"], // поле в этой таблице, которое будёт знаечние из другой таблицы
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Positive::class,
            parentColumns = ["id"],
            childColumns = ["positiveId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ResumePositive(
    @ColumnInfo
        @PrimaryKey(autoGenerate = true)
            val id: Int? = null,
    @ColumnInfo
        val personId: Int? = null,
    @ColumnInfo
        val positiveId: Int? = null
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Person::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Negative::class,
            parentColumns = ["id"],
            childColumns = ["negativeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ResumeNegative(
    @ColumnInfo
        @PrimaryKey(autoGenerate = true)
            val id: Int? = null,
    @ColumnInfo
        val personId: Int? = null,
    @ColumnInfo
        val negativeId: Int? = null
)
