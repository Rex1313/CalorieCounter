package com.example.caloriecounter.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserSettingsDao {


    @Query("SELECT * FROM user_settings  ORDER BY id ASC LIMIT 1")
    fun get(): List<UserSettings>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userSettings: UserSettings)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(userSettings: List<UserSettings>)

}