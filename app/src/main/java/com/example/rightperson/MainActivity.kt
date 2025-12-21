package com.example.rightperson

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rightperson.screens.Home
import com.example.rightperson.screens.Negative
import com.example.rightperson.screens.Positive
import com.example.rightperson.ui.theme.RightPersonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RightPersonTheme {
                val navController = rememberNavController()
                val home = remember { mutableStateOf(false) }
                val positive = remember { mutableStateOf(false) }
                val negative = remember { mutableStateOf(false) }

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ){
                    composable("home"){
                        Home(
                            navController,
                            home
                        )
                    }

                    composable("positive"){
                        Positive(
                            navController,
                            positive
                        )
                    }

                    composable("negative"){
                        Negative(
                            navController,
                            negative
                        )
                    }
                }
            }
        }
    }
}
