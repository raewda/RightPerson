package com.example.rightperson.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rightperson.roomDB.DB
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

    fun getAllResumeNegative(): Flow<List<ResumeNegative>> = db?.getResumeNegativeDao()?.getAllResume() ?: flowOf(listOf())

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