package com.example.rightperson.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.rightperson.R
import com.example.rightperson.ui.theme.AppTypography
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun Home(
    navController : NavHostController,
    home : MutableState<Boolean>
){
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Text(
                "RAEWDA PRODUCTION",
                style = AppTypography.labelLarge
            )

            val composition by rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(R.raw.home)
            )

            LottieAnimation(
                composition = composition,
                modifier = Modifier
                    .fillMaxWidth(),
                isPlaying = true,
                iterations = LottieConstants.IterateForever
            )


        }
    }
}

