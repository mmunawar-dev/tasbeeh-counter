package com.example.calculation

import com.example.model.AppLanguage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.floor

object HijriCalendarHelper {

  data class HijriDate(
    val day: Int,
    val monthIndex: Int, // 1 to 12
    val year: Int
  ) {
    fun formatted(language: AppLanguage): String {
      val monthName = when (language) {
        AppLanguage.ENGLISH -> HIJRI_MONTHS_EN[monthIndex - 1]
        AppLanguage.URDU -> HIJRI_MONTHS_UR[monthIndex - 1]
        AppLanguage.ARABIC -> HIJRI_MONTHS_AR[monthIndex - 1]
      }
      return when (language) {
        AppLanguage.ENGLISH -> "$day $monthName $year AH"
        AppLanguage.URDU -> "$day $monthName $year ھ"
        AppLanguage.ARABIC -> "$day $monthName $year هـ"
      }
    }
  }

  private val HIJRI_MONTHS_EN = listOf(
    "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
    "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
    "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
  )

  private val HIJRI_MONTHS_AR = listOf(
    "محرم", "صفر", "ربيع الأول", "ربيع الثاني",
    "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
    "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
  )

  private val HIJRI_MONTHS_UR = listOf(
    "محرم", "صفر", "ربیع الاول", "ربیع الثانی",
    "جمادی الاول", "جمادی الثانی", "رجب", "شعبان",
    "رمضان", "شوال", "ذی القعدہ", "ذی الحجہ"
  )

  /**
   * Astronomical conversion from Gregorian date to Umm al-Qura standard Hijri date
   */
  fun getHijriDate(calendar: Calendar): HijriDate {
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var m = month
    var y = year
    if (m < 3) {
      y -= 1
      m += 12
    }

    val a = floor(y / 100.0)
    val b = 2 - a + floor(a / 4.0)
    val jd = floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5

    // Days since Islamic epoch (16 July 622 CE = JD 1948439.5)
    val z = jd - 1948440 + 10632
    val n = floor((z - 1) / 10631.0)
    val zMinus10631N = z - 10631 * n + 354
    val j = (floor((10985 - zMinus10631N) / 5316.0)) * (floor((50 * zMinus10631N) / 17719.0)) +
      (floor(zMinus10631N / 5670.0)) * (floor((43 * zMinus10631N) / 15238.0))
    val zMod = zMinus10631N - (floor((30 - j) / 15.0)) * (floor((17719 * j) / 50.0)) -
      (floor(j / 16.0)) * (floor((15238 * j) / 43.0)) + 29
    val hijriMonth = floor((24 * zMod) / 709.0).toInt()
    val hijriDay = (zMod - floor((709 * hijriMonth) / 24.0)).toInt()
    val hijriYear = (30 * n + j - 30).toInt()

    val safeMonth = ((hijriMonth - 1) % 12 + 12) % 12 + 1
    val safeDay = hijriDay.coerceIn(1, 30)

    return HijriDate(
      day = safeDay,
      monthIndex = safeMonth,
      year = hijriYear
    )
  }

  fun formatGregorianDate(calendar: Calendar, language: AppLanguage): String {
    val locale = when (language) {
      AppLanguage.ENGLISH -> Locale.ENGLISH
      AppLanguage.URDU -> Locale("ur")
      AppLanguage.ARABIC -> Locale("ar")
    }
    val formatter = SimpleDateFormat("EEEE, d MMMM yyyy", locale)
    return formatter.format(calendar.time)
  }
}
