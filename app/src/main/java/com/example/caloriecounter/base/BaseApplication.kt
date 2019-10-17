package com.example.caloriecounter.base

import android.app.Application
import android.os.Environment
import com.example.caloriecounter.repository.CalorieCounterRepository
import com.example.caloriecounter.utils.export.CsvConverter

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        CalorieCounterRepository.initDatabase(this)
        CsvConverter.dataDirectory ="${Environment.getExternalStorageDirectory().path}/CalorieCounter/"
    }
}