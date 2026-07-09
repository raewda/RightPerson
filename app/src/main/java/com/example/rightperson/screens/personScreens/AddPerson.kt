package com.example.rightperson.screens.personScreens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rightperson.functions.CalculationResult
import com.example.rightperson.roomDB.Result
import com.example.rightperson.roomDB.Tables.Negative
import com.example.rightperson.roomDB.Tables.Person
import com.example.rightperson.roomDB.Tables.Positive
import com.example.rightperson.roomDB.Tables.ResumeNegative
import com.example.rightperson.roomDB.Tables.ResumePositive
import com.example.rightperson.ui.theme.AppTypography
import com.example.rightperson.ui.theme.displayFontFamily
import com.example.rightperson.ui.theme.onPrimaryContainerLight
import com.example.rightperson.ui.theme.primaryContainerDarkHighContrast
import com.example.rightperson.vm.NegativeViewModel
import com.example.rightperson.vm.PersonViewModel
import com.example.rightperson.vm.PositiveViewModel
import com.example.rightperson.vm.ResumeNegativeViewModel
import com.example.rightperson.vm.ResumePositiveViewModel
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun AddPerson(
    navController: NavHostController,
    addPerson: MutableState<Boolean>
) {
    val scope = rememberCoroutineScope()

    val personVM: PersonViewModel = viewModel()
    personVM.initDB(context = WeakReference(LocalContext.current))

    val positiveVM: PositiveViewModel = viewModel()
    positiveVM.initDB(context = WeakReference(LocalContext.current))
    val positiveList = positiveVM.getPositive().collectAsState(listOf())

    val negativeVM: NegativeViewModel = viewModel()
    negativeVM.initDB(context = WeakReference(LocalContext.current))
    val negativeList = negativeVM.getNegative().collectAsState(listOf())

    val hazeState = rememberHazeState()
    val gradientColors = listOf(primaryContainerDarkHighContrast, onPrimaryContainerLight)

    var negativeQualities = listOf<Negative>()
    var positiveQualities = listOf<Positive>()

    val personName = remember { mutableStateOf(TextFieldValue("")) }

    val dialogInfo = remember { mutableStateOf(false) }
    val dialogSelectPositive = remember { mutableStateOf(false) }
    val dialogSelectNegative = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "NEW PERSON",
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

                        IconButton(
                            onClick = {
                                dialogInfo.value = true
                            }
                        ) {
                            Icon(
                                Icons.Default.Info,
                                "info",
                                tint = onPrimaryContainerLight
                            )
                        }
                    }
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
                    ) {
                        progressive =
                            HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0f)
                    }
            )
        },
        floatingActionButton = {
            TextButton(
                onClick = {
                    if (personName.value.text.isNotBlank() &&
                        (positiveQualities.isNotEmpty() || negativeQualities.isNotEmpty())){
                        val res : Pair<Int, Result> = CalculationResult(
                            positiveQualities,
                            negativeQualities
                        )
                        Log.d("result", res.first.toString())

                        scope.launch(Dispatchers.Main) {
                            val id = personVM.insertPerson(
                                item = Person(
                                    name = personName.value.text,
                                    result = res.second,
                                    percent = res.first
                                )
                            )
                            navController.navigate("result/${id}")
                        }
                    }
                },
                border = BorderStroke(
                    1.dp,
                    brush = Brush.linearGradient(
                        colors = gradientColors
                    )
                )
            ) {
                Text(
                    "add person",
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = gradientColors
                        )
                    ),
                    fontSize = 16.sp,
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = personName.value,
                    onValueChange = {
                        personName.value = it
                    },
                    maxLines = 1,
                    shape = RoundedCornerShape(40.dp),
                    placeholder = {
                        Text(
                            "person\'s name"
                        )
                    },
                    label = {
                        Text(
                            "person\'s name"
                        )
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Person,
                            "person's name",
                            tint = onPrimaryContainerLight
                        )
                    },
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "POSITIVE QUALITIES",
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = gradientColors
                            )
                        ),
                        fontSize = 20.sp
                    )

                    IconButton(
                        onClick = {
                            dialogSelectPositive.value = true
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            "add positive qualities",
                            tint = onPrimaryContainerLight
                        )
                    }
                }

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.Start,
                    verticalArrangement = Arrangement.Top,
                    maxItemsInEachRow = Int.MAX_VALUE
                ) {
                    positiveQualities.forEach { item ->
                        Card(
                            modifier = Modifier
                                .padding(end = 5.dp, bottom = 10.dp)
                        ) {
                            Text(
                                text = item.title!!,
                                modifier = Modifier
                                    .padding(horizontal = 7.dp, vertical = 3.dp),
                                style = AppTypography.bodyLarge
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "NEGATIVE QUALITIES",
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = gradientColors
                            )
                        ),
                        fontSize = 20.sp
                    )

                    IconButton(
                        onClick = {
                            dialogSelectNegative.value = true
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            "add negative qualities",
                            tint = onPrimaryContainerLight
                        )
                    }
                }

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.Start,
                    verticalArrangement = Arrangement.Top,
                    maxItemsInEachRow = Int.MAX_VALUE
                ) {
                    negativeQualities.forEach { item ->
                        Card(
                            modifier = Modifier
                                .padding(end = 5.dp, bottom = 10.dp)
                        ) {
                            Text(
                                text = item.title!!,
                                modifier = Modifier
                                    .padding(horizontal = 7.dp, vertical = 3.dp),
                                style = AppTypography.bodyLarge
                            )
                        }
                    }
                }
            }

            if (dialogSelectPositive.value) {
                val selectedPositive = mutableListOf<Positive>()
                Dialog(
                    onDismissRequest = {
                        dialogSelectPositive.value = false
                    }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                "POSITIVE QUALITIES",
                                style = TextStyle(
                                    brush = Brush.linearGradient(
                                        colors = gradientColors
                                    )
                                ),
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )

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
                                    val selectedPositiveQuality = remember { mutableStateOf(false) }
                                    Card(
                                        modifier = Modifier
                                            .padding(end = 5.dp, bottom = 10.dp)
                                            .clickable {
                                                selectedPositiveQuality.value =
                                                    !selectedPositiveQuality.value
                                                if (selectedPositive.contains(item) && !selectedPositiveQuality.value) {
                                                    selectedPositive.remove(item)
                                                } else {
                                                    selectedPositive.add(item)
                                                }
                                            },
                                        shape = CircleShape,
                                        colors = CardDefaults.cardColors(
                                            if (selectedPositiveQuality.value) {
                                                onPrimaryContainerLight
                                            } else {
                                                Color.Transparent
                                            }
                                        )
                                    ) {
                                        Text(
                                            text = item.title!!,
                                            modifier = Modifier
                                                .padding(horizontal = 7.dp, vertical = 3.dp),
                                            style = AppTypography.bodyMedium
                                        )
                                    }
                                }
                            }

                            TextButton(
                                onClick = {
                                    positiveQualities = selectedPositive.toList()
                                    dialogSelectPositive.value = false
                                }
                            ) {
                                Text(
                                    "ADD QUALITIES",
                                    style = TextStyle(
                                        brush = Brush.linearGradient(
                                            colors = gradientColors
                                        )
                                    ),
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            if (dialogSelectNegative.value) {
                val selectedNegative = mutableListOf<Negative>()
                Dialog(
                    onDismissRequest = {
                        dialogSelectPositive.value = false
                    }
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                "NEGATIVE QUALITIES",
                                style = TextStyle(
                                    brush = Brush.linearGradient(
                                        colors = gradientColors
                                    )
                                ),
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )

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
                                negativeList.value.forEach { item ->
                                    val selectedNegativeQuality = remember { mutableStateOf(false) }
                                    Card(
                                        modifier = Modifier
                                            .padding(end = 5.dp, bottom = 10.dp)
                                            .clickable {
                                                selectedNegativeQuality.value =
                                                    !selectedNegativeQuality.value
                                                if (selectedNegative.contains(item) && !selectedNegativeQuality.value) {
                                                    selectedNegative.remove(item)
                                                } else {
                                                    selectedNegative.add(item)
                                                }
                                            },
                                        shape = CircleShape,
                                        colors = CardDefaults.cardColors(
                                            if (selectedNegativeQuality.value) {
                                                onPrimaryContainerLight
                                            } else {
                                                Color.Transparent
                                            }
                                        )
                                    ) {
                                        Text(
                                            text = item.title!!,
                                            modifier = Modifier
                                                .padding(horizontal = 7.dp, vertical = 3.dp),
                                            style = AppTypography.bodyMedium
                                        )
                                    }
                                }
                            }

                            TextButton(
                                onClick = {
                                    negativeQualities = selectedNegative.toList()
                                    dialogSelectNegative.value = false
                                }
                            ) {
                                Text(
                                    "ADD QUALITIES",
                                    style = TextStyle(
                                        brush = Brush.linearGradient(
                                            colors = gradientColors
                                        )
                                    ),
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}