package com.example.rightperson.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rightperson.roomDB.DB
import com.example.rightperson.roomDB.Tables.Negative
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class NegativeViewModel : ViewModel(){

    private var db: DB? = null

    fun initDB(context: WeakReference<Context>){
        if (context.get() != null){
            db = DB.getDatabase(context.get()!!)
        }
    }

    fun getNegative(): Flow<List<Negative>> = db?.getNegativeDao()?.getAllNegative() ?: flowOf(listOf())

    fun insertNegative(item: Negative){
        viewModelScope.launch {
            db?.getNegativeDao()?.insert(item)
        }
    }

    fun updateNegative(item: Negative){
        viewModelScope.launch {
            db?.getNegativeDao()?.update(item)
        }
    }

    fun deleteNegative(item: Negative){
        viewModelScope.launch {
            db?.getNegativeDao()?.delete(item)
        }
    }

}