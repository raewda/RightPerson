package com.example.rightperson.screens.personScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun UpdatePerson(
    navController: NavHostController,
    updatePerson: MutableState<Boolean>,
    id: Int?
) {
    LaunchedEffect(id) {
        if (id == null) {
            navController.navigateUp()
        }
    }

    val scope = rememberCoroutineScope()

    val personVM: PersonViewModel = viewModel()
    personVM.initDB(context = WeakReference(LocalContext.current))
    val personItem by personVM.getByIdPerson(id!!).collectAsState(null)

    val positiveVM: PositiveViewModel = viewModel()
    positiveVM.initDB(context = WeakReference(LocalContext.current))
    val positiveList = positiveVM.getPositive().collectAsState(listOf())

    val negativeVM: NegativeViewModel = viewModel()
    negativeVM.initDB(context = WeakReference(LocalContext.current))
    val negativeList = negativeVM.getNegative().collectAsState(listOf())

    val resumePositiveVM: ResumePositiveViewModel = viewModel()
    resumePositiveVM.initDB(context = WeakReference(LocalContext.current))
    val personPositive = resumePositiveVM.getPositiveByPersonId(id).collectAsState(listOf())
    val positiveResume = resumePositiveVM.getAllPersonResumePositive(id).collectAsState(listOf())

    val resumeNegativeVM: ResumeNegativeViewModel = viewModel()
    resumeNegativeVM.initDB(context = WeakReference(LocalContext.current))
    val personNegative = resumeNegativeVM.getNegativeByPersonId(id).collectAsState(listOf())
    val negativeResume = resumeNegativeVM.getAllPersonResumeNegative(id).collectAsState(listOf())

    val hazeState = rememberHazeState()
    val gradientColors = listOf(primaryContainerDarkHighContrast, onPrimaryContainerLight)

    val positiveQualities = remember { mutableStateListOf<Positive>() }
    val negativeQualities = remember { mutableStateListOf<Negative>() }

    LaunchedEffect(personPositive.value) {
        positiveQualities.clear()
        positiveQualities.addAll(personPositive.value)
    }

    LaunchedEffect(personNegative.value) {
        negativeQualities.clear()
        negativeQualities.addAll(personNegative.value)
    }

    if (personItem == null) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val personName = remember(personItem) {
        mutableStateOf(TextFieldValue(personItem!!.name ?: ""))
    }

    val dialogInfo = remember { mutableStateOf(false) }
    val dialogSelectPositive = remember { mutableStateOf(false) }
    val dialogSelectNegative = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (personItem != null) {
                            Text(
                                "UPDATE ${personItem!!.name}",
                                style = TextStyle(
                                    brush = Brush.linearGradient(
                                        colors = gradientColors
                                    )
                                ),
                                fontFamily = displayFontFamily,
                                fontSize = 32.sp,
                                modifier = Modifier
                                    .padding(vertical = 5.dp)
                                    .clickable {
                                        navController.navigateUp()
                                    })
                        } else {
                            CircularProgressIndicator()
                        }

                        IconButton(
                            onClick = {
                                dialogInfo.value = true
                            }) {
                            Icon(
                                Icons.Default.Info, "info", tint = onPrimaryContainerLight
                            )
                        }
                    }
                },
                expandedHeight = 40.dp,
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
                modifier = Modifier
                    .hazeSource(
                        hazeState, zIndex = 1f
                    )
                    .hazeEffect(
                        hazeState, style = HazeMaterials.ultraThin()
                    ) {
                        progressive =
                            HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0f)
                    })
        },
        floatingActionButton = {
            TextButton(
                onClick = {
                    if (personName.value.text.isNotBlank() && (positiveQualities.isNotEmpty() || negativeQualities.isNotEmpty())) {
                        val res: Pair<Int, Result> = CalculationResult(
                            positiveQualities, negativeQualities
                        )

                        scope.launch(Dispatchers.Main) {

                            personVM.updatePerson(
                                item = Person(
                                    id = id,
                                    name = personName.value.text,
                                    result = res.second,
                                    percent = res.first
                                )
                            )

                            positiveResume.value.forEach { item ->
                                resumePositiveVM.deleteResumePositive(item)
                            }

                            positiveQualities.forEach { item ->
                                resumePositiveVM.insertResumePositive(
                                    item = ResumePositive(
                                        personId = id, positiveId = item.id
                                    )
                                )
                            }

                            negativeResume.value.forEach { item ->
                                resumeNegativeVM.deleteResumeNegative(item)
                            }

                            negativeQualities.forEach { item ->
                                resumeNegativeVM.insertResumeNegative(
                                    item = ResumeNegative(
                                        personId = id, negativeId = item.id
                                    )
                                )
                            }
                            navController.navigate("result/${id}")
                        }
                    }
                }, border = BorderStroke(
                    1.dp, brush = Brush.linearGradient(
                        colors = gradientColors
                    )
                )
            ) {
                Text(
                    "update person",
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = gradientColors
                        )
                    ),
                    fontSize = 16.sp,
                )
            }
        }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
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
                            Icons.Default.Person, "person's name", tint = onPrimaryContainerLight
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
                        "POSITIVE QUALITIES", style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = gradientColors
                            )
                        ), fontSize = 20.sp
                    )

                    IconButton(
                        onClick = {
                            dialogSelectPositive.value = true
                        }) {
                        Icon(
                            Icons.Default.Create,
                            "update positive qualities",
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
                            modifier = Modifier.padding(end = 5.dp, bottom = 10.dp)
                        ) {
                            Text(
                                text = item.title!!,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
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
                        "NEGATIVE QUALITIES", style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = gradientColors
                            )
                        ), fontSize = 20.sp
                    )

                    IconButton(
                        onClick = {
                            dialogSelectNegative.value = true
                        }) {
                        Icon(
                            Icons.Default.Create,
                            "update negative qualities",
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
                            modifier = Modifier.padding(end = 5.dp, bottom = 10.dp)
                        ) {
                            Text(
                                text = item.title!!,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                                style = AppTypography.bodyLarge
                            )
                        }
                    }
                }

                if (dialogSelectPositive.value) {
                    Dialog(
                        onDismissRequest = {
                            dialogSelectPositive.value = false
                        }
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight()
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
                                        .fillMaxWidth()
                                        .padding(bottom = 10.dp),
                                    textAlign = TextAlign.Center
                                )

                                FlowRow(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth(0.9f)
                                        .verticalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalArrangement = Arrangement.Top,
                                    maxItemsInEachRow = Int.MAX_VALUE
                                ) {
                                    positiveList.value.forEach { item ->
                                        val selectedPositiveQuality =
                                            remember { mutableStateOf(false) }

                                        if (positiveQualities.contains(item)){
                                            selectedPositiveQuality.value = true
                                        }

                                        Card(
                                            modifier = Modifier
                                                .padding(end = 5.dp, bottom = 10.dp)
                                                .clickable {
                                                    selectedPositiveQuality.value =
                                                        !selectedPositiveQuality.value
                                                    if (positiveQualities.contains(item) && !selectedPositiveQuality.value) {
                                                        positiveQualities.remove(item)
                                                    } else {
                                                        positiveQualities.add(item)
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
                                        dialogSelectPositive.value = false
                                    }
                                ) {
                                    Text(
                                        "UPDATE QUALITIES",
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
                    Dialog(
                        onDismissRequest = {
                            dialogSelectPositive.value = false
                        }
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight()
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
                                        .fillMaxWidth()
                                        .padding(bottom = 10.dp),
                                    textAlign = TextAlign.Center
                                )

                                FlowRow(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth(0.9f)
                                        .verticalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalArrangement = Arrangement.Top,
                                    maxItemsInEachRow = Int.MAX_VALUE
                                ) {
                                    negativeList.value.forEach { item ->
                                        val selectedNegativeQuality =
                                            remember { mutableStateOf(false) }

                                        if (negativeQualities.contains(item)) {
                                            selectedNegativeQuality.value = true
                                        }

                                        Card(
                                            modifier = Modifier
                                                .padding(end = 5.dp, bottom = 10.dp)
                                                .clickable {
                                                    selectedNegativeQuality.value =
                                                        !selectedNegativeQuality.value
                                                    if (negativeQualities.contains(item) && !selectedNegativeQuality.value) {
                                                        negativeQualities.remove(item)
                                                    } else {
                                                        negativeQualities.add(item)
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
                                        dialogSelectNegative.value = false
                                    }
                                ) {
                                    Text(
                                        "UPDATE QUALITIES",
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
}