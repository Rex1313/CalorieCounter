package com.example.caloriecounter.database

import androidx.room.*

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM favourites where name LIKE '%' || :name || '%' and type=:type and updated!=$UPDATE_STATUS_DELETED")
    fun getByNameAndType(name: String, type: String): List<Favourite>

    @Query("SELECT * FROM favourites where name LIKE '%' || :name || '%' and updated!=$UPDATE_STATUS_DELETED")
    fun getByName(name: String): List<Favourite>

    @Query("SELECT * FROM favourites where name LIKE :query || '%' and updated!=$UPDATE_STATUS_DELETED ORDER BY name COLLATE NOCASE ASC")
    fun getStartsWith(query: String): List<Favourite>


    @Query("SELECT * FROM favourites where id = :id and updated!=$UPDATE_STATUS_DELETED")
    fun getById(id: Int?): Favourite

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favourite: Favourite)

    @Query("DELETE FROM favourites WHERE id = :id and updated!=$UPDATE_STATUS_DELETED")
    fun deleteByFavouriteId(id: Int?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun edit(newFavourite: Favourite)

    @Insert()
    fun insertAll(favourites: List<Favourite>)

    @Query("DELETE FROM favourites")
    fun deleteAllFavourites()

    @Query("SELECT * FROM favourites where updated!=$UPDATE_STATUS_DELETED")
    fun getAllFavourites(): List<Favourite>

    @Query("SELECT * FROM favourites where updated!=$UPDATE_STATUS_DELETED ORDER BY name COLLATE NOCASE ASC ")
    fun getAllFavouritesAlphabetical(): List<Favourite>
}