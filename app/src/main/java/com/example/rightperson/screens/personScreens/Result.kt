package com.example.rightperson.screens.personScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rightperson.R
import com.example.rightperson.roomDB.Result
import com.example.rightperson.ui.theme.onPrimaryContainerLight
import com.example.rightperson.ui.theme.primaryContainerDarkHighContrast
import com.example.rightperson.vm.PersonViewModel
import java.lang.ref.WeakReference

@Composable
fun Result(
    navController : NavHostController,
    result : MutableState<Boolean>,
    id: Int?
){
    val personVM : PersonViewModel = viewModel()
    personVM.initDB(context = WeakReference(LocalContext.current))
    val personItem by personVM.getByIdPerson(id!!).collectAsState(null)

    val gradientColors = listOf(onPrimaryContainerLight, primaryContainerDarkHighContrast)
    val gradientReverseColors = listOf(primaryContainerDarkHighContrast, onPrimaryContainerLight)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            if (personItem != null){
                Text(
                    personItem!!.name!!,
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = gradientColors
                        )
                    ),
                    fontSize = 30.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable{
                            navController.navigate("allPersons")
                        }
                        .padding(top = 30.dp),
                    textAlign = TextAlign.Center
                )
            }
            else{
                CircularProgressIndicator(
                    color = primaryContainerDarkHighContrast
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (personItem != null){

                Card(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .size(height = 270.dp, width = 300.dp),
                    shape = RoundedCornerShape(40.dp),
                    colors = CardDefaults.cardColors(Color.Transparent),
                    border = BorderStroke(
                        1.dp,
                        brush = Brush.linearGradient(
                            colors = gradientColors
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
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
                                .size(150.dp)
                                .padding(bottom = 30.dp)
                        )

                        Text(
                            "POSITIVE - " + personItem!!.percent.toString() + "%"
                                    +"\n" +
                                    "NEGATIVE - " + (100 - personItem!!.percent!!).toString() + "%",
                            style = TextStyle(
                                brush = Brush.linearGradient(
                                    colors = gradientColors
                                )
                            ),
                            fontSize = 30.sp
                        )
                    }
                }

                Text(
                    if (personItem!!.result == Result.GreenFlag) {
                        "The person predominantly meets your criteria for healthy and comfortable communication. Positive qualities significantly outweigh the negative ones."
                    } else if (personItem!!.result == Result.NeutralFlag) {
                        "The situation is ambiguous — positive and negative traits are balanced. The person requires further observation to make a conclusion."
                    } else {
                        "Negative qualities significantly outweigh the positive ones. The person exhibits traits you consider unacceptable in relationships — it's worth reconsidering safety and boundaries."
                    },
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = gradientReverseColors
                        )
                    ),
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            else{
                CircularProgressIndicator(
                    color = primaryContainerDarkHighContrast
                )
            }
        }
    }
}