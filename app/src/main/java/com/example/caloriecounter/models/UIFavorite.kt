package com.example.caloriecounter.models

class UIFavorite(
    val name: String,
    val calories: String,
    val id: Int?,
    val entryType: String
){

    override fun toString(): String {
        return "$name ($calories kcal)"
    }
}




