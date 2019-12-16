package com.example.caloriecounter.utils

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import android.content.Intent
import android.provider.Settings
import android.widget.RemoteViews
import com.example.caloriecounter.R
import com.example.caloriecounter.widget.SimpleWidgetProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class WidgetUtils {

    companion object{
        const val PENDING_INTENT_START_ACTIVITY = "calorie_counter_pending_intent_start_activity"




        fun requestUpdateSimpleWidgets(app:Application)
        {
            val intent = Intent(app, SimpleWidgetProvider::class.java)
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            val appWidgetManager =AppWidgetManager.getInstance(app)
            val ids = appWidgetManager.getAppWidgetIds((ComponentName(app, SimpleWidgetProvider::class.java)))
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            app.sendBroadcast(intent)
            val widget = SimpleWidgetProvider()
            widget.onUpdate(app, appWidgetManager, ids)
        }
    }
}