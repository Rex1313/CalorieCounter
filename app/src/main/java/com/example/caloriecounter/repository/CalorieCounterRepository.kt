package com.example.caloriecounter.repository

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.caloriecounter.database.CalorieCounterDatabase
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.database.DatabaseConstants
import com.example.caloriecounter.database.Entry

object CalorieCounterRepository {

    var db: CalorieCounterDatabase? = null
    fun initDatabase(app: Application) {
        db = Room.databaseBuilder(
            app,
            CalorieCounterDatabase::class.java,
            DatabaseConstants.DATABASE_NAME
        ).build()

    }


    fun initInMemoryDatabase(context: Context) {
        db = Room.inMemoryDatabaseBuilder(context, CalorieCounterDatabase::class.java).build()
    }

    fun addDailySetting(dailySetting: DailySetting) {
        db?.dailySettingsDao()?.insert(dailySetting)
    }

    fun getDailySetting(date: String): DailySetting? {
        return db?.dailySettingsDao()?.get(date)?.first()
    }

    // Date needs to be in format YYYY-mm-DD
    fun getEntriesForDate(date:String):List<Entry> {
        return db?.entriesDao()?.get(date)?: mutableListOf()
    }


}