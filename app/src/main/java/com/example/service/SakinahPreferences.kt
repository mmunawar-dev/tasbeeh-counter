package com.example.service

import android.content.Context
import android.content.SharedPreferences
import com.example.model.CalculationMethod
import com.example.model.CityLocation
import com.example.model.Madhab
import com.example.model.PrayerNotificationMode
import com.example.model.PrayerType
import com.example.model.TasbeehClickMode

object SakinahPreferences {

  private const val PREFS_NAME = "sakinah_prefs"

  // Tasbeeh
  private const val KEY_TASBEEH_CLICK_MODE = "tasbeeh_click_mode"

  // Theme
  private const val KEY_IS_DARK_THEME = "pref_is_dark_theme"

  // Location / City
  private const val KEY_CITY_ID = "pref_city_id"
  private const val KEY_CITY_NAME = "pref_city_name"
  private const val KEY_CITY_COUNTRY = "pref_city_country"
  private const val KEY_CITY_LAT = "pref_city_lat"
  private const val KEY_CITY_LNG = "pref_city_lng"
  private const val KEY_CITY_TZ = "pref_city_tz"
  private const val KEY_CITY_GPS = "pref_city_gps"

  // Madhab & Method
  private const val KEY_MADHAB = "pref_madhab"
  private const val KEY_CALC_METHOD = "pref_calc_method"

  // Deduplication prefix
  private const val KEY_DEDUP_PREFIX = "dedup_prayer_"

  private fun getPrefs(context: Context): SharedPreferences {
    return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  }

  // --- Tasbeeh Click Mode ---
  fun getTasbeehClickMode(context: Context): TasbeehClickMode {
    val name = getPrefs(context).getString(KEY_TASBEEH_CLICK_MODE, TasbeehClickMode.RING.name)
    return try {
      TasbeehClickMode.valueOf(name ?: TasbeehClickMode.RING.name)
    } catch (_: Exception) {
      TasbeehClickMode.RING
    }
  }

  fun saveTasbeehClickMode(context: Context, mode: TasbeehClickMode) {
    getPrefs(context).edit().putString(KEY_TASBEEH_CLICK_MODE, mode.name).apply()
  }

  // --- City Location ---
  fun getSavedCity(context: Context): CityLocation {
    val prefs = getPrefs(context)
    val id = prefs.getString(KEY_CITY_ID, null) ?: return CityLocation.DEFAULT_CITY
    val name = prefs.getString(KEY_CITY_NAME, CityLocation.DEFAULT_CITY.name) ?: CityLocation.DEFAULT_CITY.name
    val country = prefs.getString(KEY_CITY_COUNTRY, CityLocation.DEFAULT_CITY.country) ?: CityLocation.DEFAULT_CITY.country
    val lat = prefs.getFloat(KEY_CITY_LAT, CityLocation.DEFAULT_CITY.latitude.toFloat()).toDouble()
    val lng = prefs.getFloat(KEY_CITY_LNG, CityLocation.DEFAULT_CITY.longitude.toFloat()).toDouble()
    val tz = prefs.getFloat(KEY_CITY_TZ, CityLocation.DEFAULT_CITY.timeZoneOffsetHours.toFloat()).toDouble()
    val isGps = prefs.getBoolean(KEY_CITY_GPS, false)
    val method = getSavedCalculationMethod(context)
    val madhab = getSavedMadhab(context)

    return CityLocation(
      id = id,
      name = name,
      country = country,
      latitude = lat,
      longitude = lng,
      timeZoneOffsetHours = tz,
      isGpsDetected = isGps,
      recommendedMethod = method,
      recommendedMadhab = madhab
    )
  }

  fun saveCity(context: Context, city: CityLocation) {
    getPrefs(context).edit()
      .putString(KEY_CITY_ID, city.id)
      .putString(KEY_CITY_NAME, city.name)
      .putString(KEY_CITY_COUNTRY, city.country)
      .putFloat(KEY_CITY_LAT, city.latitude.toFloat())
      .putFloat(KEY_CITY_LNG, city.longitude.toFloat())
      .putFloat(KEY_CITY_TZ, city.timeZoneOffsetHours.toFloat())
      .putBoolean(KEY_CITY_GPS, city.isGpsDetected)
      .apply()
  }

  // --- Madhab ---
  fun getSavedMadhab(context: Context): Madhab {
    val name = getPrefs(context).getString(KEY_MADHAB, Madhab.HANAFI.name)
    return try {
      Madhab.valueOf(name ?: Madhab.HANAFI.name)
    } catch (_: Exception) {
      Madhab.HANAFI
    }
  }

  fun saveMadhab(context: Context, madhab: Madhab) {
    getPrefs(context).edit().putString(KEY_MADHAB, madhab.name).apply()
  }

  // --- Calculation Method ---
  fun getSavedCalculationMethod(context: Context): CalculationMethod {
    val name = getPrefs(context).getString(KEY_CALC_METHOD, CalculationMethod.KARACHI.name)
    return try {
      CalculationMethod.valueOf(name ?: CalculationMethod.KARACHI.name)
    } catch (_: Exception) {
      CalculationMethod.KARACHI
    }
  }

  fun saveCalculationMethod(context: Context, method: CalculationMethod) {
    getPrefs(context).edit().putString(KEY_CALC_METHOD, method.name).apply()
  }

  // --- Notification Modes ---
  fun getSavedNotificationModes(context: Context): Map<PrayerType, PrayerNotificationMode> {
    val prefs = getPrefs(context)
    val map = mutableMapOf<PrayerType, PrayerNotificationMode>()
    for (type in PrayerType.values()) {
      val defaultMode = when (type) {
        PrayerType.SUNRISE -> PrayerNotificationMode.SILENT
        else -> PrayerNotificationMode.SOUND
      }
      val saved = prefs.getString("notif_mode_${type.name}", defaultMode.name)
      val mode = try {
        PrayerNotificationMode.valueOf(saved ?: defaultMode.name)
      } catch (_: Exception) {
        defaultMode
      }
      map[type] = mode
    }
    return map
  }

  fun saveNotificationModes(context: Context, modes: Map<PrayerType, PrayerNotificationMode>) {
    val editor = getPrefs(context).edit()
    for ((type, mode) in modes) {
      editor.putString("notif_mode_${type.name}", mode.name)
    }
    editor.apply()
  }

  // --- Manual Adjustments ---
  fun getSavedManualAdjustments(context: Context): Map<PrayerType, Int> {
    val prefs = getPrefs(context)
    val map = mutableMapOf<PrayerType, Int>()
    for (type in PrayerType.values()) {
      val adj = prefs.getInt("adj_min_${type.name}", 0)
      if (adj != 0) {
        map[type] = adj
      }
    }
    return map
  }

  fun saveManualAdjustments(context: Context, adjustments: Map<PrayerType, Int>) {
    val editor = getPrefs(context).edit()
    for (type in PrayerType.values()) {
      val adj = adjustments[type] ?: 0
      editor.putInt("adj_min_${type.name}", adj)
    }
    editor.apply()
  }

  // --- De-duplication ---
  fun isNotificationDeliveredRecently(context: Context, key: String): Boolean {
    val lastTime = getPrefs(context).getLong(KEY_DEDUP_PREFIX + key, 0L)
    // 2 hours window prevents any duplicate notifications for the same prayer occurrence
    return (System.currentTimeMillis() - lastTime) < 2 * 60 * 60 * 1000L
  }

  fun recordNotificationDelivered(context: Context, key: String) {
    getPrefs(context).edit()
      .putLong(KEY_DEDUP_PREFIX + key, System.currentTimeMillis())
      .commit()
  }

  // --- Theme Mode ---
  fun isDarkTheme(context: Context): Boolean {
    return getPrefs(context).getBoolean(KEY_IS_DARK_THEME, false)
  }

  fun saveDarkTheme(context: Context, isDark: Boolean) {
    getPrefs(context).edit().putBoolean(KEY_IS_DARK_THEME, isDark).apply()
  }

  // --- Language ---
  private const val KEY_LANGUAGE_CODE = "pref_language_code"

  fun getSavedLanguage(context: Context): com.example.model.AppLanguage {
    val code = getPrefs(context).getString(KEY_LANGUAGE_CODE, com.example.model.AppLanguage.ENGLISH.code)
      ?: com.example.model.AppLanguage.ENGLISH.code
    return com.example.model.AppLanguage.entries.firstOrNull { it.code == code } ?: com.example.model.AppLanguage.ENGLISH
  }

  fun saveLanguage(context: Context, language: com.example.model.AppLanguage) {
    getPrefs(context).edit().putString(KEY_LANGUAGE_CODE, language.code).apply()
  }
}
