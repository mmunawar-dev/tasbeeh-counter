package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.model.PrayerNotificationMode
import com.example.model.PrayerType
import java.util.Calendar

object PrayerNotificationManager {

  private const val TAG = "PrayerNotifMgr"
  const val CHANNEL_ID = "sakinah_prayer_azan_channel"
  const val CHANNEL_NAME = "Prayer Times & Azan"

  const val ACTION_PRAYER_ALARM = "com.example.action.PRAYER_ALARM"
  const val ACTION_STOP_AZAN = "com.example.action.STOP_AZAN"
  const val EXTRA_PRAYER_TYPE = "extra_prayer_type"

  private const val NOTIFICATION_ID_BASE = 5000

  fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        ?: return

      val channel = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_NAME,
        NotificationManager.IMPORTANCE_HIGH
      ).apply {
        description = "Prayer times alerts and Azan audio notifications"
        enableVibration(true)
        lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        // Sound is played directly via AzanAudioPlayer on USAGE_ALARM stream
        setSound(null, null)
      }
      notificationManager.createNotificationChannel(channel)
    }
  }

  fun showPrayerNotification(
    context: Context,
    prayerTypeName: String,
    isTriggerFromWorker: Boolean = false
  ) {
    val prayerType = try {
      PrayerType.valueOf(prayerTypeName)
    } catch (e: Exception) {
      Log.e(TAG, "Unknown prayer type: $prayerTypeName", e)
      return
    }

    createNotificationChannel(context)

    // De-duplication check: ensure no duplicate notifications within 25 minutes
    val cal = Calendar.getInstance()
    val dedupKey = "${cal.get(Calendar.YEAR)}_${cal.get(Calendar.MONTH) + 1}_${cal.get(Calendar.DAY_OF_MONTH)}_${prayerType.name}"
    if (SakinahPreferences.isNotificationDeliveredRecently(context, dedupKey)) {
      Log.d(TAG, "Skipping duplicate notification for $dedupKey (already delivered)")
      return
    }

    val modes = SakinahPreferences.getSavedNotificationModes(context)
    val mode = modes[prayerType] ?: PrayerNotificationMode.SOUND

    if (mode == PrayerNotificationMode.OFF) {
      Log.d(TAG, "Notification for ${prayerType.name} is OFF. Skipping notification.")
      return
    }

    // Mark as delivered to prevent race condition between AlarmManager and WorkManager
    SakinahPreferences.recordNotificationDelivered(context, dedupKey)

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
      ?: return
    val notifId = NOTIFICATION_ID_BASE + prayerType.ordinal

    val city = SakinahPreferences.getSavedCity(context)
    val title = "${prayerType.englishName} Prayer Time (${prayerType.arabicName})"
    val message = "It is now time for ${prayerType.englishName} prayer in ${city.name}."

    val contentIntent = Intent(context, MainActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    val contentPendingIntent = PendingIntent.getActivity(
      context,
      notifId,
      contentIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val stopAzanIntent = Intent(context, PrayerNotificationReceiver::class.java).apply {
      action = ACTION_STOP_AZAN
      putExtra(EXTRA_PRAYER_TYPE, prayerType.name)
    }
    val stopAzanPendingIntent = PendingIntent.getBroadcast(
      context,
      notifId + 500,
      stopAzanIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_notification_audio)
      .setContentTitle(title)
      .setContentText(message)
      .setPriority(NotificationCompat.PRIORITY_MAX)
      .setCategory(NotificationCompat.CATEGORY_ALARM)
      .setAutoCancel(true)
      .setContentIntent(contentPendingIntent)
      .setDeleteIntent(stopAzanPendingIntent)

    when (mode) {
      PrayerNotificationMode.SOUND -> {
        builder.addAction(
          android.R.drawable.ic_media_pause,
          "Stop Azan",
          stopAzanPendingIntent
        )
        // Play Azan audio via dedicated alarm stream player
        AzanAudioPlayer.playAzan(context)
      }
      PrayerNotificationMode.VIBRATE -> {
        builder.setVibrate(longArrayOf(0, 500, 200, 500, 200, 500))
      }
      PrayerNotificationMode.SILENT -> {
        builder.setSilent(true)
      }
      PrayerNotificationMode.OFF -> {
        return
      }
    }

    try {
      notificationManager.notify(notifId, builder.build())
      Log.d(TAG, "Posted prayer notification for ${prayerType.name} with mode $mode")
    } catch (e: Exception) {
      Log.e(TAG, "Failed to post notification", e)
    }

    // Schedule next upcoming prayers
    PrayerNotificationScheduler.scheduleAll(context)
  }
}
