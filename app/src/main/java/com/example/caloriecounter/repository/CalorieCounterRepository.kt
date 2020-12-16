package com.example.caloriecounter.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.caloriecounter.R
import com.example.caloriecounter.base.ResourceProvider
import com.example.caloriecounter.database.*
import com.example.caloriecounter.models.CreateUserResponse
import com.example.caloriecounter.models.SimpleWidgetModel
import com.example.caloriecounter.models.User
import com.example.caloriecounter.network.ApiService
import com.example.caloriecounter.utils.CalculationUtils
import com.example.caloriecounter.utils.DateUtils
import com.example.caloriecounter.utils.export.CsvConverter
import com.example.caloriecounter.utils.export.ImportExportValues
import com.example.caloriecounter.utils.getRandomUUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CalorieCounterRepository {

    var retrofit = Retrofit.Builder()
        .baseUrl("http://92.222.75.54:9898/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)

    var db: CalorieCounterDatabase? = null
    fun initDatabase(app: Application) {
        db = Room.databaseBuilder(
            app,
            CalorieCounterDatabase::class.java,
            DatabaseConstants.DATABASE_NAME
        ).addMigrations(CalorieCounterDatabase.MIGRATION_2_3).build()

    }


    suspend fun registerUserOnServer(user: User): CreateUserResponse {
        return apiService.createUser(user)
    }

    fun initInMemoryDatabase(context: Context) {
        db = Room.inMemoryDatabaseBuilder(context, CalorieCounterDatabase::class.java).build()
    }

    suspend fun addUserSetting(userSettings: UserSettings) = withContext(Dispatchers.IO) {
        db?.userSettingsDao()?.insert(userSettings)
    }

    suspend fun getUserSetting(): UserSettings? = withContext(Dispatchers.IO) {
        return@withContext db?.userSettingsDao()?.get()?.firstOrNull()
    }

    suspend fun addDailySetting(dailySetting: DailySetting) = withContext(Dispatchers.IO) {
        val dSetting = db?.dailySettingsDao()?.get(dailySetting.startDate)
        if (dSetting?.size ?: 0 == 0) { // Entry doesn't exist insert it
            db?.dailySettingsDao()?.insert(dailySetting)
        } else { // entry exist so update it with regard to unique id
            db?.dailySettingsDao()?.update(dailySetting, dSetting?.first()?.id ?: "")
        }

    }

    suspend fun getDailySetting(date: String): DailySetting? = withContext(Dispatchers.IO) {
        return@withContext db?.dailySettingsDao()?.get(date)?.firstOrNull()
            ?: DailySetting(getRandomUUID(),"1920-12-12", 1200)
    }

    // Date needs to be in format YYYY-mm-DD
    suspend fun getEntriesForDate(date: String): List<Entry> = withContext(Dispatchers.IO) {
        return@withContext db?.entriesDao()?.get(date) ?: mutableListOf()
    }

    suspend fun getEntryById(id: String) = withContext(Dispatchers.IO) {
        return@withContext db?.entriesDao()?.getById(id)
    }

    suspend fun addEntry(entry: Entry) = withContext(Dispatchers.IO) {
        db?.entriesDao()?.insert(entry)
    }

    suspend fun getWidgetInfo() = withContext(Dispatchers.IO) {
        val todayDate = LocalDate.now().toString(DateUtils.DB_DATE_FORMAT)
        val dailySettings = db?.dailySettingsDao()?.get(todayDate)
        val entries = db?.entriesDao()?.get(todayDate)
        val leftCalories = CalculationUtils.calculateLeftCalories(
            entries ?: mutableListOf(),
            dailySettings?.firstOrNull()?.caloriesLimit?.toFloat() ?: 0f
        )
        return@withContext SimpleWidgetModel("${leftCalories} ${ResourceProvider.getString(R.string.kcal)}")
    }

    suspend fun removeDailySettingById(id: String) = withContext(Dispatchers.IO) {
        db?.dailySettingsDao()?.markAsDeletedById(id)
    }

    suspend fun removeEntryById(id: String?) = withContext(Dispatchers.IO) {
        db?.entriesDao()?.markAsDeletedById(id)
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
            val fav = db?.favouritesDao()?.getByNameAndType(name, type);
            if (fav.isNullOrEmpty()) {
                db?.favouritesDao()?.insert(Favourite(null, value, name, type))
                return@withContext true
            }else{
                db?.favouritesDao()?.edit(Favourite(fav.first().id, value, name, type))
                return@withContext true;
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

    suspend fun getFavoritesStartingWith(query: String) = withContext(Dispatchers.IO) {
        return@withContext db?.favouritesDao()?.getStartsWith(query)
    }

    suspend fun exportAllEntriesToCSV() = withContext(Dispatchers.IO) {
        val entries = db?.entriesDao()?.getAllButDeleted()
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

    suspend fun uploadData() {
        val userSettings = db?.userSettingsDao()?.get()?.firstOrNull()
        userSettings?.token?.let { token ->
            val entries =
                db?.entriesDao()?.getAll()?.filterNot { it.update == UPDATE_STATUS_SYNCED }
            entries?.forEach {
                Log.d("Entries to upload :", " ${it.toString()}")
                val response = apiService.uploadEntry(token, it)
                if (response.status == "Success") {
                    setEntrySynced(it)
                }

            }

        val dailysetting = db?.dailySettingsDao()?.getAll()?.filterNot { it.update == UPDATE_STATUS_SYNCED }
            dailysetting?.forEach {
                val response = apiService.uploadDailySetting(token, it)
                if(response.status=="Success"){
                    setDailySettingSynced(dailySetting = it)
                }
            }
        }


    }

    private suspend fun setEntrySynced(entry: Entry) {
        if (entry.update == UPDATE_STATUS_DELETED) {// We do not want to display deleted entries, or update its status to synced. We want to remove it from DB altogether after successful sync
            removeEntryById(entry.id)
        } else { // OTHERWISE WE GONNA CHANGE ENTRY UPDATE STATUS TO SYNCED
            editEntry(
                Entry(
                    id = entry.id,
                    date = entry.date,
                    entryValue = entry.entryValue,
                    entryName = entry.entryName,
                    entryType = entry.entryType,
                    update = UPDATE_STATUS_SYNCED
                )
            )
        }
    }
    private suspend fun setDailySettingSynced(dailySetting: DailySetting) {
        if (dailySetting.update == UPDATE_STATUS_DELETED) {// We do not want to display deleted entries, or update its status to synced. We want to remove it from DB altogether after successful sync
            removeDailySettingById(dailySetting.id)
        } else { // OTHERWISE WE GONNA CHANGE ENTRY UPDATE STATUS TO SYNCED
            addDailySetting(
                DailySetting(
                    id = dailySetting.id,
                    startDate = dailySetting.startDate,
                    caloriesLimit = dailySetting.caloriesLimit,
                    update = UPDATE_STATUS_SYNCED
                )
            )
        }
    }

}