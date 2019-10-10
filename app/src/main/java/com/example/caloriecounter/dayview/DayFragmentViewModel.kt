package com.example.caloriecounter.dayview

import androidx.lifecycle.MutableLiveData
import com.example.caloriecounter.base.BaseViewModel
import com.example.caloriecounter.base.format
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.database.Entry
import com.example.caloriecounter.database.EntryConstants
import com.example.caloriecounter.models.DayScreenUIModel
import com.example.caloriecounter.models.EntryType
import com.example.caloriecounter.models.EntryTypeModel
import com.example.caloriecounter.models.UIEntry
import com.example.caloriecounter.utils.CalculationUtils
import com.example.caloriecounter.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate

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
                LocalDate.parse(dayDate).toString(DateUtils.WEEKDAY_FORMAT)

            withContext(Dispatchers.Main) {
                uiModelLiveData.value = DayScreenUIModel(
                    entries.map {
                        UIEntry(
                            it.entryName ?: EntryConstants.NAME_DEFAULT_VALUE,
                            "${it.entryCalories.format(0)} kcal",
                            it.id,
                            it.entryType
                        )
                    },
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

    suspend fun addNewEntry(inputCalories: String, inputName: String, entryType: String) {
        val calories = if (entryType == EntryType.EXCERCISE.toString()) -CalculationUtils.calculateValueFromInput(inputCalories) else { CalculationUtils.calculateValueFromInput(inputCalories) }
        val name = if (inputName.isEmpty()) null else { inputName }
        repository.addEntry(Entry(null, dayDate, calories, name, entryType))


    }

    suspend fun refreshData() = getData()

    suspend fun deleteEntryById(id: Int?) {
        repository.removeEntryById(id)
    }

    fun getEntryTypes(): ArrayList<EntryTypeModel> {
        return arrayListOf(
            EntryTypeModel().apply {
                type = EntryType.FOOD
            },
            EntryTypeModel().apply {
                type = EntryType.EXCERCISE
            }
        )
    }

    suspend fun editEntry(id: Int?, inputCalories: String, inputName: String, entryType: String) {
        val calories = if (entryType == EntryType.EXCERCISE.toString()) -CalculationUtils.calculateValueFromInput(inputCalories) else { CalculationUtils.calculateValueFromInput(inputCalories) }
        val name = if (inputName.isEmpty()) null else { inputName }
        repository.editEntry(Entry(id, dayDate, calories, name, entryType))
    }
}