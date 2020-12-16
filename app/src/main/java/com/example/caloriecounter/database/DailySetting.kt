package com.example.caloriecounter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.caloriecounter.CsvConvertible
import com.example.caloriecounter.utils.getRandomUUID

@Entity(tableName = DatabaseConstants.DAILY_SETTING_TABLE_NAME)
class DailySetting(
     @ColumnInfo(name = DailySettingConstants.COLUMN_ID) val id: String,
     @PrimaryKey @ColumnInfo(name = DailySettingConstants.COLUMN_DATE) val startDate: String,
    @ColumnInfo(
        name = DailySettingConstants.COLUMN_CALORIES_LIMIT
    ) val caloriesLimit: Int
    ,
    @ColumnInfo(name = DailySettingConstants.COLUMN_UPDATE) val update: Int = UPDATE_STATUS_SYNCED
) : CsvConvertible {
    override fun toCsv(): String {
        return "$id|$startDate|$caloriesLimit"
    }
}