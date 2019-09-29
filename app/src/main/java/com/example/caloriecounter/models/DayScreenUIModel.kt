package com.example.caloriecounter.models

import com.example.caloriecounter.database.Entry

class DayScreenUIModel(
    val entries: List<UIEntry>,
    val limit: String,
    val eatenCalories: String,
    val leftCalories: String
) {


}