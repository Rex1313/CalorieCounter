package com.example.caloriecounter.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.caloriecounter.utils.getRandomUUID


@Database(
    entities = arrayOf(DailySetting::class, Entry::class, Favourite::class, UserSettings::class),
    version = DatabaseConstants.DATABASE_VERSION
)
abstract class CalorieCounterDatabase : RoomDatabase() {
    abstract fun dailySettingsDao(): DailySettingsDao
    abstract fun entriesDao(): EntryDao
    abstract fun favouritesDao(): FavouriteDao
    abstract fun userSettingsDao():UserSettingsDao
   companion object{
       val MIGRATION_2_3: Migration = object : Migration(2, 3) {
           override fun migrate(database: SupportSQLiteDatabase) {
               database.execSQL(
                   "ALTER TABLE daily_settings "
                           + " ADD COLUMN id TEXT NOT NULL DEFAULT '${getRandomUUID()}'"
               )
           }
       }
   }

}

