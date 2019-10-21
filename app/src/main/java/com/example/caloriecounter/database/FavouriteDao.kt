package com.example.caloriecounter.database

import androidx.room.*

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourites where name = :name and type=:type")
    fun getByNameAndType(name: String, type: String): Favourite

    @Query("SELECT * FROM favourites where id = :id")
    fun getById(id: Int?): Favourite

    @Insert()
    fun insert(favourite: Favourite):Boolean

    @Query("DELETE FROM favourites WHERE id = :id")
    fun deleteByFavouriteId(id: Int?)

}