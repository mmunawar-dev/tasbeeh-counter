package com.example.model

enum class Screen {
  SPLASH,
  LANGUAGE_SETUP,
  ONBOARDING,
  FIQH_SETUP,
  LOCATION_SETUP,
  HOME,
  CITY_SELECT,
  PRAYER_SETTINGS,
  CALENDAR_VIEW,
  ALLAH_NAMES,
  ALLAH_NAME_DETAIL,
  TASBEEH,
  QIBLA,
  DUA_CATEGORIES,
  DUA_LIST,
  DUA_DETAIL,
  LIVE_MAKKAH,
  LIVE_MADINAH,
  APP_SETTINGS,
  FIQH_SETTINGS,
  LANGUAGE_SETTINGS,
  PREMIUM
}

enum class ThemeMode(val label: String) {
  SYSTEM("System Default"),
  LIGHT("Light"),
  DARK("Dark")
}

data class SakinahUiState(
  val currentScreen: Screen = Screen.SPLASH,
  val selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
  val selectedMadhab: Madhab = Madhab.HANAFI,
  val calculationMethod: CalculationMethod = CalculationMethod.KARACHI,
  val selectedCity: CityLocation = CityLocation.DEFAULT_CITY,
  val notificationModes: Map<PrayerType, PrayerNotificationMode> = mapOf(
    PrayerType.FAJR to PrayerNotificationMode.SOUND,
    PrayerType.SUNRISE to PrayerNotificationMode.SILENT,
    PrayerType.DHUHR to PrayerNotificationMode.SOUND,
    PrayerType.ASR to PrayerNotificationMode.SOUND,
    PrayerType.MAGHRIB to PrayerNotificationMode.SOUND,
    PrayerType.ISHA to PrayerNotificationMode.SOUND
  ),
  val manualAdjustments: Map<PrayerType, Int> = emptyMap(),
  val themeMode: ThemeMode = ThemeMode.SYSTEM,
  val isGpsSearching: Boolean = false,
  val selectedCalendarDateYear: Int = 2026,
  val selectedCalendarDateMonth: Int = 10,
  val selectedCalendarDateDay: Int = 6,
  val activeDetailPrayer: PrayerTimeItem? = null,

  // Tasbeeh state & 5 distinct styles
  val currentDhikr: DhikrPreset = DhikrPreset.PRESETS.first(),
  val tasbeehCount: Int = 0,
  val tasbeehTotalTarget: Int = 33,
  val tasbeehLaps: Int = 0,
  val selectedTasbeehStyle: TasbeehStyle = TasbeehStyle.CIRCULAR,
  val isHapticEnabled: Boolean = true,

  // Allah's Names state
  val selectedAllahName: AllahName? = null,

  // Duas state
  val selectedDuaCategory: DuaCategory? = null,
  val selectedDuaItem: DuaItem? = null,

  // Audio system state
  val activeAudioTrack: AudioTrack? = null,

  // Qibla state
  val qiblaHeadingDegrees: Float = 0f,
  val qiblaBearingDegrees: Float = 262f, // Default for Rawalpindi is ~262° WSW
  val isQiblaAligned: Boolean = false,

  // Subscription & settings
  val isPremiumUnlocked: Boolean = false,
  val prePrayerReminderMinutes: Int = 15,
  val isPrePrayerReminderEnabled: Boolean = true
)
