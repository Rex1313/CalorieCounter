package com.example.caloriecounter

import android.content.Context
import android.util.Log
import android.util.Log.println
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.caloriecounter.database.CalorieCounterDatabase
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.database.Entry
import com.example.caloriecounter.database.Favourite
import com.example.caloriecounter.models.EntryType
import com.example.caloriecounter.repository.CalorieCounterRepository
import com.example.caloriecounter.repository.CalorieCounterRepository.db
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RepositoryUnitTest {

    var repository = CalorieCounterRepository


    @Before
    fun setUp() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        repository.initInMemoryDatabase(context)
    }

    @Test
    fun dataIsInserted() {
        runBlocking {
            val dailySetting = GlobalScope.async {
                repository.addDailySetting(DailySetting("1222-12-12", 1200))
                val dailySetting = repository.getDailySetting("1222-12-12")
                println("dailySetting: ${dailySetting?.startDate}")
                dailySetting

            }.await()

            val all = GlobalScope.async {
                println("in memory database ${repository.db}")
                repository.db?.dailySettingsDao()?.getAll()
            }

            all.await()?.forEach {
                println("Entry: ${it.startDate}")
            }

            println("${dailySetting?.startDate} , ${dailySetting?.caloriesLimit}")
        }
    }


    @Test
    fun getsClosestSettingsOnFutureDateWithGreaterDatePresent() {
        val testData = arrayListOf(
            DailySetting("2018-01-01", 1000),
            DailySetting("2018-01-12", 1500),
            DailySetting("2018-01-15", 1200)
        )
        runBlocking {
            GlobalScope.async {
                repository.db?.dailySettingsDao()?.insertAll(testData)
                // try to retrieve dailySetting for 13 01 2018 expected result from 12 01 2018
                val dailySetting = repository.getDailySetting("2018-01-13")
                dailySetting

            }.await()?.let {
                println("DailySetting: ${it.startDate}, ${it.caloriesLimit}")
                assertThat("Gets Closest Date", it.startDate == "2018-01-12")
            }
        }
    }

    @Test
    fun getsClosestSettingsOnSameDate() {
        val testData = arrayListOf(
            DailySetting("2018-01-01", 1000),
            DailySetting("2018-01-12", 1500),
            DailySetting("2018-01-15", 1200)
        )
        runBlocking {
            GlobalScope.async {
                repository.db?.dailySettingsDao()?.insertAll(testData)
                // try to retrieve dailySetting for 13 01 2018 expected result from 12 01 2018
                val dailySetting = repository.getDailySetting("2018-01-12")
                dailySetting

            }.await()?.let {
                println("DailySetting: ${it.startDate}, ${it.caloriesLimit}")
                assertThat("Gets Closest Date", it.startDate == "2018-01-12")
            }
        }
    }


    @Test
    fun getsClosestSettingsOnFutureDate() {
        val testData = arrayListOf(
            DailySetting("2018-01-01", 1000),
            DailySetting("2018-01-12", 1500),
            DailySetting("2018-01-15", 1200)
        )
        runBlocking {
            GlobalScope.async {
                repository.db?.dailySettingsDao()?.insertAll(testData)
                // try to retrieve dailySetting for 13 01 2018 expected result from 12 01 2018
                val dailySetting = repository.getDailySetting("2018-02-12")
                dailySetting

            }.await()?.let {
                println("DailySetting: ${it.startDate}, ${it.caloriesLimit}")
                assertThat("Gets Closest Date", it.startDate == "2018-01-15")
            }
        }
    }

    @Test
    fun test_importDataFromCSV() {
        val testData = arrayListOf<Entry>(
            Entry(1, "1200-12-12", 1200f, "Some", EntryType.FOOD.toString()),
            Entry(2, "1200-12-13", 1300f, "Some", EntryType.FOOD.toString()),
            Entry(3, "1200-12-12", 120f, "Some", EntryType.FOOD.toString())
        )
        runBlocking {
            GlobalScope.async {
                repository.db?.entriesDao()?.insertAll(testData)
                repository.exportAllEntriesToCSV()
                repository.db?.entriesDao()?.deleteAllEntries()
                repository.importAllEntriesFromCSV()
                repository.db?.entriesDao()?.getAll()?.first()

            }.let { entry ->
                assertThat(
                    "Entry is inserted and imported: ",
                    entry.await()?.date == "1200-12-12" ?: false
                )
            }
        }

    }

    @Test
    fun test_addFavourite() {
        val testData = arrayListOf<Favourite>(
            Favourite(1, 256.toFloat(), "peroni small", EntryType.FOOD.toString()),
            Favourite(2, 300.toFloat(), "cookie", EntryType.FOOD.toString()),
            Favourite(3, 400.toFloat(), "tomato", EntryType.FOOD.toString()),
            Favourite(4, 500.toFloat(), "dance", EntryType.EXCERCISE.toString())
        )
        runBlocking {
            GlobalScope.async {
                repository.db?.favouritesDao()?.insertAll(testData);
                repository.db?.favouritesDao()
                    ?.insert(Favourite(null, 400.toFloat(), "cookie2", EntryType.FOOD.toString()))
                repository.db?.favouritesDao()
                    ?.getByNameAndType("cookie2", EntryType.FOOD.toString())?.first()
            }.let { fav ->
                assertThat(
                    "Favourite is added :",
                    fav.await()?.name == "cookie2" ?: false
                )
            }
        }

    }

    @Test
    fun test_editFavourite() {
        val testData = arrayListOf<Favourite>(
            Favourite(1, 256.toFloat(), "peroni small", EntryType.FOOD.toString()),
            Favourite(2, 300.toFloat(), "cookie", EntryType.FOOD.toString()),
            Favourite(3, 400.toFloat(), "tomato", EntryType.FOOD.toString()),
            Favourite(4, 500.toFloat(), "dance", EntryType.EXCERCISE.toString())
        )
        runBlocking {
            GlobalScope.async {
                repository.db?.favouritesDao()?.insertAll(testData);
                repository.db?.favouritesDao()
                    ?.edit(Favourite(4, 400.toFloat(), "dancing cookie", EntryType.FOOD.toString()))
                repository.db?.favouritesDao()
                    ?.getAllFavourites()
            }.let { all ->
                all.await()?.forEach {
                    if (it.id == 4) {
                        assertThat(
                            "Favourite is edited :",
                            it.name == "dancing cookie" ?: false
                        )
                    }
                }

            }
        }

    }

    @Test
    fun test_deleteFavourite() {
        val testData = arrayListOf<Favourite>(
            Favourite(1, 256.toFloat(), "peroni small", EntryType.FOOD.toString()),
            Favourite(2, 300.toFloat(), "cookie", EntryType.FOOD.toString()),
            Favourite(3, 400.toFloat(), "tomato", EntryType.FOOD.toString()),
            Favourite(4, 500.toFloat(), "dance", EntryType.EXCERCISE.toString())
        )
        runBlocking {
            GlobalScope.async {
                repository.db?.favouritesDao()?.insertAll(testData);
                repository.db?.favouritesDao()
                    ?.deleteByFavouriteId(2)
                repository.db?.favouritesDao()
                    ?.getAllFavourites()
            }.let { all ->
                all.await()?.forEach {
                    if (it.id == 2) {
                        assertThat(
                            "Favourite is not deleted :",
                            it.id == 2 ?: false
                        )
                    }
                }

            }
        }

    }

    @Test
    fun test_getFavouriteById() {
        val testData = arrayListOf<Favourite>(
            Favourite(1, 256.toFloat(), "peroni small", EntryType.FOOD.toString()),
            Favourite(2, 300.toFloat(), "cookie", EntryType.FOOD.toString()),
            Favourite(3, 400.toFloat(), "tomato", EntryType.FOOD.toString()),
            Favourite(4, 500.toFloat(), "dance", EntryType.EXCERCISE.toString())
        )
        runBlocking {
            GlobalScope.async {
                repository.db?.favouritesDao()?.insertAll(testData);
                repository.db?.favouritesDao()
                    ?.getById(3)
            }.let { fav ->
                assertThat(
                    "Favourite name by Id :",
                    fav.await()?.name == "tomato" ?: false
                )
            }
        }

    }

    @Test
    fun test_searchFavouritesByNameAndType() {
        val testData = arrayListOf<Favourite>(
            Favourite(1, 256.toFloat(), "peroni small", EntryType.FOOD.toString()),
            Favourite(2, 300.toFloat(), "cookie", EntryType.FOOD.toString()),
            Favourite(3, 400.toFloat(), "tomato", EntryType.FOOD.toString()),
            Favourite(4, 500.toFloat(), "dance", EntryType.EXCERCISE.toString())
        )
        runBlocking {
            GlobalScope.async {
                repository.db?.favouritesDao()?.insertAll(testData);
                repository.db?.favouritesDao()
                    ?.getByNameAndType("cookie", EntryType.FOOD.toString())?.first()
            }.let { fav ->
                assertThat(
                    "Favourite name by search :",
                    fav.await()?.name == "cookie" ?: false
                )
            }
        }

    }

    @Test
    fun test_searchFavouritesByName() {
        val testData = arrayListOf<Favourite>(
            Favourite(1, 256.toFloat(), "peroni small", EntryType.FOOD.toString()),
            Favourite(2, 300.toFloat(), "cookie", EntryType.FOOD.toString()),
            Favourite(3, 400.toFloat(), "tomato", EntryType.FOOD.toString()),
            Favourite(4, 500.toFloat(), "dance", EntryType.EXCERCISE.toString())
        )
        runBlocking {
            GlobalScope.async {
                repository.db?.favouritesDao()?.insertAll(testData);
                repository.db?.favouritesDao()
                    ?.getByName("cookie")?.first()
            }.let { fav ->
                assertThat(
                    "Favourite name by search :",
                    fav.await()?.name == "cookie" ?: false
                )
            }
        }

    }

    @Test
    fun test_getFavouritesAlphabetical() {
        val testData = arrayListOf<Favourite>(
            Favourite(1, 256.toFloat(), "peroni small", EntryType.FOOD.toString()),
            Favourite(2, 300.toFloat(), "cookie", EntryType.FOOD.toString()),
            Favourite(3, 400.toFloat(), "Tomato", EntryType.FOOD.toString()),
            Favourite(4, 500.toFloat(), "dance", EntryType.EXCERCISE.toString())
        )
        runBlocking {
            GlobalScope.async {
                repository.db?.favouritesDao()?.insertAll(testData);
                repository.db?.favouritesDao()
                    ?.getAllFavouritesAlphabetical()
            }.let { all ->
                all.await()?.forEach{

                    println("Favourite by name is: ${it.name}")

                }

            }
        }

    }




    @After
    fun closeDb() {
        repository.db?.close()
    }
}