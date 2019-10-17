package com.example.caloriecounter.database

import androidx.room.*

@Dao
interface EntryDao {


    @Query("SELECT * FROM entries where date(entry_date) == date(:date)")
    fun get(date: String): List<Entry>

    @Query("SELECT * FROM entries where id = :id")
    fun getById(id: Int?): Entry

    @Query("SELECT * FROM entries")
    fun getAll(): List<Entry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: Entry)


    @Insert()
    fun insertAll(entries: List<Entry>)

    @Query("DELETE FROM entries WHERE id = :id")
    fun deleteByEntryId(id: Int?)

    @Query("DELETE FROM entries")
    fun deleteAllEntries()
}