package com.example.caloriecounter

import androidx.lifecycle.MutableLiveData
import com.example.caloriecounter.base.BaseViewModel
import com.example.caloriecounter.base.format
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.database.Entry
import com.example.caloriecounter.models.DayScreenUIModel
import com.example.caloriecounter.models.UIEntry
import com.example.caloriecounter.utils.CalculationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

class DayFragmentViewModel() : BaseViewModel() {

    val entriesLiveData = MutableLiveData<List<Entry>>()
    val uiModelLiveData = MutableLiveData<DayScreenUIModel>()
    lateinit var dayDate: String


    suspend fun getData() =
        withContext(Dispatchers.IO) {
            val entries = repository.getEntriesForDate(dayDate)
            val setting = repository.getDailySetting(dayDate) ?: DailySetting(dayDate, 1500)
            val eatenCalories = CalculationUtils.calculateEatenCalories(entries = entries)
            val leftCalories = CalculationUtils.calculateLeftCalories(
                entries = entries,
                limit = setting.caloriesLimit.toFloat()
            )
            val dateDescription =
            if(LocalDate.now()==LocalDate.parse(dayDate)) "Today"
            else if(LocalDate.now().minusDays(1) == LocalDate.parse(dayDate)) "Yesterday"
            else{""}

            withContext(Dispatchers.Main) {
                uiModelLiveData.value = DayScreenUIModel(
                    entries.map { UIEntry(it.entryName, it.entryCalories.format(0)) },
                    setting.caloriesLimit.toString(),
                    eatenCalories.toString(),
                    leftCalories.toString(), dayDate
                ).apply { this.dateDescription = "$dateDescription ($dayDate)" }
            }
        }


    //Date format is YYYY-mm-DD
    private suspend fun getEntries(date: String) {
        val entries = GlobalScope.async(Dispatchers.IO) { repository.getEntriesForDate(date) }
        withContext(Dispatchers.Main) {
            entriesLiveData.value = entries.await()
        }

    }

    suspend fun addNewEntry(inputCalories: String, inputName: String) {
        val calories = CalculationUtils.calculateValueFromInput(inputCalories)
        if (inputName.isEmpty()) {
            repository.addEntry(Entry(null, dayDate, calories))
        } else {
            repository.addEntry(Entry(null, dayDate, calories, inputName))
        }

    }

    suspend fun refreshData() = getData()

}