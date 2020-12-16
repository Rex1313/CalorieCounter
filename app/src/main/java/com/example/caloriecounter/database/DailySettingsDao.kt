package com.example.caloriecounter.database

import androidx.room.*

@Dao
interface DailySettingsDao {


    @Query("SELECT * FROM daily_settings where date(start_date) <= date(:date) and updated != $UPDATE_STATUS_DELETED  ORDER BY start_date DESC LIMIT 1")
    fun get(date: String): List<DailySetting>


    @Query("SELECT * FROM daily_settings where updated != $UPDATE_STATUS_DELETED")
    fun getAll(): List<DailySetting>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dailySetting: DailySetting)


    @Query("UPDATE daily_settings set updated = $UPDATE_STATUS_DELETED WHERE id = :id")
    fun markAsDeletedById(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(dailySetting: List<DailySetting>)

    @Transaction
    fun update(dailySetting: DailySetting, id: String): DailySetting {
        val dSetting = DailySetting(id, dailySetting.startDate, dailySetting.caloriesLimit)
        insert(dSetting)
        return dSetting;
    }

}