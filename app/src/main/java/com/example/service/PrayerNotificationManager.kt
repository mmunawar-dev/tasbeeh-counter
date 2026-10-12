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
import com.example.R
import com.example.model.PrayerNotificationMode
import com.example.model.PrayerType
import java.util.Calendar

object PrayerNotificationManager {

  private const val TAG = "PrayerNotifMgr"
  const val CHANNEL_ID = "sakinah_prayer_notifications_v2"
  const val CHANNEL_NAME = "Prayer Times"

  const val ACTION_PRAYER_ALARM = "com.example.action.PRAYER_ALARM"
  const val ACTION_STOP_AZAN = "com.example.action.STOP_AZAN"
  const val ACTION_NOTIFICATION_TAP = "com.example.action.NOTIFICATION_TAP"
  const val ACTION_NOTIFICATION_DISMISSED = "com.example.action.NOTIFICATION_DISMISSED"

  const val EXTRA_PRAYER_TYPE = "extra_prayer_type"
  const val EXTRA_OCCURRENCE_ID = "extra_occurrence_id"
  const val EXTRA_NOTIFICATION_ID = "extra_notification_id"

  private const val NOTIFICATION_ID_BASE = 5000

  fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        ?: return

      // Clean up legacy high-importance channel if present
      try {
        notificationManager.deleteNotificationChannel("sakinah_prayer_azan_channel")
      } catch (_: Exception) {}

      val channel = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT
      ).apply {
        description = "Daily prayer times and Azan notifications"
        enableVibration(true)
        setShowBadge(true)
        lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        // Sound is null on the channel so system does not play overlapping sounds
        // when AzanAudioPlayer plays azan.mp3.
        setSound(null, null)
      }
      notificationManager.createNotificationChannel(channel)
    }
  }

  fun showPrayerNotification(
    context: Context,
    prayerTypeName: String,
    occurrenceId: String? = null,
    isTriggerFromWorker: Boolean = false
  ) {
    val prayerType = try {
      PrayerType.valueOf(prayerTypeName)
    } catch (e: Exception) {
      Log.e(TAG, "Unknown prayer type: $prayerTypeName", e)
      return
    }

    createNotificationChannel(context)

    // Stable occurrence key: YYYY_MM_DD_PRAYERNAME
    val dedupKey = occurrenceId ?: run {
      val cal = Calendar.getInstance()
      "${cal.get(Calendar.YEAR)}_${cal.get(Calendar.MONTH) + 1}_${cal.get(Calendar.DAY_OF_MONTH)}_${prayerType.name}"
    }

    // Thread-safe and persistent deduplication safeguard
    synchronized(this) {
      if (SakinahPreferences.isNotificationDeliveredRecently(context, dedupKey)) {
        Log.d(TAG, "Skipping duplicate notification for $dedupKey (already delivered)")
        return
      }
      SakinahPreferences.recordNotificationDelivered(context, dedupKey)
    }

    val modes = SakinahPreferences.getSavedNotificationModes(context)
    val mode = modes[prayerType] ?: PrayerNotificationMode.SOUND

    if (mode == PrayerNotificationMode.OFF) {
      Log.d(TAG, "Notification for ${prayerType.name} is OFF. Skipping notification.")
      return
    }

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
      ?: return
    val notifId = NOTIFICATION_ID_BASE + prayerType.ordinal

    val city = SakinahPreferences.getSavedCity(context)
    val title = "${prayerType.englishName} Prayer Time (${prayerType.arabicName})"
    val message = "It is now time for ${prayerType.englishName} prayer in ${city.name}."

    // 1. Content Intent: Tapping notification stops Azan and opens MainActivity
    val tapIntent = Intent(context, PrayerNotificationReceiver::class.java).apply {
      action = ACTION_NOTIFICATION_TAP
      putExtra(EXTRA_PRAYER_TYPE, prayerType.name)
      putExtra(EXTRA_NOTIFICATION_ID, notifId)
    }
    val contentPendingIntent = PendingIntent.getBroadcast(
      context,
      notifId,
      tapIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // 2. Delete Intent: Swiping away / dismissing notification stops Azan
    val dismissIntent = Intent(context, PrayerNotificationReceiver::class.java).apply {
      action = ACTION_NOTIFICATION_DISMISSED
      putExtra(EXTRA_PRAYER_TYPE, prayerType.name)
      putExtra(EXTRA_NOTIFICATION_ID, notifId)
    }
    val deletePendingIntent = PendingIntent.getBroadcast(
      context,
      notifId + 1000,
      dismissIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // 3. Build notification with normal (IMPORTANCE_DEFAULT) configuration
    // and NO "Stop Azan" action button
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_notification_audio)
      .setContentTitle(title)
      .setContentText(message)
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setCategory(NotificationCompat.CATEGORY_REMINDER)
      .setAutoCancel(true)
      .setSound(null) // Prevent simultaneous duplicate notification sound
      .setContentIntent(contentPendingIntent)
      .setDeleteIntent(deletePendingIntent)

    when (mode) {
      PrayerNotificationMode.SOUND -> {
        // Play azan.mp3 via dedicated audio focus player at current system volume
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
      Log.d(TAG, "Posted prayer notification for ${prayerType.name} with mode $mode (dedupKey=$dedupKey)")
    } catch (e: Exception) {
      Log.e(TAG, "Failed to post notification", e)
    }

    // Schedule next upcoming prayers
    PrayerNotificationScheduler.scheduleAll(context)
  }
}
