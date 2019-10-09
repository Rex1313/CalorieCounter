package com.example.caloriecounter

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.caloriecounter.base.BaseViewModel
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.models.SettingsUIModel
import com.example.caloriecounter.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate


class SettingsFragmentViewModel : BaseViewModel() {

    val uiModelLiveData = MutableLiveData<SettingsUIModel>()


    suspend fun addDailySetting(calorieLimit: String) {
        val startDate = LocalDate.now().toString(DateUtils.DB_DATE_FORMAT)
        repository.addDailySetting(DailySetting(startDate, calorieLimit.toInt()))
    }


    fun addUsername(username: String, context: Context) {
        SharedPreferencesUtils.setString("username", username, context)
    }

    suspend fun getData(context: Context) =
        withContext(Dispatchers.IO) {
            val today = LocalDate.now().toString(DateUtils.DB_DATE_FORMAT)
            val calorieLimit = repository.getDailySetting(today)?.caloriesLimit.toString()
            val username = SharedPreferencesUtils.getString("username", context)
            withContext(Dispatchers.Main) {

                uiModelLiveData.value = SettingsUIModel(calorieLimit, username)
            }
        }
}
