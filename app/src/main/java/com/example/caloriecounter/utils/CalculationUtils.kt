package com.example.caloriecounter.utils

import com.example.caloriecounter.database.Entry


class CalculationUtils {
    companion object {
        fun calculateEatenCalories(entries: List<Entry>): Int {
            return entries.sumBy { it.entryValue.toInt() }

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
            var newString = inputString
            val firstBracket = getFirstBracketPosition(newString)
            // If bracket found
            if(firstBracket != -1) {
                val bracketString = newString.slice(IntRange(firstBracket, findCorespondingBracketPosition(newString)))
                val equation = newString.slice(IntRange(firstBracket+1, findCorespondingBracketPosition(newString)-1))
                val calculation = calculateValueFromInput(equation).toString()
                newString = newString.replace(bracketString, calculation)
            }
            if (newString.contains("("))
                return calculateEquationsFromBrackets(newString)
            return newString
        }

        private fun getFirstBracketPosition(input: String):Int{
            return input.indexOfFirst { it.toString() == "(" }
        }

        private fun findCorespondingBracketPosition(input: String): Int {
            val positionOpeningBracket = getFirstBracketPosition(input)
            var level = 1
            for (i in positionOpeningBracket+1 until input.length) {
                when (input.get(i).toString()) {
                    ")" -> level--
                    "(" -> level++
                }
                if (level == 0) {
                    return i
                }
            }
            return -1
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