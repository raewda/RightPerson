package com.example.rightperson.screens.personScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (personItem != null){

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
                        .padding(bottom = 50.dp)
                )

                Text(
                    "POSITIVE " + personItem!!.percent.toString() + "%"
                            +"\n" +
                            "NEGATIVE " + (100 - personItem!!.percent!!).toString() + "%",
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = gradientColors
                        )
                    ),
                    fontSize = 40.sp
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