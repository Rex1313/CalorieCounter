package com.example.caloriecounter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")

class Favourite(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int?, @ColumnInfo(
        name = FavouriteConstants.COLUMN_VALUE
    ) val value: Float, @ColumnInfo(name = FavouriteConstants.COLUMN_NAME) val name: String
    , @ColumnInfo(name = FavouriteConstants.COLUMN_TYPE) val type: String
) {

}