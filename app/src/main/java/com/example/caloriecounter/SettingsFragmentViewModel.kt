package com.example.caloriecounter

import com.example.caloriecounter.base.BaseViewModel
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.utils.DateUtils
import org.joda.time.LocalDate


class SettingsFragmentViewModel : BaseViewModel() {

    suspend fun addDailySetting(calorieLimit: String) {
        val startDate = LocalDate.now().toString(DateUtils.DB_DATE_FORMAT)
        repository.addDailySetting(DailySetting(startDate, calorieLimit.toInt()))
    }


    fun addUsername(username: String) {

    }
}
