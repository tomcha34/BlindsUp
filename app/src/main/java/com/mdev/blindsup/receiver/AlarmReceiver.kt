package com.mdev.blindsup.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mdev.blindsup.notifications.BlindNotification

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val notification = BlindNotification()
        if (p0 != null) {
            notification.triggerNotification("Blinds are up!", p0)
        }
    }
}