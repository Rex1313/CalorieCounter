package com.example.caloriecounter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.caloriecounter.CsvConvertible

@Entity(tableName = DatabaseConstants.USER_SETTINGS_TABLE_NAME)
class UserSettings(
    @PrimaryKey @ColumnInfo(name = UserSettingsConstants.COLUMN_USERNAME) val username: String, @ColumnInfo(
        name = UserSettingsConstants.COLUMN_PWD
    ) val password: String
, @ColumnInfo(name = UserSettingsConstants.COLUMN_TOKEN) val token:String):CsvConvertible {
    override fun toCsv(): String {
        return "$username|$password"
    }
}