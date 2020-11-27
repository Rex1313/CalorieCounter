package com.example.caloriecounter.base

data class CalorieCounterError(val errorMessage:String, val errorType: ErrorType) {
    enum class ErrorType{
        DIALOG,
        TEXTINPUT
    }
    var errorViewId: Int? = null
}