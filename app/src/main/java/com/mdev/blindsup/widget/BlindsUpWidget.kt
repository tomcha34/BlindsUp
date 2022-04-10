package com.mdev.blindsup.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews
import com.mdev.blindsup.R

/**
 * Implementation of App Widget functionality.
 */
class BlindsUpWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.blinds_up_widget)
    //views.setTextViewText(R.id.appwidget_text, widgetText)



    val timer = object : CountDownTimer(10_000L, 1_000L){
        override fun onTick(p0: Long) {
            views.setTextViewText(R.id.actualtimer, SystemClock.elapsedRealtime().toString())
        }

        override fun onFinish() {
            Log.e("Tag", "ended")
        }

    }
    timer.start()



    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}