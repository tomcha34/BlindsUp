package com.mdev.blindsup.notifications

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mdev.blindsup.R
import com.mdev.blindsup.data.Constants

class BlindNotification {

    // Basic function to trigger a notification. The body of the notification
    // is passed as a parameter, and will be loaded with dynamic data
    fun triggerNotification(notificationMessage: String, context: Context) {
        val builder = NotificationCompat.Builder(context, Constants().CHANNEL_ID)
            .setSmallIcon(R.drawable.blindsup_logo_one)
            .setContentTitle("Blinds Are Up!")
            .setContentText(notificationMessage)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(0, builder.build())


    }
}