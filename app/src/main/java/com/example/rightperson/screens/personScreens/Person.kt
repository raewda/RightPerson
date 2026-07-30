package com.example.rightperson.screens.personScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rightperson.R
import com.example.rightperson.roomDB.Result
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
import java.lang.ref.WeakReference

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun Person(
    navController: NavHostController,
    person: MutableState<Boolean>,
    id: Int?
) {
    val personVM: PersonViewModel = viewModel()
    personVM.initDB(context = WeakReference(LocalContext.current))
    val personItem by personVM.getByIdPerson(id!!).collectAsState(null)

    val resumeNegativeVM: ResumeNegativeViewModel = viewModel()
    resumeNegativeVM.initDB(context = WeakReference(LocalContext.current))
    val negativeList by resumeNegativeVM.getNegativeByPersonId(id)
        .collectAsState(initial = emptyList())

    val resumePositiveVM: ResumePositiveViewModel = viewModel()
    resumePositiveVM.initDB(context = WeakReference(LocalContext.current))
    val positiveList by resumePositiveVM.getPositiveByPersonId(id)
        .collectAsState(initial = emptyList())

    val hazeState = rememberHazeState()
    val gradientColors = listOf(primaryContainerDarkHighContrast, onPrimaryContainerLight)

    val dialogUpdate = remember { mutableStateOf(false) }
    val dialogDelete = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    if (personItem != null) {
                        Text(
                            personItem!!.name!!,
                            style = TextStyle(
                                brush = Brush.linearGradient(
                                    colors = gradientColors
                                )
                            ),
                            fontFamily = displayFontFamily,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .clickable{
                                    navController.navigateUp()
                                }
                                .padding(vertical = 5.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = false
                        )
                    } else {
                        CircularProgressIndicator(
                            color = primaryContainerDarkHighContrast
                        )
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
                    },
                actions = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            "delete person",
                            tint = primaryContainerDarkHighContrast
                        )
                    }

                    IconButton(
                        onClick = {
                            dialogUpdate.value = !dialogUpdate.value
                        }
                    ) {
                        Icon(
                            Icons.Default.Create,
                            "update person",
                            tint = onPrimaryContainerLight
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (personItem != null) {
                Card(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .size(height = 200.dp, width = 260.dp),
                    shape = RoundedCornerShape(40.dp),
                    colors = CardDefaults.cardColors(Color.Transparent),
                    border = BorderStroke(
                        1.dp,
                        brush = Brush.linearGradient(
                            colors = gradientColors
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Image(
                            painter = if (personItem!!.result == Result.NeutralFlag) {
                                painterResource(R.drawable.neutral_flag)
                            } else if (personItem!!.result == Result.GreenFlag) {
                                painterResource(R.drawable.green_flag)
                            } else {
                                painterResource(R.drawable.red_flag)
                            },
                            contentDescription = "image flag",
                            modifier = Modifier
                                .size(100.dp)
                        )

                        personItem!!.percent?.let {
                            Text(
                                text = if (it >= (100 - personItem!!.percent!!)){
                                    personItem!!.percent.toString() + "%"
                                } else{
                                    (100 - personItem!!.percent!!).toString() + "%"
                                },
                                modifier = Modifier,
                                style = TextStyle(
                                    brush = Brush.linearGradient(
                                        colors = gradientColors
                                    )
                                ),
                                fontFamily = displayFontFamily,
                                fontSize = 50.sp
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(40.dp),
                    colors = CardDefaults.cardColors(primaryContainerDarkHighContrast)
                ) {
                    Text(
                        "POSITIVE QUALITIES",
                        color = onPrimaryContainerLight,
                        fontFamily = displayFontFamily,
                        fontSize = 26.sp,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
                FlowRow(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(250.dp)
                        .fillMaxWidth(0.9f)
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.Start,
                    verticalArrangement = Arrangement.Top,
                    maxItemsInEachRow = Int.MAX_VALUE
                ) {
                    positiveList.forEach { item ->
                        Card(
                            modifier = Modifier
                                .padding(end = 5.dp, bottom = 10.dp)
                        ) {
                            Text(
                                text = item.title!!,
                                modifier = Modifier
                                    .padding(
                                        horizontal = 7.dp,
                                        vertical = 3.dp
                                    ),
                                style = AppTypography.bodyMedium
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(40.dp),
                    colors = CardDefaults.cardColors(onPrimaryContainerLight)
                ) {
                    Text(
                        "NEGATIVE QUALITIES",
                        color = primaryContainerDarkHighContrast,
                        fontFamily = displayFontFamily,
                        fontSize = 26.sp,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }

                FlowRow(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(300.dp)
                        .fillMaxWidth(0.9f)
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.Start,
                    verticalArrangement = Arrangement.Top,
                    maxItemsInEachRow = Int.MAX_VALUE
                ) {
                    negativeList.forEach { item ->
                        Card(
                            modifier = Modifier
                                .padding(end = 5.dp, bottom = 10.dp)
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
            }
            else {
                CircularProgressIndicator(
                    color = primaryContainerDarkHighContrast
                )
            }
        }

        // for dialogUpdate
    }
}
