package com.example.caloriecounter
import androidx.lifecycle.MutableLiveData
import com.example.caloriecounter.base.BaseViewModel
import org.joda.time.LocalDate


class MainActivityViewModel : BaseViewModel() {
val loadData = MutableLiveData<String>()

    fun refreshDataWithDate(date:String){
        loadData.postValue(date)
    }


}