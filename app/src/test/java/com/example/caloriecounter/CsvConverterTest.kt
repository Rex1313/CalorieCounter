package com.example.caloriecounter

import android.os.Environment
import com.example.caloriecounter.database.DailySetting
import com.example.caloriecounter.database.Entry
import com.example.caloriecounter.models.EntryType
import com.example.caloriecounter.utils.export.CsvConverter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File


@RunWith(RobolectricTestRunner::class)
class CsvConverterTest {


    @Before
    fun setup(){
        CsvConverter.dataDirectory =" ${Environment.DIRECTORY_DOCUMENTS}/CalorieCounterTest/"
    }

    @Test
    fun test_fileIsCreated() {
        val testData = mutableListOf<Entry>(
            Entry(1, "12-12-2002", 123f, "some", EntryType.FOOD.toString()),
            Entry(2, "11-12-2002", 323f, "some2", EntryType.FOOD.toString())
        )
        CsvConverter.saveToCsv(testData, "Entries.csv")

        val file = File(CsvConverter.dataDirectory, "entries.csv").useLines {
            it.forEach { println("$it") }
        }
    }

    @Test
    fun test_fileIsCreatedAndRead() {
        val testData = mutableListOf<Entry>(
            Entry(1, "12-12-2002", 123f, "some", EntryType.FOOD.toString()),
            Entry(2, "11-12-2002", 323f, "some2", EntryType.FOOD.toString())
        )
        CsvConverter.saveToCsv(testData, "Entries.csv")

       CsvConverter.readFromCsv<Entry>("Entries.csv").forEach {

       }
    }

    @Test
    fun test_fileIsCreatedAndReadForDailySettings() {
        val testData = mutableListOf<DailySetting>(
            DailySetting("12-12-2001", 1230),
            DailySetting("12-12-2001", 1250),
            DailySetting("13-12-2001", 1260),
            DailySetting("14-12-2001", 1270)
        )
        CsvConverter.saveToCsv(testData, "DailySettings.csv")

        CsvConverter.readFromCsv<DailySetting>("DailySettings.csv").forEach {
        println("Daily settings: ${it.caloriesLimit}")
        }
    }
}