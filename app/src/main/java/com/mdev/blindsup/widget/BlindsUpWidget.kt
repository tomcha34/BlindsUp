package com.mdev.blindsup.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.RemoteViews
import androidx.lifecycle.ViewModelProvider
import com.mdev.blindsup.R
import com.mdev.blindsup.notifications.BlindNotification
import com.mdev.blindsup.ui.tournamentRunning.TournamentRunningViewModel

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


var position = 0
    var _smallBlind = 25
    var _bigBlind = 50
    var _nextBigBlind = 100
    var _nextSmallBlind = 50
    var timer : CountDownTimer
    var _currentLevel = 1
    var _nextLevel = 2
    var triggerTime = SystemClock.elapsedRealtime() + 60_000L

    views.setTextViewText(R.id.actualtimer, triggerTime.toString())

    fun createTimer(view: View) {


//        if (isNew) {
//            isNew = false


//        val triggerTime = SystemClock.elapsedRealtime() + userTimeSelection

        // println(time)

        timer = object : CountDownTimer(triggerTime, 1_000L) {
            override fun onTick(p0: Long) {
                triggerTime = triggerTime - SystemClock.elapsedRealtime()
                //  millisecondLeft = _elapsedTime.value!!
                //  println(elapsedTime)
                if (triggerTime <= 0) {
                //    resetTimer()
                }
            }

            override fun onFinish() {
              //  resetTimer()

            }
        }
        timer.start()
    }

    fun resetTimer() {

        position++
        _smallBlind *= 2
        _bigBlind *= 2
        _nextSmallBlind = _smallBlind * 2
        _nextBigBlind = _bigBlind * 2
        _currentLevel = position + 1
        _nextLevel = position + 2
       // timer.cancel()

        BlindNotification().triggerNotification(
            "Blinds are now ${_smallBlind}/${_bigBlind}",
            context
        )
     //   createTimer()
    }




    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}