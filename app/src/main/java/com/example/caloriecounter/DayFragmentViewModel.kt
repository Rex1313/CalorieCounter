package com.example.caloriecounter

import androidx.lifecycle.MutableLiveData
import com.example.caloriecounter.base.BaseViewModel
import com.example.caloriecounter.database.Entry
import com.example.caloriecounter.models.DayScreenUIModel
import com.example.caloriecounter.utils.CalculationUtils
import kotlinx.coroutines.*

class DayFragmentViewModel() : BaseViewModel() {

    val entriesLiveData = MutableLiveData<List<Entry>>()
    val uiModelLiveData = MutableLiveData<DayScreenUIModel>()
    val wartosc = "Hello World"
    lateinit var dayDate: String
//
//    init {
//        runBlocking {
//            GlobalScope.async {
//                repository.getEntriesForDate(date)
//            }.await().let {
//                entriesLiveData.value = it
//            }
//
//        }
//    }


    suspend fun getData() =
        withContext(Dispatchers.IO) {
      println("daydate $dayDate")
            val entries = repository.getEntriesForDate(dayDate)
            val setting = repository.getDailySetting(dayDate)
            val eatenCalories = CalculationUtils.calculateEatenCalories(entries = entries)
            val leftCalories = CalculationUtils.calculateLeftCalories(
                entries = entries,
                limit = setting?.caloriesLimit?.toFloat() ?: 0f
            )
            withContext(Dispatchers.Main) {
                println("eaten calories $eatenCalories , left calories $leftCalories ${Thread.currentThread().name}")
                uiModelLiveData.value = DayScreenUIModel(
                    entries,
                    setting!!.caloriesLimit,
                    eatenCalories,
                    leftCalories
                )
            }
        }


    //Date format is YYYY-mm-DD
    private suspend fun getEntries(date: String) {
        val entries = GlobalScope.async(Dispatchers.IO) { repository.getEntriesForDate(date) }
        withContext(Dispatchers.Main) {
            entriesLiveData.value = entries.await()
        }

    }

    fun addNewEntry(inputCalories: String, inputName: String) {
        val calories = inputCalories.toFloat()
        val name = if (inputName.isEmpty()) null else {
            inputName
        }
        repository.addEntry(Entry(null, dayDate, calories, name))
    }

    suspend fun refreshData() = getData()

}