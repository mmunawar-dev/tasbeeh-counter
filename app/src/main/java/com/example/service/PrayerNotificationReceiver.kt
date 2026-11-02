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

    if (action == PrayerNotificationManager.ACTION_PRAYER_ALARM) {
      val prayerTypeName = intent.getStringExtra(PrayerNotificationManager.EXTRA_PRAYER_TYPE)
      val occurrenceId = intent.getStringExtra(PrayerNotificationManager.EXTRA_OCCURRENCE_ID)
      if (prayerTypeName != null) {
        PrayerNotificationManager.showPrayerNotification(
          context = context,
          prayerTypeName = prayerTypeName,
          occurrenceId = occurrenceId,
          isTriggerFromWorker = false
        )
      }
    }
  }
}
