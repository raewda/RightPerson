package com.example.rightperson.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rightperson.R
import com.example.rightperson.ui.theme.AppTypography
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.rightperson.ui.theme.displayFontFamily
import com.example.rightperson.ui.theme.onPrimaryContainerLight
import com.example.rightperson.ui.theme.onTertiaryContainerDark
import com.example.rightperson.ui.theme.primaryContainerDarkHighContrast
import com.example.rightperson.ui.theme.primaryContainerDarkMediumContrast
import kotlinx.coroutines.launch

@Composable
fun Home(
    navController : NavHostController,
    home : MutableState<Boolean>
){
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.home)
    )
    val gradientColors = listOf(onPrimaryContainerLight, primaryContainerDarkHighContrast)
    val reverseGradientColors = listOf(primaryContainerDarkHighContrast, onPrimaryContainerLight)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl,
        content = {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerContainerColor = Color.Transparent,

                        ) {
                        Column(
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            TextButton(
                                onClick = {
                                    navController.navigate("positive")
                                }
                            ){
                                Text(
                                    "Positive",
                                    style = AppTypography.headlineLarge
                                )
                            }

                            TextButton(
                                onClick = {
                                    navController.navigate("negative")
                                }
                            ){
                                Text(
                                    "Negative",
                                    style = AppTypography.headlineLarge,
                                )
                            }
                        }
                    }
                },
            ) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "menu",
                                    modifier = Modifier
                                        .size(40.dp)
                                )
                            }
                            Text(
                                "RAEWDA PRODUCTION",
                                style = AppTypography.titleSmall
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {

                            Text(
                                text = "Right Person",
                                style = TextStyle(
                                    brush = Brush.linearGradient(
                                        colors = gradientColors
                                    )
                                ),
                                fontFamily = displayFontFamily,
                                fontSize = 48.sp,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "for you",
                                style = TextStyle(
                                    brush = Brush.linearGradient(
                                        colors = reverseGradientColors
                                    )
                                ),
                                fontSize = 30.sp,
                                modifier = Modifier
                                    .padding(start = 55.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Right
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
            }
        }
    )

}

