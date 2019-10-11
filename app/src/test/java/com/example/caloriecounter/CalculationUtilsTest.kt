package com.example.caloriecounter

import com.example.caloriecounter.utils.CalculationUtils
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CalculationUtilsTest {

    @Test
    fun multiplication_isCorrect() {
        val testMultiplication = "2*4.0"
        val value = CalculationUtils.calculateMultiplications(testMultiplication)
        assertThat("Value of 2*4 should be 8", value.toFloat() == 8F)
    }

    @Test
    fun addition_isCorrect() {
        val testAddition = "2+4"
        val value = CalculationUtils.calculateAdditions(testAddition)
        assertThat("Value of 2+4 should be 6", value.toFloat() == 6F)
    }

    @Test
    fun substraction_isCorrect() {
        val testSubstraction = "2-4"
        val value = CalculationUtils.calculateSubstractions(testSubstraction)
        assertThat("Value of 2-4 should be -2", value.toFloat() == -2F)
    }

    @Test
    fun calculation_complexCalculation() {
        val testCalculation = "2+2*2"
        val value = CalculationUtils.calculateValueFromInput(testCalculation)
        assertThat("Value of 2+2*2 should be 6", value == 6F)
    }
    @Test
    fun calculation_complexCalculationWithMultiplication() {
        val testCalculation = "2+2*2/2"
        val value = CalculationUtils.calculateValueFromInput(testCalculation)
        assertThat("Value of 2+2*2/2 should be 4", value == 4F)
    }

    @Test
    fun calculateComplexEquationWithBrackets(){
        val testCalculation = "(2/2+2)*2-(2*4)"
        val value = CalculationUtils.calculateValueFromInput(testCalculation)
        assertThat("Value for (2/2+2)*2-(2*4) should be -2", value==-2f)
    }

    @Test
    fun calculation_returnsSameValue() {
        val testCalculation = "240"
        val value = CalculationUtils.calculateValueFromInput(testCalculation)
        assertThat("Value of 240 after calculations should be still 240", value == 240F)
    }

    @Test
    fun calculation_bracketEquationsExtract(){
        val testValue = "(2+2)*2"
        val value = CalculationUtils.calculateEquationsFromBrackets(testValue)
        assertThat("Value from (2+2)* 2 should render as 4*2 ",value == "4.0*2")
    }

    @Test
    fun calculation_bracketEquationsExtractComplex(){
        val testValue = "(2/2+2)*2-(2*4)"
        val value = CalculationUtils.calculateEquationsFromBrackets(testValue)
        assertThat("Value from (2+2)* 2 should render as 3*2-8 ",value == "3.0*2-8.0")
    }

    @Test
    fun calculation_complexBracketCalculation(){
        val testValue = "(21-15) * 0.6 + 123.5 /2"
        val value = CalculationUtils.calculateValueFromInput(testValue)
        assertThat("Value from (21-15) * 0.6 + 123.5 /2 should be 65.35", value == 65.35F)
    }

    @Test
    fun calculation_nestedBracketCalculation(){
        val testValue = "((3-1)*15-39) * 0.5"
        val value = CalculationUtils.calculateValueFromInput(testValue)
        assertThat("Value from (21-15*(3-1)) * 0.5 should be -4.5", value == -4.5F)
    }

    @Test
    fun calculation_complexNestedBracketCalculation(){
        val testValue =  "((1.3*40)+20+(130*5) + 30 + (66*2))/2"
        val value = CalculationUtils.calculateValueFromInput(testValue)
        println("calculation is $value")
        assertThat("Value from  ((1.3*40)+20+(130*5) + 30 + (66*2))/2 should be 442", value == 442F)
    }



}