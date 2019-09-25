package com.example.caloriecounter

import androidx.lifecycle.MutableLiveData
import com.example.caloriecounter.base.BaseViewModel
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.database.Entry
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class DayFragmentViewModel : BaseViewModel() {

    val entriesLiveData = MutableLiveData<List<Entry>>()


    //Date format is YYYY-mm-DD
    fun getEntriesForDate(date: String) {
        runBlocking {
            GlobalScope.async {
                repository.getEntriesForDate(date)
            }.await().let {
                entriesLiveData.value = it
            }

        }
    }


}