package com.example.rightperson.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rightperson.roomDB.DB
import com.example.rightperson.roomDB.Tables.Positive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class PositiveViewModel : ViewModel() {

    private var db: DB? = null

    // получаем доступ к ресурсам приложения и если доступ есть, устанавливаем соединение с бд
    fun initDB(context: WeakReference<Context>){
        if (context.get() != null){
            db = DB.getDatabase(context.get()!!)
        }
    }

    // реализуем функцию получения всех объектов класса (таблицы) Positive
    fun getPositive(): Flow<List<Positive>> = db?.getPositiveDao()?.getAllPositive() ?: flowOf(listOf())

    // реализуем функцию получения 1 элемента из всех объектов Positive
    fun getByIdPositive(id: Int): Flow<Positive?> = db?.getPositiveDao()?.getByIdPositive(id) ?: flowOf(null)

    // реализуем функцию вставки объекта в таблицу Positive
    fun insertPositive(item: Positive){
        viewModelScope.launch {
            db?.getPositiveDao()?.insert(item)
        }
    }

    // реализуем функцию обновления объекта в таблице Positive
    fun updatePositive(item: Positive){
        viewModelScope.launch {
            db?.getPositiveDao()?.update(item)
        }
    }

    // реализуем функцию удаления объекта таблицы Positive
    fun deletePositive(item: Positive){
        viewModelScope.launch {
            db?.getPositiveDao()?.delete(item)
        }
    }

}

