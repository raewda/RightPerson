package com.example.rightperson.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rightperson.roomDB.DB
import com.example.rightperson.roomDB.Tables.Negative
import com.example.rightperson.roomDB.Tables.Positive
import com.example.rightperson.roomDB.Tables.ResumeNegative
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class ResumeNegativeViewModel : ViewModel() {
    private var db: DB? = null

    fun initDB(context: WeakReference<Context>){
        if (context.get() != null){
            db = DB.getDatabase(context.get()!!)
        }
    }

    fun getAllPersonResumeNegative(personId: Int?): Flow<List<ResumeNegative>> = db?.getResumeNegativeDao()?.getAllPersonResume(personId) ?: flowOf(listOf())

    fun getNegativeByPersonId(personId: Int): Flow<List<Negative>> =
        db?.getResumeNegativeDao()?.getNegativeByPersonId(personId) ?: flowOf(emptyList())

    fun insertResumeNegative(item: ResumeNegative){
        viewModelScope.launch {
            db?.getResumeNegativeDao()?.insert(item)
        }
    }

    fun deleteResumeNegative(item: ResumeNegative){
        viewModelScope.launch {
            db?.getResumeNegativeDao()?.delete(item)
        }
    }
}