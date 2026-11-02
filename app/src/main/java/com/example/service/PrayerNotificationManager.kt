package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.model.PrayerNotificationMode
import com.example.model.PrayerType
import java.util.Calendar
import androidx.core.net.toUri

/**
 * Manages native Android prayer-time notifications with custom Azan sound.
 *
 * Utilizes NotificationChannel and NotificationManager directly so Android handles
 * audio playback natively, respecting the system notification volume, Do Not Disturb,
 * and standard dismissal behavior without any MediaPlayer, background service, or volume overrides.
 */
object PrayerNotificationManager {

  private const val TAG = "PrayerNotifMgr"

  // Channels (v3 migration ensuring custom Azan sound is configured)
  const val CHANNEL_ID_AZAN = "sakinah_prayer_azan_sound_v1"
  const val CHANNEL_ID_VIBRATE = "sakinah_prayer_vibrate_v1"
  const val CHANNEL_ID_SILENT = "sakinah_prayer_silent_v1"

  const val CHANNEL_NAME_AZAN = "Prayer Times (Azan Sound)"
  const val CHANNEL_NAME_VIBRATE = "Prayer Times (Vibrate)"
  const val CHANNEL_NAME_SILENT = "Prayer Times (Silent)"

  const val ACTION_PRAYER_ALARM = "com.example.action.PRAYER_ALARM"
  const val EXTRA_PRAYER_TYPE = "extra_prayer_type"
  const val EXTRA_OCCURRENCE_ID = "extra_occurrence_id"

  private const val NOTIFICATION_ID_BASE = 5000

  /**
   * Creates notification channels with custom Azan sound URI on Android 8.0+ (API 26+).
   * Implements channel migration by deleting legacy channels whose sound settings cannot be modified.
   */
  fun createNotificationChannels(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        ?: return

      // Clean up legacy channels that had null or incorrect sound
      val obsoleteChannelIds = listOf(
        "sakinah_prayer_azan_channel",
        "sakinah_prayer_notifications_v2"
      )
      for (oldId in obsoleteChannelIds) {
        try {
          notificationManager.deleteNotificationChannel(oldId)
        } catch (_: Exception) {}
      }

      // Custom sound URI pointing to res/raw/azan.mp3
      val soundUri =
          "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.raw.azan}".toUri()
      val audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
        .build()

      // 1. Azan Sound Channel
      val azanChannel = NotificationChannel(
        CHANNEL_ID_AZAN,
        CHANNEL_NAME_AZAN,
        NotificationManager.IMPORTANCE_HIGH
      ).apply {
        description = "Daily prayer time notifications with Azan sound"
        enableVibration(true)
        setShowBadge(true)
        lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        setSound(soundUri, audioAttributes)
      }

      // 2. Vibrate Only Channel
      val vibrateChannel = NotificationChannel(
        CHANNEL_ID_VIBRATE,
        CHANNEL_NAME_VIBRATE,
        NotificationManager.IMPORTANCE_DEFAULT
      ).apply {
        description = "Daily prayer time notifications with vibration"
        enableVibration(true)
        setShowBadge(true)
        lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        setSound(null, null)
      }

      // 3. Silent Channel
      val silentChannel = NotificationChannel(
        CHANNEL_ID_SILENT,
        CHANNEL_NAME_SILENT,
        NotificationManager.IMPORTANCE_LOW
      ).apply {
        description = "Daily prayer time silent notifications"
        enableVibration(false)
        setShowBadge(false)
        lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        setSound(null, null)
      }

      notificationManager.createNotificationChannels(listOf(azanChannel, vibrateChannel, silentChannel))
    }
  }

  /**
   * Displays a normal native Android notification for the given prayer.
   */
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

    createNotificationChannels(context)

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

    // Normal Activity launch on notification tap
    val contentIntent = Intent(context, MainActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    val contentPendingIntent = PendingIntent.getActivity(
      context,
      notifId,
      contentIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Determine channel based on user preference
    val channelId = when (mode) {
      PrayerNotificationMode.SOUND -> CHANNEL_ID_AZAN
      PrayerNotificationMode.VIBRATE -> CHANNEL_ID_VIBRATE
      PrayerNotificationMode.SILENT -> CHANNEL_ID_SILENT
      PrayerNotificationMode.OFF -> return
    }

    // Build normal dismissible Android notification
    val builder = NotificationCompat.Builder(context, channelId)
      .setSmallIcon(R.mipmap.ic_launcher)
      .setContentTitle(title)
      .setContentText(message)
      .setAutoCancel(true)
      .setOngoing(false) // Fully dismissible
      .setContentIntent(contentPendingIntent)

    // Pre-Android O legacy sound and vibration fallback
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      when (mode) {
        PrayerNotificationMode.SOUND -> {
          val soundUri = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.raw.azan}")
          builder.setSound(soundUri)
          builder.setPriority(NotificationCompat.PRIORITY_HIGH)
          builder.setVibrate(longArrayOf(0, 500, 200, 500))
        }
        PrayerNotificationMode.VIBRATE -> {
          builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
          builder.setVibrate(longArrayOf(0, 500, 200, 500))
        }
        PrayerNotificationMode.SILENT -> {
          builder.setPriority(NotificationCompat.PRIORITY_LOW)
          builder.setSilent(true)
        }
        PrayerNotificationMode.OFF -> return
      }
    }

    try {
      notificationManager.notify(notifId, builder.build())
      Log.d(TAG, "Posted native prayer notification for ${prayerType.name} with channel=$channelId mode=$mode")
    } catch (e: Exception) {
      Log.e(TAG, "Failed to post notification", e)
    }

    // Schedule next upcoming prayers
    PrayerNotificationScheduler.scheduleAll(context)
  }
}
