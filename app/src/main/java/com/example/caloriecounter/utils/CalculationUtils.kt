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


        fun calculateValueFromInput(inputString: String): Float {
            var calculatedString = inputString.replace(Regex("\\s+"), "")
            calculatedString = calculateEquationsFromBrackets(calculatedString)
            calculatedString = calculateMultiplications(calculatedString)
            calculatedString = calculateDivisions(calculatedString)
            calculatedString = calculateAdditions(calculatedString)
            calculatedString = calculateSubstractions(calculatedString)
            return calculatedString.toFloat()
        }


        fun calculateEquationsFromBrackets(inputString: String): String {
            val bracketEquations = "(\\((.+?)\\))".toRegex()
            val match = bracketEquations.findAll(inputString)
            var newString = inputString
            match.iterator().forEach {
                val (first, second) = it.destructured
                val calculation = calculateValueFromInput(second).toString()
                newString = newString.replace(first, calculation)
            }
            if (newString.contains("("))
                return calculateEquationsFromBrackets(newString)
            return newString
        }


        fun calculateSubstractions(inputString: String): String {
            val multiplicationRegex = "((-?\\d+(\\.\\d+)?)([-])(-?\\d+(\\.\\d+)?))".toRegex()
            val match = multiplicationRegex.findAll(inputString)
            var newString = inputString
            match.iterator().forEach {
                val (first, second, third, fourth, fifth) = it.destructured
                val calculation = second.toFloat() - fifth.toFloat()
                newString = newString.replace(first, calculation.toString())
            }
            if (newString.contains("-"))
                return calculateAdditions(newString)
            return newString
        }

        fun calculateAdditions(inputString: String): String {
            val multiplicationRegex = "((-?\\d+(\\.\\d+)?)([+])(-?\\d+(\\.\\d+)?))".toRegex()
            val match = multiplicationRegex.findAll(inputString)
            var newString = inputString
            match.iterator().forEach {
                val (first, second, third, fourth, fifth) = it.destructured
                val calculation = second.toFloat() + fifth.toFloat()
                newString = newString.replace(first, calculation.toString())
            }
            if (newString.contains("+"))
                return calculateAdditions(newString)
            return newString
        }

        fun calculateDivisions(inputString: String): String {
            val multiplicationRegex =
                "((-?\\d+(\\.\\d+)?)([\\\\||\\/||:])(-?\\d+(\\.\\d+)?))".toRegex()
            val match = multiplicationRegex.findAll(inputString)
            var newString = inputString
            match.iterator().forEach {
                val (first, second, third, fourth, fifth) = it.destructured
                val calculation = second.toFloat() / fifth.toFloat()
                newString = newString.replace(first, calculation.toString())
            }
            if (newString.contains("/"))
                return calculateDivisions(newString)
            return newString
        }

        fun calculateMultiplications(inputString: String): String {
            val multiplicationRegex = "((-?\\d+(\\.\\d+)?)([*])(-?\\d+(\\.\\d+)?))".toRegex()
            val match = multiplicationRegex.findAll(inputString)
            var newString = inputString
            match.iterator().forEach {
                val (first, second, third, fourth, fifth) = it.destructured
                val calculation = second.toFloat() * fifth.toFloat()
                newString = newString.replace(first, calculation.toString())
            }
            if (newString.contains("*"))
                return calculateMultiplications(newString)
            return newString
        }
    }

}