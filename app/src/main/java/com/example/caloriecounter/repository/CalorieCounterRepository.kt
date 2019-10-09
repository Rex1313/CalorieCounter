package com.example.caloriecounter.repository

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.caloriecounter.database.CalorieCounterDatabase
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.database.DatabaseConstants
import com.example.caloriecounter.database.Entry
import kotlinx.coroutines.*

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

    suspend fun addDailySetting(dailySetting: DailySetting) = withContext(Dispatchers.IO) {
        db?.dailySettingsDao()?.insert(dailySetting)
    }

    suspend fun getDailySetting(date: String): DailySetting? = withContext(Dispatchers.IO) {
        return@withContext db?.dailySettingsDao()?.get(date)?.firstOrNull()
            ?: DailySetting("1920-12-12", 1200)
    }

    // Date needs to be in format YYYY-mm-DD
    suspend fun getEntriesForDate(date: String): List<Entry> = withContext(Dispatchers.IO) {
        return@withContext db?.entriesDao()?.get(date) ?: mutableListOf()
    }

    fun addSomeEntries() {
        runBlocking {
            GlobalScope.async {
                db?.entriesDao()?.insertAll(
                    mutableListOf(
                        Entry(null, "1220-12-12", 1000F),
                        Entry(null, "1720-11-14", 1500F, "Mamas"),
                        Entry(null, "1920-12-12", 1600F, "Bread")
                    )
                )

                db?.dailySettingsDao()?.insert(DailySetting("2019-09-26", 1450))
            }
        }
    }

    suspend fun addEntry(entry: Entry) = withContext(Dispatchers.IO) {
        db?.entriesDao()?.insert(entry)

    }

    suspend fun removeEntry(entry: Entry) = withContext(Dispatchers.IO) {
        db?.entriesDao()?.delete(entry)
    }

    suspend fun removeEntryById(id: Int?) = withContext(Dispatchers.IO) {
        db?.entriesDao()?.deleteByEntryId(id)

    }

}