package com.example.caloriecounter.repository

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.caloriecounter.database.CalorieCounterDatabase
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.database.DatabaseConstants
import com.example.caloriecounter.database.Entry
import com.example.caloriecounter.utils.export.CsvConverter
import com.example.caloriecounter.utils.export.ImportExportValues
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

    suspend fun getEntryById(id: Int?) = withContext(Dispatchers.IO) {
        return@withContext db?.entriesDao()?.getById(id)
    }

    suspend fun addEntry(entry: Entry) = withContext(Dispatchers.IO) {
        db?.entriesDao()?.insert(entry)

    }

    suspend fun removeEntryById(id: Int?) = withContext(Dispatchers.IO) {
        db?.entriesDao()?.deleteByEntryId(id)

    }

    suspend fun editEntry(entry: Entry) = withContext(Dispatchers.IO) {
        db?.entriesDao()?.insert(entry)
    }

    suspend fun exportAllEntriesToCSV() = withContext(Dispatchers.IO){
        val entries  = db?.entriesDao()?.getAll()
        entries?.let {
            CsvConverter.saveToCsv(it, ImportExportValues.ENTRIES_CSV_FILE)
        }
    }

    suspend fun exportDailySettingsToCSV() = withContext(Dispatchers.IO){
        val dailySettings = db?.dailySettingsDao()?.getAll()
        dailySettings?.let {
            CsvConverter.saveToCsv(it, ImportExportValues.DAILY_SETTINGS_CSV_FILE)
        }
    }

    suspend fun importAllEntriesFromCSV() = withContext(Dispatchers.IO){
        val entries = CsvConverter.readFromCsv<Entry>(ImportExportValues.ENTRIES_CSV_FILE)
        db?.entriesDao()?.insertAll(entries)

    }
    suspend fun importAllDailySettingsFromCSV() = withContext(Dispatchers.IO){
        val dailySettings = CsvConverter.readFromCsv<DailySetting>(ImportExportValues.DAILY_SETTINGS_CSV_FILE)
        db?.dailySettingsDao()?.insertAll(dailySettings)
    }
}