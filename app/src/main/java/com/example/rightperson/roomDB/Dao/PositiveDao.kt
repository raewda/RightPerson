package com.example.rightperson.roomDB.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.rightperson.roomDB.Tables.Positive
import kotlinx.coroutines.flow.Flow

// Dao - Data Access Object - интерфейс для манипуляции данными в таблице (только для Room)
@Dao
interface PositiveDao {
    @Query("SELECT * FROM Positive") // сам запрос (при вызове функции cработает запрос)
    fun getAllPositive(): Flow<List<Positive>> // Flow - параллельно изменениям отображает их (возвращает список всех объектов класса - если появится новый объект, то он тут же его вернёт)

    @Insert // вставка в таблицу
    suspend fun insert(item: Positive) // передаём для вставки экземпляр класса

    @Update(onConflict = REPLACE) // обновление как замена записи
    suspend fun update(item: Positive)

    @Delete // удаление из таблицы
    suspend fun delete(item: Positive)
}