package com.example.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.calculation.PrayerCalculator
import com.example.model.PrayerNotificationMode
import com.example.model.PrayerTimeItem
import java.util.Calendar
import java.util.concurrent.TimeUnit

object PrayerNotificationScheduler {

  private const val TAG = "PrayerScheduler"
  private const val PERIODIC_WORK_TAG = "sakinah_periodic_prayer_sync"
  private const val WORK_TAG_PRAYER_TRIGGER = "prayer_trigger"

  fun scheduleAll(context: Context) {
    try {
      val city = SakinahPreferences.getSavedCity(context)
      val madhab = SakinahPreferences.getSavedMadhab(context)
      val method = SakinahPreferences.getSavedCalculationMethod(context)
      val adjustments = SakinahPreferences.getSavedManualAdjustments(context)
      val modes = SakinahPreferences.getSavedNotificationModes(context)

      val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
      val workManager = WorkManager.getInstance(context)
      val nowMs = System.currentTimeMillis()

      // Clean up any obsolete one-time trigger workers to prevent duplicate notifications
      try {
        workManager.cancelAllWorkByTag(WORK_TAG_PRAYER_TRIGGER)
      } catch (_: Exception) {}

      val calendar = Calendar.getInstance()
      val todayYear = calendar.get(Calendar.YEAR)
      val todayMonth = calendar.get(Calendar.MONTH) + 1
      val todayDay = calendar.get(Calendar.DAY_OF_MONTH)

      data class ScheduledPrayerEvent(
        val prayer: PrayerTimeItem,
        val triggerMs: Long,
        val occurrenceId: String,
        val isTomorrow: Boolean
      )

      val scheduledEvents = mutableListOf<ScheduledPrayerEvent>()

      fun collectPrayersForDate(year: Int, month: Int, day: Int, isTomorrow: Boolean) {
        val schedule = PrayerCalculator.calculate(
          year = year,
          month = month,
          day = day,
          city = city,
          madhab = madhab,
          method = method,
          manualAdjustmentsMinutes = adjustments,
          notificationModes = modes
        )

        for (prayer in schedule.obligatoryPrayers) {
          val prayerCal = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, prayer.startMinutes / 60)
            set(Calendar.MINUTE, prayer.startMinutes % 60)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
          }

          val triggerMs = prayerCal.timeInMillis
          val occurrenceId = "${year}_${month}_${day}_${prayer.type.name}"

          // Skip if already in the past or already delivered
          if (triggerMs <= nowMs - 30_000L) {
            continue
          }
          if (SakinahPreferences.isNotificationDeliveredRecently(context, occurrenceId)) {
            Log.d(TAG, "Occurrence $occurrenceId already delivered. Skipping scheduling.")
            continue
          }

          scheduledEvents.add(
            ScheduledPrayerEvent(
              prayer = prayer,
              triggerMs = triggerMs,
              occurrenceId = occurrenceId,
              isTomorrow = isTomorrow
            )
          )
        }
      }

      collectPrayersForDate(todayYear, todayMonth, todayDay, isTomorrow = false)

      val tomorrowCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
      collectPrayersForDate(
        tomorrowCal.get(Calendar.YEAR),
        tomorrowCal.get(Calendar.MONTH) + 1,
        tomorrowCal.get(Calendar.DAY_OF_MONTH),
        isTomorrow = true
      )

      scheduledEvents.sortBy { it.triggerMs }

      var exactAlarmsScheduled = false

      for (event in scheduledEvents) {
        val prayer = event.prayer
        val triggerMs = event.triggerMs
        val occurrenceId = event.occurrenceId
        val mode = modes[prayer.type] ?: PrayerNotificationMode.SOUND

        if (mode == PrayerNotificationMode.OFF) {
          continue
        }

        // Stable requestCode: Type ordinal * 10 + (0 for today, 1 for tomorrow)
        val dayOffset = if (event.isTomorrow) 1 else 0
        val requestCode = prayer.type.ordinal * 10 + dayOffset

        val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
          action = PrayerNotificationManager.ACTION_PRAYER_ALARM
          putExtra(PrayerNotificationManager.EXTRA_PRAYER_TYPE, prayer.type.name)
          putExtra(PrayerNotificationManager.EXTRA_OCCURRENCE_ID, occurrenceId)
          putExtra("extra_trigger_time", triggerMs)
        }

        val pendingIntent = PendingIntent.getBroadcast(
          context,
          requestCode,
          intent,
          PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (alarmManager != null) {
          try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
              if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMs, pendingIntent)
              } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMs, pendingIntent)
              }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMs, pendingIntent)
            } else {
              alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerMs, pendingIntent)
            }
            exactAlarmsScheduled = true
            Log.d(TAG, "Scheduled alarm for ${prayer.type.name} ($occurrenceId) at $triggerMs with requestCode=$requestCode")
          } catch (e: SecurityException) {
            Log.w(TAG, "Exact alarm permission restricted; falling back to setAndAllowWhileIdle", e)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMs, pendingIntent)
              exactAlarmsScheduled = true
            }
          }
        }

        // Fallback: ONLY enqueue WorkManager if AlarmManager is unavailable
        if (!exactAlarmsScheduled && alarmManager == null) {
          val delayMs = (triggerMs - nowMs).coerceAtLeast(0L)
          val workInput = workDataOf(
            PrayerTriggerWorker.KEY_PRAYER_TYPE to prayer.type.name,
            PrayerTriggerWorker.KEY_OCCURRENCE_ID to occurrenceId
          )
          val workRequest = OneTimeWorkRequestBuilder<PrayerTriggerWorker>()
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .setInputData(workInput)
            .addTag(WORK_TAG_PRAYER_TRIGGER)
            .build()

          workManager.enqueueUniqueWork(
            "prayer_trigger_${occurrenceId}",
            ExistingWorkPolicy.KEEP,
            workRequest
          )
          Log.d(TAG, "Enqueued fallback WorkManager for $occurrenceId in ${delayMs / 1000}s")
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, "Error in PrayerNotificationScheduler.scheduleAll", e)
    }
  }

  fun schedulePeriodicSync(context: Context) {
    try {
      val periodicWork = PeriodicWorkRequestBuilder<PrayerScheduleWorker>(
        12, TimeUnit.HOURS
      ).build()

      WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        PERIODIC_WORK_TAG,
        ExistingPeriodicWorkPolicy.KEEP,
        periodicWork
      )
      Log.d(TAG, "Scheduled periodic prayer sync work")
    } catch (e: Exception) {
      Log.e(TAG, "Error scheduling periodic sync", e)
    }
  }
}
