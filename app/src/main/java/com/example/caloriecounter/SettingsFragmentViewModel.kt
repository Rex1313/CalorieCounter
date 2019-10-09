package com.example.caloriecounter

import androidx.lifecycle.MutableLiveData
import com.example.caloriecounter.base.BaseViewModel
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.models.SettingsUIModel
import com.example.caloriecounter.utils.DateUtils
import org.joda.time.LocalDate


class SettingsFragmentViewModel : BaseViewModel() {

    val uiModelLiveData = MutableLiveData<SettingsUIModel>()


    suspend fun addDailySetting(calorieLimit: String) {
        val startDate = LocalDate.now().toString(DateUtils.DB_DATE_FORMAT)
        repository.addDailySetting(DailySetting(startDate, calorieLimit.toInt()))
    }


    fun addUsername(username: String) {
       // SharedPreferencesUtils.setString('name')
    }

    suspend fun getData() {
        val today = LocalDate.now().toString(DateUtils.DB_DATE_FORMAT)
        val calorieLimit = repository.getDailySetting(today)?.caloriesLimit.toString()
        val username = SharedPreferencesUtils.USER_NAME
        uiModelLiveData.value = SettingsUIModel(calorieLimit, username)
    }
}
