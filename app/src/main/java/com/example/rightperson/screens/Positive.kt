package com.example.rightperson.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rightperson.roomDB.Tables.Positive
import com.example.rightperson.vm.PositiveViewModel
import java.lang.ref.WeakReference

@Composable
fun Positive(
    navController: NavHostController,
    positive: MutableState<Boolean>,
) {
    // создаём объект вью-модели для работы с ней
    val positiveVM: PositiveViewModel = viewModel()
    // инициализуем базу данных и передаём ей контекст (все ресурсы) экрана
    positiveVM.initDB(context = WeakReference(LocalContext.current))
    // получаем данные - все объекты из таблицы Positive (из базы данных)
    val positivelist = positiveVM.getPositive().collectAsState(listOf())


    LazyColumn{
        items(positivelist.value) { item ->
            Text(item.title)
        }
    }

}