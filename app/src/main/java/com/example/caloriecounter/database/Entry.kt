package com.example.caloriecounter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.caloriecounter.CsvConvertible

@Entity(tableName = DatabaseConstants.ENTRIES_TABLE_NAME)
class Entry(
    @PrimaryKey() @ColumnInfo(name = "id") val id: String, @ColumnInfo(name =EntryConstants.COLUMN_DATE) val date:String, @ColumnInfo(
        name = EntryConstants.COLUMN_VALUE
    ) val entryValue: Float, @ColumnInfo(name = EntryConstants.COLUMN_NAME) val entryName: String? = EntryConstants.NAME_DEFAULT_VALUE
    , @ColumnInfo(name = EntryConstants.COLUMN_ENTRY_TYPE) val entryType:String,@ColumnInfo(name = EntryConstants.COLUMN_UPDATE) val update:Int = UPDATE_STATUS_SYNCED):CsvConvertible {


   override fun toCsv(): String {
        return "$id|$date|$entryValue|$entryName|$entryType"
    }
}