package com.example.model

enum class PrayerStatus {
  PAST,
  CURRENT,
  UPCOMING
}

data class PrayerTimeItem(
  val type: PrayerType,
  val timeFormatted: String,
  val startMinutes: Int,
  val endMinutes: Int,
  val endFormatted: String,
  val status: PrayerStatus,
  val notificationMode: PrayerNotificationMode,
  val remainingTimeFormatted: String,
  val sunnahInfo: String
)
