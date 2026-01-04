package com.example.rightperson.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rightperson.roomDB.DB
import com.example.rightperson.roomDB.Tables.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class PersonViewModel: ViewModel() {

    private var db: DB? = null

    fun initDB(context: WeakReference<Context>){
        if (context.get() != null){
            db = DB.getDatabase(context.get()!!)
        }
    }

    fun getPerson(): Flow<List<Person>> = db?.getPersonDao()?.getAllPerson() ?: flowOf(listOf())

    fun insertPerson(item: Person){
        viewModelScope.launch {
            db?.getPersonDao()?.insert(item)
        }
    }

    fun updatePerson(item: Person){
        viewModelScope.launch {
            db?.getPersonDao()?.update(item)
        }
    }

    fun deletePerson(item: Person){
        viewModelScope.launch {
            db?.getPersonDao()?.delete(item)
        }
    }
}