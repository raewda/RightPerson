package com.example.rightperson.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rightperson.R
import com.example.rightperson.roomDB.Tables.Positive
import com.example.rightperson.ui.theme.AppTypography
import com.example.rightperson.ui.theme.displayFontFamily
import com.example.rightperson.ui.theme.onPrimaryContainerLight
import com.example.rightperson.ui.theme.primaryContainerDarkHighContrast
import com.example.rightperson.vm.PositiveViewModel
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import java.lang.ref.WeakReference

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
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

    val hazeState = rememberHazeState()
    val gradientColors = listOf(onPrimaryContainerLight, primaryContainerDarkHighContrast)

    val dialogAddState = remember { mutableStateOf(false) }
    val dialogDeleteState = remember { mutableStateOf(false) }
    val dialogInfo = remember { mutableStateOf(false) }
    val newPositive = remember { mutableStateOf(TextFieldValue("")) }

    val positiveItem = remember { mutableStateOf(Positive()) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "POSITIVE QUALITIES",
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = gradientColors
                            )
                        ),
                        fontFamily = displayFontFamily,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .pointerInput(Unit){
                                detectTapGestures(
                                    onLongPress = {
                                        dialogInfo.value = !dialogInfo.value
                                    }
                                )
                            }
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
                    },
                actions = {
                    IconButton(
                        onClick = {
                            dialogAddState.value = !dialogAddState.value
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            "create",
                            tint = primaryContainerDarkHighContrast
                        )
                    }
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
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlowRow(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(0.9f)
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.Start,
                    verticalArrangement = Arrangement.Top,
                    maxItemsInEachRow = Int.MAX_VALUE 
                ) {
                    positiveList.value.forEach { item ->
                        Card(
                            modifier = Modifier
                                .padding(end = 5.dp, bottom = 10.dp)
                                .pointerInput(Unit){
                                    detectTapGestures(
                                        onLongPress = {
                                            dialogDeleteState.value = !dialogDeleteState.value
                                            positiveItem.value = item
                                        }
                                    )
                                }

                        ){
                            Text(
                                text = item.title,
                                modifier = Modifier
                                    .padding(horizontal = 7.dp, vertical = 3.dp),
                                style = AppTypography.headlineSmall
                            )
                        }
                    }
                }
            }

           if (dialogAddState.value){
               Dialog(
                   onDismissRequest = {
                       dialogAddState.value = false
                   }
               ) {
                   Card(
                       modifier = Modifier
                           .fillMaxWidth(0.7f)
                           .fillMaxHeight(0.2f),
                       shape = RoundedCornerShape(20.dp)
                   ) {
                       Column(
                           modifier = Modifier
                               .fillMaxSize(),
                           verticalArrangement = Arrangement.Center,
                           horizontalAlignment = Alignment.CenterHorizontally
                       ) {
                           OutlinedTextField(
                               value = newPositive.value,
                               onValueChange = {
                                   newPositive.value = it
                               },
                               modifier = Modifier
                                   .fillMaxWidth(0.9f)
                                   .padding(bottom = 20.dp),
                               shape = RoundedCornerShape(20.dp)
                           )
                           Button(
                               onClick = {
                                   positiveVM.insertPositive(
                                       Positive(title = newPositive.value.text)
                                   )
                                   dialogAddState.value = false
                               }
                           ) {
                               Text(
                                   "add positive"
                               )
                           }
                       }
                   }
               }
           }

            if (dialogDeleteState.value){
                Dialog(
                    onDismissRequest = {
                        dialogDeleteState.value = false
                    }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .fillMaxHeight(0.15f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "remove \"${
                                    positiveItem.value.title
                                }\"?"
                            )
                            Button(
                                onClick = {
                                    positiveVM.deletePositive(
                                        positiveItem.value
                                    )
                                    dialogDeleteState.value = false
                                }
                            ) {
                                Text(
                                    "remove positive"
                                )
                            }
                        }
                    }
                }
            }

            if (dialogInfo.value){
                Dialog(
                    onDismissRequest = {
                        dialogInfo.value = false
                    }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.3f)
                    ){
                       Column(
                           modifier = Modifier
                               .fillMaxSize()
                               .padding(horizontal = 10.dp)
                               .padding(vertical = 5.dp),
                           horizontalAlignment = Alignment.CenterHorizontally,
                           verticalArrangement = Arrangement.spacedBy(15.dp)
                       ) {
                           Text(
                               "Info",
                               style = TextStyle(
                                   brush = Brush.linearGradient(
                                       colors = gradientColors
                                   )
                               ),
                               fontSize = 28.sp,
                               modifier = Modifier
                                   .padding(vertical = 5.dp)
                           )
                           Text(
                               stringResource(R.string.positive_info),
                               softWrap = true
                           )
                       }

                    }
                }
            }
        }
    }
}