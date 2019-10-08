package com.example.caloriecounter

import android.content.Context
import com.example.caloriecounter.base.BaseViewModel
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.utils.DateUtils
import org.joda.time.LocalDate


class SplashScreenFragmentViewModel : BaseViewModel() {



    fun getWelcomeScreenViewed(context: Context):Boolean{
        return SharedPreferencesUtils.getBoolean(SharedPreferencesUtils.WELCOME_SCREEN_SHOWN, context)
    }

    fun setWelcomeScreenViewed(context: Context){
        SharedPreferencesUtils.setBoolean(SharedPreferencesUtils.WELCOME_SCREEN_SHOWN, true, context)
    }
}
