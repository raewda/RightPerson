package com.example.rightperson.screens.personScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rightperson.ui.theme.AppTypography
import com.example.rightperson.ui.theme.displayFontFamily
import com.example.rightperson.ui.theme.onPrimaryContainerLight
import com.example.rightperson.ui.theme.primaryContainerDarkHighContrast
import com.example.rightperson.vm.PersonViewModel
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import java.lang.ref.WeakReference

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun AllPersons(
    navController: NavHostController,
    allPersons: MutableState<Boolean>
) {
    val personVM: PersonViewModel = viewModel()
    personVM.initDB(context = WeakReference(LocalContext.current))
    val personList = personVM.getPerson().collectAsState(listOf())

    val hazeState = rememberHazeState()
    val gradientColors = listOf(onPrimaryContainerLight, primaryContainerDarkHighContrast)

    val dialogInfo = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ALL PERSONS",
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = gradientColors
                            )
                        ),
                        fontFamily = displayFontFamily,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .pointerInput(Unit) {
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
                    ) {
                        progressive =
                            HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0f)
                    },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate("addPerson")
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            if (personList.value.size != 0){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(personList.value) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .clickable{
                                    navController.navigate("person/${item.id}")
                                },
                            colors = CardDefaults.cardColors(onPrimaryContainerLight)
                        ) {
                            Text(
                                item.name!!,
                                color = primaryContainerDarkHighContrast
                            )
                        }
                    }
                }
            }
            else if (personList == null){
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = primaryContainerDarkHighContrast
                    )
                }
            }
            else{
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "NO PERSONS",
                        modifier = Modifier
                            .padding(horizontal = 7.dp, vertical = 3.dp),
                        style = AppTypography.headlineSmall,
                        color = primaryContainerDarkHighContrast
                    )
                }
            }
        }
    }
}

