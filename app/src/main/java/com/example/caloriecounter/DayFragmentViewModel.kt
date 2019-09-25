package com.example.caloriecounter

import androidx.lifecycle.MutableLiveData
import com.example.caloriecounter.base.BaseViewModel
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.database.Entry
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class DayFragmentViewModel(val date:String) : BaseViewModel() {

    val entriesLiveData = MutableLiveData<List<Entry>>()

    init {
        runBlocking {
            GlobalScope.async {
                repository.getEntriesForDate(date)
            }.await().let {
                entriesLiveData.value = it
            }

        }
    }

    //Date format is YYYY-mm-DD
    fun getEntries(date: String) {
        runBlocking {
            GlobalScope.async {
                repository.getEntriesForDate(date)
            }.await().let {
                entriesLiveData.value = it
            }

        }
    }


}