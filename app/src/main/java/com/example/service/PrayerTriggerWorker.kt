package com.example.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class PrayerTriggerWorker(
  context: Context,
  params: WorkerParameters
) : CoroutineWorker(context, params) {

  companion object {
    const val KEY_PRAYER_TYPE = "key_prayer_type"
    private const val TAG = "PrayerTriggerWorker"
  }

  override suspend fun doWork(): Result {
    val prayerTypeName = inputData.getString(KEY_PRAYER_TYPE)
    if (prayerTypeName == null) {
      Log.e(TAG, "No prayer type provided in Worker inputData")
      return Result.failure()
    }

    return try {
      Log.d(TAG, "Executing PrayerTriggerWorker for $prayerTypeName")
      PrayerNotificationManager.showPrayerNotification(
        applicationContext,
        prayerTypeName,
        isTriggerFromWorker = true
      )
      Result.success()
    } catch (e: Exception) {
      Log.e(TAG, "Error executing PrayerTriggerWorker for $prayerTypeName", e)
      Result.failure()
    }
  }
}
