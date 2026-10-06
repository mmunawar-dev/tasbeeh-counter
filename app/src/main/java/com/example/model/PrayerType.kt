package com.example.model

enum class PrayerType(
  val englishName: String,
  val arabicName: String,
  val urduName: String,
  val isObligatory: Boolean
) {
  FAJR("Fajr", "الفجر", "فجر", true),
  SUNRISE("Sunrise", "الشروق", "طلوع آفتاب", false),
  DHUHR("Dhuhr", "الظهر", "ظہر", true),
  ASR("Asr", "العصر", "عصر", true),
  MAGHRIB("Maghrib", "المغرب", "مغرب", true),
  ISHA("Isha", "العشاء", "عشاء", true);

  fun localizedName(language: AppLanguage): String {
    return when (language) {
      AppLanguage.ENGLISH -> englishName
      AppLanguage.URDU -> urduName
      AppLanguage.ARABIC -> arabicName
    }
  }
}
