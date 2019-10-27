package com.example.caloriecounter

import androidx.lifecycle.MutableLiveData
import com.example.caloriecounter.base.BaseViewModel


class MainActivityViewModel : BaseViewModel() {
    var newEntryRequest = false
    val loadData = MutableLiveData<String>()
    fun refreshDataWithDate(date: String) {
        loadData.postValue(date)
    }


    fun showNewEntryFromWidget() {
        newEntryRequest = true
    }

}