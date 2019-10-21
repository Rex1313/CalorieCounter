package com.example.caloriecounter.dayview

import androidx.lifecycle.MutableLiveData
import com.example.caloriecounter.R
import com.example.caloriecounter.base.BaseViewModel
import com.example.caloriecounter.base.ResourceProvider
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
    val entryLiveData = MutableLiveData<Entry>()
    val entryTypes = arrayListOf(
        EntryTypeModel().apply {
            type = EntryType.FOOD
        },
        EntryTypeModel().apply {
            type = EntryType.EXCERCISE
        }
    )

    suspend fun getData() =
        withContext(Dispatchers.IO) {
            val entries = repository.getEntriesForDate(dayDate)

            val setting = repository.getDailySetting(dayDate) ?: DailySetting(dayDate, 1500)
            val eatenCalories = CalculationUtils.calculateEatenCalories(entries = entries)
            val leftCalories = CalculationUtils.calculateLeftCalories(
                entries = entries,
                limit = setting.caloriesLimit.toFloat()
            )
            var dateDescription = LocalDate.parse(dayDate).toString(DateUtils.WEEKDAY_FORMAT)
            if (LocalDate.parse(dayDate).isEqual(LocalDate.now())) {
                dateDescription = "${ResourceProvider.getString(R.string.today)} ($dateDescription)"
            }
            if (LocalDate.parse(dayDate).isEqual(LocalDate.now().minusDays(1))) {
                dateDescription =
                    "${ResourceProvider.getString(R.string.yesterday)} ($dateDescription)"
            }

            withContext(Dispatchers.Main) {
                uiModelLiveData.value = DayScreenUIModel(
                    entries.map {
                        UIEntry(
                            it.entryName ?: EntryConstants.NAME_DEFAULT_VALUE,
                            "${it.entryValue.format(0)} ${ResourceProvider.getString(R.string.kcal)}",
                            it.id,
                            it.entryType
                        )
                    },
                    setting.caloriesLimit.toString(),
                    eatenCalories.toString(),
                    leftCalories.toString(),
                    LocalDate.parse(dayDate).toString(DateUtils.DATE_UI_FORMAT),
                    getProgress(setting.caloriesLimit.toString(), eatenCalories.toString()).toInt(),
                    isLimitExceeded(setting.caloriesLimit.toString(), eatenCalories.toString())
                ).apply { this.dateDescription = dateDescription }
            }
        }


    //Date format is YYYY-mm-DD
    private suspend fun getEntries(date: String) {
        val entries = GlobalScope.async(Dispatchers.IO) { repository.getEntriesForDate(date) }
        withContext(Dispatchers.Main) {
            entriesLiveData.value = entries.await()
        }

    }

    suspend fun addNewEntry(id: Int?, inputValue: String, inputName: String, entryType: String) {
        val value =
            if (entryType == EntryType.EXCERCISE.toString() && inputValue.toInt() > 0) -CalculationUtils.calculateValueFromInput(
                inputValue
            ) else {
                CalculationUtils.calculateValueFromInput(inputValue)
            }
        val name = if (inputName.isEmpty()) null else {
            inputName
        }
        repository.addEntry(Entry(id, dayDate, value, name, entryType))
    }

    suspend fun refreshData() = getData()

    suspend fun deleteEntryById(id: Int?) {
        repository.removeEntryById(id)
    }

    fun getEntryTypePosition(type: String): Int {
        return entryTypes.indexOfFirst { it.type.toString() == type }
    }

    suspend fun getEntryById(id: Int?) {
        withContext(Dispatchers.IO) {
            var entry = repository.getEntryById(id)
            withContext(Dispatchers.Main) {
                entryLiveData.value = entry
            }
        }
    }

    suspend fun editEntry(id: Int?, inputCalories: String, inputName: String, entryType: String) {
        val calories =
            if (entryType == EntryType.EXCERCISE.toString()) -CalculationUtils.calculateValueFromInput(
                inputCalories
            ) else {
                CalculationUtils.calculateValueFromInput(inputCalories)
            }
        val name = if (inputName.isEmpty()) null else {
            inputName
        }
        repository.editEntry(Entry(id, dayDate, calories, name, entryType))
    }

    private fun getProgress(limit: String, eatenCalories: String): Float {
        return (eatenCalories.toFloat() / limit.toFloat() * 100)
    }

    private fun isLimitExceeded(limit: String, eatenCalories: String): Boolean {
        return getProgress(limit, eatenCalories) > 100
    }
}