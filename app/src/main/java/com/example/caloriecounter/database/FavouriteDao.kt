package com.example.caloriecounter.database

import androidx.room.*

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourites where CONTAINS(name, :name) and type=:type")
    fun getByNameAndType(name: String, type: String): List<Favourite>

    @Query("SELECT * FROM favourites where id = :id")
    fun getById(id: Int?): Favourite

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(favourite: Favourite)

    @Query("DELETE FROM favourites WHERE id = :id")
    fun deleteByFavouriteId(id: Int?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun edit(newFavourite: Favourite)

}