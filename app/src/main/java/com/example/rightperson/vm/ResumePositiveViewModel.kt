package com.example.rightperson.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rightperson.roomDB.DB
import com.example.rightperson.roomDB.Tables.Positive
import com.example.rightperson.roomDB.Tables.ResumePositive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class ResumePositiveViewModel: ViewModel() {

    private var db: DB? = null

    fun initDB(context: WeakReference<Context>){
        if (context.get() != null){
            db = DB.getDatabase(context.get()!!)
        }
    }

    fun getAllPersonResumePositive(personId: Int?): Flow<List<ResumePositive>> = db?.getResumePositiveDao()?.getAllPersonResume(personId) ?: flowOf(listOf())

    fun getPositiveByPersonId(personId: Int): Flow<List<Positive>> =
        db?.getResumePositiveDao()?.getPositiveByPersonId(personId) ?: flowOf(emptyList())

    fun insertResumePositive(item: ResumePositive){
        viewModelScope.launch {
            db?.getResumePositiveDao()?.insert(item)
        }
    }

    fun deleteResumePositive(item: ResumePositive){
        viewModelScope.launch {
            db?.getResumePositiveDao()?.delete(item)
        }
    }

}