package com.example.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class PrayerScheduleWorker(
  context: Context,
  params: WorkerParameters
) : CoroutineWorker(context, params) {

  companion object {
    private const val TAG = "PrayerScheduleWorker"
  }

  override suspend fun doWork(): Result {
    return try {
      Log.d(TAG, "Executing PrayerScheduleWorker daily prayer times sync")
      PrayerNotificationScheduler.scheduleAll(applicationContext)
      Result.success()
    } catch (e: Exception) {
      Log.e(TAG, "Error in PrayerScheduleWorker", e)
      Result.retry()
    }
  }
}
