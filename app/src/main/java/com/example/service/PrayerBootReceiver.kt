package com.example.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PrayerBootReceiver : BroadcastReceiver() {

  companion object {
    private const val TAG = "PrayerBootReceiver"
  }

  override fun onReceive(context: Context, intent: Intent?) {
    if (intent == null) return
    val action = intent.action
    Log.d(TAG, "onReceive action=$action")

    when (action) {
      Intent.ACTION_BOOT_COMPLETED,
      Intent.ACTION_MY_PACKAGE_REPLACED,
      Intent.ACTION_TIME_CHANGED,
      Intent.ACTION_TIMEZONE_CHANGED,
      "android.intent.action.TIME_SET" -> {
        Log.d(TAG, "Rescheduling prayer alarms and workers on system event: $action")
        PrayerNotificationScheduler.scheduleAll(context)
        PrayerNotificationScheduler.schedulePeriodicSync(context)
      }
    }
  }
}
