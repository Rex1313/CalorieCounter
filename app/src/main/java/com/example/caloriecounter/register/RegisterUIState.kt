package com.example.caloriecounter.register

data class RegisterUIState(val state:State){
    enum class State{
        DEFAULT,
        ERROR,
        WAIT,
        SUCCESS
    }
    var errorMessage:String? = null
}