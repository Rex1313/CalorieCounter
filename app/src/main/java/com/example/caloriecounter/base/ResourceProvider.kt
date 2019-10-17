package com.example.caloriecounter.base

import android.app.Application
import androidx.annotation.StringRes

object ResourceProvider {
    lateinit var application: Application
    fun withApp(application: Application){
        this.application = application
    }


    fun getString(resourceId: Int):String{
        return application.getString(resourceId)
    }
}