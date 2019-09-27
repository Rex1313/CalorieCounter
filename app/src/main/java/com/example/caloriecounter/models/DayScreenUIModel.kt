package com.example.caloriecounter.models

import androidx.lifecycle.MutableLiveData
import com.example.caloriecounter.database.Entry
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class DayScreenUIModel(
    val entries: List<Entry>,
    val limit: Int,
    val eatenCalories: Int,
    val leftCalories: Int
) {


}