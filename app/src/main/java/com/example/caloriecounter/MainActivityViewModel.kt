package com.example.caloriecounter

import androidx.lifecycle.MutableLiveData
import com.example.caloriecounter.base.BaseViewModel
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.database.Entry

class MainActivityViewModel : BaseViewModel() {

    val dailySettingsLiveData = MutableLiveData<DailySetting>()
    val entriesLiveData = MutableLiveData<List<Entry>>()


    fun getData() {
        val dailySettings = repository.getDailySetting("19998-12-12")
        dailySettingsLiveData.value = dailySettings
    }


    fun getEntries() {
        val entries = mutableListOf(
            Entry(null, 100f, "tofu"),
            Entry(null, 200f, "sausage")
        )
        entriesLiveData.value = entries
    }
}