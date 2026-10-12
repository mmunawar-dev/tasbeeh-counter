package com.example.service

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.MainActivity

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

      PrayerNotificationManager.ACTION_NOTIFICATION_TAP -> {
        Log.d(TAG, "Notification tapped. Stopping Azan playback and navigating to MainActivity.")
        AzanAudioPlayer.stopAzan()

        val notifId = intent.getIntExtra(PrayerNotificationManager.EXTRA_NOTIFICATION_ID, -1)
        if (notifId != -1) {
          val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
          nm?.cancel(notifId)
        }

        val mainIntent = Intent(context, MainActivity::class.java).apply {
          flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        try {
          context.startActivity(mainIntent)
        } catch (e: Exception) {
          Log.e(TAG, "Failed to launch MainActivity on notification tap", e)
        }
      }

      PrayerNotificationManager.ACTION_NOTIFICATION_DISMISSED,
      PrayerNotificationManager.ACTION_STOP_AZAN -> {
        Log.d(TAG, "Notification dismissed or stopped. Stopping Azan playback.")
        AzanAudioPlayer.stopAzan()
      }
    }
  }
}
