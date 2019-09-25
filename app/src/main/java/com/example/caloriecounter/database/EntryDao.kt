package com.example.caloriecounter.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EntryDao {


    @Query("SELECT * FROM entries where date(entry_date) == date(:date)")
    fun get(date: String): List<Entry>


    @Query("SELECT * FROM entries")
    fun getAll():List<Entry>

    @Insert()
    fun insert(entry: Entry)


    @Insert()
    fun insertAll(entries: List<Entry>)

}