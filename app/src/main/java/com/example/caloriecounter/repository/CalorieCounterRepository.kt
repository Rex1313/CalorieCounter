package com.example.caloriecounter.repository

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.room.Room
import com.example.caloriecounter.R
import com.example.caloriecounter.base.ResourceProvider
import com.example.caloriecounter.database.CalorieCounterDatabase
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.database.DatabaseConstants
import com.example.caloriecounter.database.Entry
import com.example.caloriecounter.models.SimpleWidgetModel
import com.example.caloriecounter.utils.CalculationUtils
import com.example.caloriecounter.utils.DateUtils
import com.example.caloriecounter.database.*
import com.example.caloriecounter.utils.export.CsvConverter
import com.example.caloriecounter.utils.export.ImportExportValues
import kotlinx.android.synthetic.main.fragment_day.view.*
import kotlinx.coroutines.*
import org.joda.time.LocalDate

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

    suspend fun getWidgetInfo()= withContext(Dispatchers.IO){
        val todayDate = LocalDate.now().toString(DateUtils.DB_DATE_FORMAT)
        val dailySettings = db?.dailySettingsDao()?.get(todayDate)
        val entries = db?.entriesDao()?.get(todayDate)
       val leftCalories = CalculationUtils.calculateLeftCalories(entries?: mutableListOf(), dailySettings?.firstOrNull()?.caloriesLimit?.toFloat()?:0f)
        return@withContext SimpleWidgetModel("${leftCalories} ${ResourceProvider.getString(R.string.kcal)}")
    }

    suspend fun removeEntryById(id: Int?) = withContext(Dispatchers.IO) {
        db?.entriesDao()?.deleteByEntryId(id)

    }

    suspend fun editEntry(entry: Entry) = withContext(Dispatchers.IO) {
        db?.entriesDao()?.insert(entry)
    }


    suspend fun searchFavouritesByNameAndType(name: String, type: String) =
        withContext(Dispatchers.IO) {
            return@withContext db?.favouritesDao()?.getByNameAndType(name, type)
        }

    suspend fun searchFavouritesByName(name: String) = withContext(Dispatchers.IO) {
        return@withContext db?.favouritesDao()?.getByName(name)
    }

    suspend fun addFavourite(value: Float, name: String, type: String): Boolean =
        withContext(Dispatchers.IO) {
            if (db?.favouritesDao()?.getByNameAndType(name, type).isNullOrEmpty()) {
                db?.favouritesDao()?.insert(Favourite(null, value, name, type))
                return@withContext true
            }
            return@withContext false
        }

    suspend fun deleteFavouriteById(id: Int?) = withContext(Dispatchers.IO) {
        db?.favouritesDao()?.deleteByFavouriteId(id)
    }

    suspend fun getFavouriteById(id: Int?) = withContext(Dispatchers.IO) {
        db?.favouritesDao()?.getById(id)
    }

    suspend fun editFavourite(favourite: Favourite) = withContext(Dispatchers.IO) {
        db?.favouritesDao()?.edit(favourite);
    }

    suspend fun getAllFavouritesAlphabetical() = withContext(Dispatchers.IO) {
        return@withContext db?.favouritesDao()?.getAllFavouritesAlphabetical()
    }

    suspend fun getFavoritesStartingWith(query:String) = withContext(Dispatchers.IO){
        return@withContext db?.favouritesDao()?.getStartsWith(query)
    }

    suspend fun exportAllEntriesToCSV() = withContext(Dispatchers.IO) {
        val entries = db?.entriesDao()?.getAll()
        entries?.let {
            CsvConverter.saveToCsv(it, ImportExportValues.ENTRIES_CSV_FILE)
        }
    }

    suspend fun exportDailySettingsToCSV() = withContext(Dispatchers.IO) {
        val dailySettings = db?.dailySettingsDao()?.getAll()
        dailySettings?.let {
            CsvConverter.saveToCsv(it, ImportExportValues.DAILY_SETTINGS_CSV_FILE)
        }
    }

    suspend fun importAllEntriesFromCSV() = withContext(Dispatchers.IO) {
        val entries = CsvConverter.readFromCsv<Entry>(ImportExportValues.ENTRIES_CSV_FILE)
        db?.entriesDao()?.insertAll(entries)

    }

    suspend fun importAllDailySettingsFromCSV() = withContext(Dispatchers.IO) {
        val dailySettings =
            CsvConverter.readFromCsv<DailySetting>(ImportExportValues.DAILY_SETTINGS_CSV_FILE)
        db?.dailySettingsDao()?.insertAll(dailySettings)
    }
}