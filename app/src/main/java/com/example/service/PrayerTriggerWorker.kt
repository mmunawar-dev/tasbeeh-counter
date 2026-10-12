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
    const val KEY_OCCURRENCE_ID = "key_occurrence_id"
    private const val TAG = "PrayerTriggerWorker"
  }

  override suspend fun doWork(): Result {
    val prayerTypeName = inputData.getString(KEY_PRAYER_TYPE)
    val occurrenceId = inputData.getString(KEY_OCCURRENCE_ID)
    if (prayerTypeName == null) {
      Log.e(TAG, "No prayer type provided in Worker inputData")
      return Result.failure()
    }

    return try {
      Log.d(TAG, "Executing PrayerTriggerWorker for $prayerTypeName (occurrenceId=$occurrenceId)")
      PrayerNotificationManager.showPrayerNotification(
        context = applicationContext,
        prayerTypeName = prayerTypeName,
        occurrenceId = occurrenceId,
        isTriggerFromWorker = true
      )
      Result.success()
    } catch (e: Exception) {
      Log.e(TAG, "Error executing PrayerTriggerWorker for $prayerTypeName", e)
      Result.failure()
    }
  }
}
