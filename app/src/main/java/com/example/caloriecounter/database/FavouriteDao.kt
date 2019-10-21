package com.example.caloriecounter.database

import androidx.room.*

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourites where name LIKE '%' || :name || '%' and type=:type")
    fun getByNameAndType(name: String, type: String): List<Favourite>

    @Query("SELECT * FROM favourites where name LIKE '%' || :name || '%'")
    fun getByName(name: String): List<Favourite>

    @Query("SELECT * FROM favourites where id = :id")
    fun getById(id: Int?): Favourite

    @Insert()
    fun insert(favourite: Favourite)

    @Query("DELETE FROM favourites WHERE id = :id")
    fun deleteByFavouriteId(id: Int?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun edit(newFavourite: Favourite)

    @Insert()
    fun insertAll(favourites: List<Favourite>)

    @Query("DELETE FROM favourites")
    fun deleteAllFavourites()

    @Query("SELECT * FROM favourites")
    fun getAllFavourites(): List<Favourite>

    @Query("SELECT * FROM favourites ORDER BY name DESC ")
    fun getAllFavouritesAlphabetical(): List<Favourite>
}