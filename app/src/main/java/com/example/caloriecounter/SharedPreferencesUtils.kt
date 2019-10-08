package com.example.caloriecounter

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtils {

    companion object {
        const val WELCOME_SCREEN_SHOWN = "welcome_screen_shown"
        const val USER_NAME = "user_name"
        const val SHARED_PREFERENCES_UNIQUE_NAME = "CalorieCounter"

        fun getBoolean(setting: String, context: Context): Boolean {
            return context.getSharedPreferences(
                SHARED_PREFERENCES_UNIQUE_NAME,
                Context.MODE_PRIVATE
            )
                .getBoolean(setting, false)
        }

        fun setBoolean(setting: String, value: Boolean, context: Context) {
            context.getSharedPreferences(SHARED_PREFERENCES_UNIQUE_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean(setting, value).apply()
        }


        fun getString(setting: String, context: Context): String {
            return context.getSharedPreferences(
                SHARED_PREFERENCES_UNIQUE_NAME,
                Context.MODE_PRIVATE
            )
                .getString(setting, "")
        }

        fun setString(setting: String, value: String, context: Context) {
            context.getSharedPreferences(SHARED_PREFERENCES_UNIQUE_NAME, Context.MODE_PRIVATE)
                .edit().putString(setting, value).apply()
        }
    }
}