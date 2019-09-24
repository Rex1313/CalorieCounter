package com.example.caloriecounter.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DailySettingsDao {


    @Query("SELECT * FROM daily_settings where date(start_date) <= date(:date)  ORDER BY start_date DESC LIMIT 1")
    fun get(date: String): List<DailySetting>


    @Query("SELECT * FROM daily_settings")
    fun getAll():List<DailySetting>

    @Insert()
    fun insert(dailySetting: DailySetting)


    @Insert()
    fun insertAll(dailySetting: List<DailySetting>)

}