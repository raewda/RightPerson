package com.example.rightperson.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rightperson.roomDB.Tables.Positive
import com.example.rightperson.ui.theme.displayFontFamily
import com.example.rightperson.ui.theme.onPrimaryContainerLight
import com.example.rightperson.ui.theme.primaryContainerDarkHighContrast
import com.example.rightperson.vm.PositiveViewModel
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import java.lang.ref.WeakReference

@OptIn(ExperimentalMaterial3Api::class)
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
    val positiveList = positiveVM.getPositive().collectAsState(listOf())

//    LazyColumn{
//        items(positivelist.value) { item ->
//            Text(item.title)
//        }
//    }

    val hazeState = rememberHazeState()
    val gradientColors = listOf(onPrimaryContainerLight, primaryContainerDarkHighContrast)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "PROFILE",
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = gradientColors
                            )
                        ),
                        fontFamily = displayFontFamily,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                    )
                },
                expandedHeight = 40.dp,
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
                modifier = Modifier
                    .hazeSource(
                        hazeState,
                        zIndex = 1f
                    )
                    .hazeEffect(
                        hazeState,
                        style = HazeMaterials.ultraThin()
                    ){
                        progressive = HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0f)
                    }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Positive qualities",
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = gradientColors
                        )
                    ),
                    fontFamily = displayFontFamily,
                    fontSize = 36.sp,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

        }
    }

}