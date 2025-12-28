package com.example.rightperson.screens.functions

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.rightperson.ui.theme.AppTypography
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Locale

@Composable
fun Greetings(){

    val time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    Text(
        text =
            when(time){
                in 4..11 -> "(Good Morning"
                in 12..16 -> "(Good Day"
                in 17..21 -> "(Good Evening"
                else -> "(Good Night"
            },
        style = AppTypography.headlineSmall,
        modifier = Modifier
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}
