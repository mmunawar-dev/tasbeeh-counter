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

      // Calculate prayer schedules for today and tomorrow to cover midnight crossovers
      val calendar = Calendar.getInstance()
      val todayYear = calendar.get(Calendar.YEAR)
      val todayMonth = calendar.get(Calendar.MONTH) + 1
      val todayDay = calendar.get(Calendar.DAY_OF_MONTH)

      val scheduledItems = mutableListOf<Pair<PrayerTimeItem, Long>>()

      fun collectPrayersForDate(year: Int, month: Int, day: Int) {
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

        // Include obligatory prayers (Fajr, Dhuhr, Asr, Maghrib, Isha)
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
          // If trigger time is in the future (or within the last 15 seconds)
          if (triggerMs > nowMs - 15_000L) {
            scheduledItems.add(Pair(prayer, triggerMs))
          }
        }
      }

      collectPrayersForDate(todayYear, todayMonth, todayDay)

      // Tomorrow
      val tomorrowCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
      collectPrayersForDate(
        tomorrowCal.get(Calendar.YEAR),
        tomorrowCal.get(Calendar.MONTH) + 1,
        tomorrowCal.get(Calendar.DAY_OF_MONTH)
      )

      // Sort chronological
      scheduledItems.sortBy { it.second }

      // Schedule the next 6 upcoming prayer events
      for ((idx, item) in scheduledItems.take(6).withIndex()) {
        val prayer = item.first
        val triggerMs = item.second
        val mode = modes[prayer.type] ?: PrayerNotificationMode.SOUND

        if (mode == PrayerNotificationMode.OFF) {
          continue
        }

        // 1. Exact Alarm via AlarmManager
        val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
          action = PrayerNotificationManager.ACTION_PRAYER_ALARM
          putExtra(PrayerNotificationManager.EXTRA_PRAYER_TYPE, prayer.type.name)
          putExtra("extra_trigger_time", triggerMs)
        }

        val requestCode = prayer.type.ordinal * 20 + (idx % 10)
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
            Log.d(TAG, "Scheduled exact alarm for ${prayer.type.name} at $triggerMs")
          } catch (e: SecurityException) {
            Log.w(TAG, "Exact alarm permission restricted; falling back to setAndAllowWhileIdle", e)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMs, pendingIntent)
            }
          }
        }

        // 2. WorkManager Fallback
        val delayMs = (triggerMs - nowMs).coerceAtLeast(0L)
        val workInput = workDataOf(
          PrayerTriggerWorker.KEY_PRAYER_TYPE to prayer.type.name
        )
        val workRequest = OneTimeWorkRequestBuilder<PrayerTriggerWorker>()
          .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
          .setInputData(workInput)
          .build()

        workManager.enqueueUniqueWork(
          "prayer_worker_${prayer.type.name}_${triggerMs}",
          ExistingWorkPolicy.REPLACE,
          workRequest
        )
        Log.d(TAG, "Enqueued WorkManager fallback for ${prayer.type.name} in ${delayMs / 1000}s")
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
