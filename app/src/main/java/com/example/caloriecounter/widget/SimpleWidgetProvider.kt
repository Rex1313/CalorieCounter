package com.example.caloriecounter.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.example.caloriecounter.MainActivity
import com.example.caloriecounter.R
import com.example.caloriecounter.repository.CalorieCounterRepository
import com.example.caloriecounter.utils.WidgetUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SimpleWidgetProvider : AppWidgetProvider() {

    val repository = CalorieCounterRepository

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        GlobalScope.launch {
            val widgetInfo = repository.getWidgetInfo()
            withContext(Dispatchers.Main) {
                val startActivityIntent: PendingIntent = Intent(context, MainActivity::class.java)
                    .apply { putExtra(WidgetUtils.PENDING_INTENT_START_ACTIVITY, true) }
                    .let { intent ->
                        PendingIntent.getActivity(context, 0, intent, 0)
                    }

                val updateIntent = Intent()
                updateIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                updateIntent.putExtra("ids", appWidgetIds)

                val pendingIntent = PendingIntent.getBroadcast(
                    context, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT
                )

                val remoteViews = RemoteViews(
                    context?.packageName,
                    R.layout.app_widget_simple
                ).apply {
                    this.setOnClickPendingIntent(R.id.relative_layout, pendingIntent)
                    this.setTextViewText(R.id.textview_calories_left, widgetInfo.caloriesLeft)
                    this.setOnClickPendingIntent(R.id.linearlayout_add, startActivityIntent)
                }


                appWidgetIds?.forEach {
                    appWidgetManager?.updateAppWidget(it, remoteViews)
                }

            }

        }
    }

    fun updateCalories(context: Context?, intent: Intent?, widgetIds:IntArray?){
        GlobalScope.launch {
            val widgetInfo = repository.getWidgetInfo()
            withContext(Dispatchers.Main) {
                val remoteViews = RemoteViews(
                    context?.packageName,
                    R.layout.app_widget_simple
                ).apply {
                    this.setTextViewText(R.id.textview_calories_left, widgetInfo.caloriesLeft)
                }
                val manager = AppWidgetManager.getInstance(context)
                manager.updateAppWidget(intent?.getIntArrayExtra("ids")?:widgetIds,remoteViews )
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        updateCalories(context, intent, null)

    }


    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
        updateCalories(context, null, newWidgetIds)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)

    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }
}