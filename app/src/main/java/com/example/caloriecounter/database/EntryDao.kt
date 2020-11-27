package com.example.caloriecounter.database

import androidx.room.*

@Dao
interface EntryDao {


    @Query("SELECT * FROM entries where date(entry_date) == date(:date) and updated != $UPDATE_STATUS_DELETED")
    fun get(date: String): List<Entry>

    @Query("SELECT * FROM entries where id = :id and updated != $UPDATE_STATUS_DELETED")
    fun getById(id: Int?): Entry

    @Query("SELECT * FROM entries where updated != $UPDATE_STATUS_DELETED")
    fun getAll(): List<Entry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: Entry)

    @Insert()
    fun insertAll(entries: List<Entry>)

    @Query("DELETE FROM entries WHERE id = :id and updated != $UPDATE_STATUS_DELETED")
    fun deleteByEntryId(id: Int?)

    @Query("UPDATE entries set updated = $UPDATE_STATUS_DELETED WHERE id = :id")
    fun markAsDeletedById(id:Int?)

    @Query("DELETE FROM entries where updated != $UPDATE_STATUS_DELETED")
    fun deleteAllEntries()
}