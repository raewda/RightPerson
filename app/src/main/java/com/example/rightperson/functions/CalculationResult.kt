package com.example.rightperson.functions

import android.util.Log
import com.example.rightperson.roomDB.Result
import com.example.rightperson.roomDB.Tables.Negative
import com.example.rightperson.roomDB.Tables.Positive

fun CalculationResult(
    positiveQualities : List<Positive>,
    negativeQualities : List<Negative>
) : Pair<Int, Result> {
    val positive : Int = positiveQualities.count()
    val negative : Int = negativeQualities.count()
    val result: Result

    val sum = positive + negative
    val percentPositive = positive * 100 / sum
    Log.d("result", sum.toString())


    result = if (percentPositive > 50){
        Result.GreenFlag
    }
    else if(percentPositive < 50){
        Result.RedFlag
    }
    else{
        Result.NeutralFlag
    }

    Log.d("result", percentPositive.toString())
    return Pair(percentPositive, result)
}