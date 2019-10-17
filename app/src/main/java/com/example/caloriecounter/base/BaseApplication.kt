package com.example.caloriecounter.base

import android.app.Application
import com.example.caloriecounter.repository.CalorieCounterRepository

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        CalorieCounterRepository.initDatabase(this)
        ResourceProvider.application = this
    }
}