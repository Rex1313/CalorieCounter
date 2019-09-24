package com.example.caloriecounter

import android.content.Context
import android.util.Log.println
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.caloriecounter.database.CalorieCounterDatabase
import com.example.caloriecounter.database.DailySetting
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
                // try to retrieve dailySetting for 13 01 2018 expected result from 15 01 2018
                val dailySetting = repository.getDailySetting("2018-02-12")
                dailySetting

            }.await()?.let {
                println("DailySetting: ${it.startDate}, ${it.caloriesLimit}")
                assertThat("Gets Closest Date", it.startDate == "2018-01-15")
            }
        }
    }


    @After
    fun closeDb() {
        repository.db?.close()
    }
}