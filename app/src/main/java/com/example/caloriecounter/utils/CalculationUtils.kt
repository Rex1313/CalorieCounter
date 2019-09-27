package com.example.caloriecounter.utils

import com.example.caloriecounter.database.Entry


class CalculationUtils {
    companion object {
        fun calculateEatenCalories(entries: List<Entry>): Int {
            return entries.sumBy { it.entryCalories.toInt() }

        }

        fun calculateLeftCalories(entries: List<Entry>, limit: Float): Int {
            return limit.toInt() - calculateEatenCalories(entries)

        }
    }

}