package com.example.caloriecounter.models

import com.example.caloriecounter.database.Entry

class DayScreenUIModel(
    val entries: List<UIEntry>,
    val limit: String,
    val eatenCalories: String,
    val leftCalories: String,
    val date: String,
    val progress: Int = 0,
    val isLimitExceed: Boolean = false
) {

    var dateDescription: String? = null
    var calculationText = "$limit -  $eatenCalories = $leftCalories"

}