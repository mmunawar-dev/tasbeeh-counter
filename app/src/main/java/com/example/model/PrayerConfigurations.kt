package com.example.model

enum class PrayerNotificationMode(
  val label: String
) {
  SOUND("Alarm / Sound"),
  VIBRATE("Vibrate"),
  SILENT("Silent"),
  OFF("Off")
}

enum class Madhab(
  val displayName: String,
  val asrShadowFactor: Double,
  val subtitle: String,
  val description: String
) {
  HANAFI(
    displayName = "Hanafi",
    asrShadowFactor = 2.0,
    subtitle = "Shadow reaches twice object length",
    description = "Asr time begins later when the shadow of an object becomes twice its length plus noon shadow."
  ),
  SHAFI_MALIKI_HANBALI(
    displayName = "Shafi'i, Maliki, Hanbali",
    asrShadowFactor = 1.0,
    subtitle = "Standard shadow reaches object length",
    description = "Asr time begins earlier when the shadow of an object equals its length plus noon shadow."
  )
}

enum class CalculationMethod(
  val displayName: String,
  val shortName: String,
  val fajrAngle: Double,
  val ishaAngle: Double,
  val isIshaFixedMinutes: Boolean = false,
  val ishaMinutesAfterMaghrib: Int = 0,
  val recommendedRegion: String
) {
  KARACHI(
    displayName = "University of Islamic Sciences, Karachi",
    shortName = "Karachi",
    fajrAngle = 18.0,
    ishaAngle = 18.0,
    recommendedRegion = "Pakistan, India, Bangladesh, Afghanistan"
  ),
  UMM_AL_QURA(
    displayName = "Umm al-Qura University, Makkah",
    shortName = "Umm al-Qura",
    fajrAngle = 18.5,
    ishaAngle = 0.0,
    isIshaFixedMinutes = true,
    ishaMinutesAfterMaghrib = 90,
    recommendedRegion = "Saudi Arabia, Arabian Peninsula"
  ),
  MWL(
    displayName = "Muslim World League",
    shortName = "MWL",
    fajrAngle = 18.0,
    ishaAngle = 17.0,
    recommendedRegion = "Europe, Far East, parts of USA"
  ),
  EGYPT(
    displayName = "Egyptian General Authority of Survey",
    shortName = "Egyptian Survey",
    fajrAngle = 19.5,
    ishaAngle = 17.5,
    recommendedRegion = "Egypt, Africa, Syria, Lebanon"
  ),
  ISNA(
    displayName = "Islamic Society of North America",
    shortName = "ISNA",
    fajrAngle = 15.0,
    ishaAngle = 15.0,
    recommendedRegion = "North America (USA & Canada)"
  ),
  DUBAI(
    displayName = "Gulf / Dubai Awqaf",
    shortName = "Dubai Awqaf",
    fajrAngle = 18.2,
    ishaAngle = 18.2,
    recommendedRegion = "UAE, Gulf Countries"
  )
}
