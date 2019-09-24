package com.example.caloriecounter.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = arrayOf(DailySetting::class, Entry::class),
    version = DatabaseConstants.DATABASE_VERSION
)
abstract class CalorieCounterDatabase : RoomDatabase() {
    abstract fun dailySettingsDao(): DailySettingsDao
}