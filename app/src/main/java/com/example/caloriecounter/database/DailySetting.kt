package com.example.caloriecounter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.caloriecounter.CsvConvertible

@Entity(tableName = DatabaseConstants.DAILY_SETTING_TABLE_NAME)
class DailySetting(
    @PrimaryKey @ColumnInfo(name = DailySettingConstants.COLUMN_DATE) val startDate: String, @ColumnInfo(
        name = DailySettingConstants.COLUMN_CALORIES_LIMIT
    ) val caloriesLimit: Int
):CsvConvertible {
    override fun toCsv(): String {
        return "$startDate|$caloriesLimit"
    }
}