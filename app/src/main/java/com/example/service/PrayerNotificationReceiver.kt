package com.example.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PrayerNotificationReceiver : BroadcastReceiver() {

  companion object {
    private const val TAG = "PrayerNotifReceiver"
  }

  override fun onReceive(context: Context, intent: Intent?) {
    if (intent == null) return
    val action = intent.action
    Log.d(TAG, "onReceive action=$action")

    when (action) {
      PrayerNotificationManager.ACTION_PRAYER_ALARM -> {
        val prayerTypeName = intent.getStringExtra(PrayerNotificationManager.EXTRA_PRAYER_TYPE)
        if (prayerTypeName != null) {
          PrayerNotificationManager.showPrayerNotification(
            context,
            prayerTypeName,
            isTriggerFromWorker = false
          )
        }
      }
      PrayerNotificationManager.ACTION_STOP_AZAN -> {
        Log.d(TAG, "Stopping Azan playback by user action")
        AzanAudioPlayer.stopAzan()
      }
    }
  }
}
