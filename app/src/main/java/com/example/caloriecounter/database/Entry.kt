package com.example.caloriecounter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.caloriecounter.CsvConvertible

@Entity(tableName = "entries")
class Entry(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int?, @ColumnInfo(name =EntryConstants.COLUMN_DATE) val date:String, @ColumnInfo(
        name = EntryConstants.COLUMN_VALUE
    ) val entryValue: Float, @ColumnInfo(name = EntryConstants.COLUMN_NAME) val entryName: String? = EntryConstants.NAME_DEFAULT_VALUE
    , @ColumnInfo(name = EntryConstants.COLUMN_ENTRY_TYPE) val entryType:String):CsvConvertible {


   override fun toCsv(): String {
        return "null|$date|$entryValue|$entryName|$entryType"
    }
}