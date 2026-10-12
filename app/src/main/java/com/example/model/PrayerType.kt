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
      AppLanguage.ARABIC, AppLanguage.PERSIAN -> arabicName
      else -> englishName
    }
  }

  val iconRes: Int
    get() = when (this) {
      FAJR -> com.example.R.drawable.ic_prayer_fajr
      SUNRISE -> com.example.R.drawable.ic_prayer_sunrise
      DHUHR -> com.example.R.drawable.ic_prayer_dhuhr
      ASR -> com.example.R.drawable.ic_prayer_asr
      MAGHRIB -> com.example.R.drawable.ic_prayer_maghrib
      ISHA -> com.example.R.drawable.ic_prayer_isha
    }
}
