package com.example.caloriecounter.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caloriecounter.models.CreateUserResponse

@Dao
interface UserSettingsDao {


    @Query("SELECT * FROM user_settings  ORDER BY username DESC LIMIT 1")
    fun get(): List<UserSettings>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userSettings: UserSettings)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(userSettings: List<UserSettings>)

}