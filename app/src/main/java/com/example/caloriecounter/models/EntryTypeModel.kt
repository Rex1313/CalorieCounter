package com.example.caloriecounter.models

class EntryTypeModel {
    var type:EntryType = EntryType.FOOD


    override fun toString(): String {
        return type.toString()
    }
}