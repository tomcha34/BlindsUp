package com.mdev.blindsup.notifications

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mdev.blindsup.R

class BlindNotification {

    fun triggerNotification(notificationMessage: String, context: Context) {

        val builder = NotificationCompat.Builder(context, "my_channel_01")
            .setSmallIcon(R.drawable.blindsup_logo_one)
            .setContentTitle("Blinds Are Up!")
            .setContentText(notificationMessage)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(0, builder.build())


    }
}