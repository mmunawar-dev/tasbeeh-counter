package com.example.calculation

import com.example.model.CalculationMethod
import com.example.model.CityLocation
import com.example.model.Madhab
import com.example.model.PrayerNotificationMode
import com.example.model.PrayerStatus
import com.example.model.PrayerTimeItem
import com.example.model.PrayerType
import java.util.Calendar
import java.util.Locale
import kotlin.math.*

object PrayerCalculator {

  data class DailyPrayerSchedule(
    val fajr: PrayerTimeItem,
    val sunrise: PrayerTimeItem,
    val dhuhr: PrayerTimeItem,
    val asr: PrayerTimeItem,
    val maghrib: PrayerTimeItem,
    val isha: PrayerTimeItem,
    val currentPrayer: PrayerTimeItem?,
    val nextPrayer: PrayerTimeItem,
    val timeToNextFormatted: String,
    val isBetweenPrayers: Boolean
  ) {
    val allPrayers: List<PrayerTimeItem>
      get() = listOf(fajr, sunrise, dhuhr, asr, maghrib, isha)

    val obligatoryPrayers: List<PrayerTimeItem>
      get() = listOf(fajr, dhuhr, asr, maghrib, isha)
  }

  fun calculate(
    year: Int,
    month: Int, // 1-12
    day: Int,
    city: CityLocation,
    madhab: Madhab,
    method: CalculationMethod,
    manualAdjustmentsMinutes: Map<PrayerType, Int> = emptyMap(),
    notificationModes: Map<PrayerType, PrayerNotificationMode> = emptyMap(),
    nowMinuteOfDay: Int = getCurrentMinuteOfDay()
  ): DailyPrayerSchedule {
    val lat = city.latitude
    val lng = city.longitude
    val timezone = city.timeZoneOffsetHours

    // Julian Date calculation
    val jd = julianDate(year, month, day)
    val d = jd - 2451545.0

    // Sun coordinates
    val g = fixAngle(357.529 + 0.98560028 * d)
    val q = fixAngle(280.459 + 0.98564736 * d)
    val l = fixAngle(q + 1.915 * sin(Math.toRadians(g)) + 0.020 * sin(Math.toRadians(2 * g)))
    val e = 23.439 - 0.00000036 * d

    val sinL = sin(Math.toRadians(l))
    val cosL = cos(Math.toRadians(l))
    val cosE = cos(Math.toRadians(e))
    val ra = fixAngle(Math.toDegrees(atan2(cosE * sinL, cosL))) / 15.0

    val delta = Math.toDegrees(asin(sin(Math.toRadians(e)) * sinL))
    val eqT = (q / 15.0 - ra) * 60.0 // minutes

    // Solar noon in hours
    val noonHours = 12.0 + timezone - (lng / 15.0) - (eqT / 60.0)

    // Helper to calculate sun angle hour angle
    fun hourAngle(altitudeDegrees: Double): Double {
      val sinAlt = sin(Math.toRadians(altitudeDegrees))
      val sinLat = sin(Math.toRadians(lat))
      val cosLat = cos(Math.toRadians(lat))
      val sinDec = sin(Math.toRadians(delta))
      val cosDec = cos(Math.toRadians(delta))

      val cosH = (sinAlt - (sinLat * sinDec)) / (cosLat * cosDec)
      return if (cosH < -1.0) 180.0
      else if (cosH > 1.0) 0.0
      else Math.toDegrees(acos(cosH))
    }

    // Sunrise & Sunset angle is typically -0.8333 degrees (atmospheric refraction)
    val sunriseSunsetHA = hourAngle(-0.8333)
    val sunriseHours = noonHours - (sunriseSunsetHA / 15.0)
    val sunsetHours = noonHours + (sunriseSunsetHA / 15.0)

    // Fajr
    val fajrHA = hourAngle(-method.fajrAngle)
    val fajrHours = noonHours - (fajrHA / 15.0)

    // Asr
    val shadowFactor = madhab.asrShadowFactor
    val diffLatDec = abs(lat - delta)
    val noonShadow = tan(Math.toRadians(diffLatDec))
    val asrShadow = shadowFactor + noonShadow
    val asrAlt = Math.toDegrees(atan(1.0 / asrShadow))
    val asrHA = hourAngle(asrAlt)
    val asrHours = noonHours + (asrHA / 15.0)

    // Maghrib = Sunset + 1 to 2 minutes safety margin
    val maghribHours = sunsetHours + (2.0 / 60.0)

    // Isha
    val ishaHours = if (method.isIshaFixedMinutes) {
      maghribHours + (method.ishaMinutesAfterMaghrib.toDouble() / 60.0)
    } else {
      val ishaHA = hourAngle(-method.ishaAngle)
      noonHours + (ishaHA / 15.0)
    }

    // Convert to minutes with manual adjustments
    fun toMinutes(hours: Double, type: PrayerType): Int {
      val adj = manualAdjustmentsMinutes[type] ?: 0
      val raw = (hours * 60.0).roundToInt() + adj
      return (raw % 1440 + 1440) % 1440
    }

    val fajrMin = toMinutes(fajrHours, PrayerType.FAJR)
    val sunriseMin = toMinutes(sunriseHours, PrayerType.SUNRISE)
    val dhuhrMin = toMinutes(noonHours + (4.0 / 60.0), PrayerType.DHUHR) // Solar noon + 4 min zawal margin
    val asrMin = toMinutes(asrHours, PrayerType.ASR)
    val maghribMin = toMinutes(maghribHours, PrayerType.MAGHRIB)
    val ishaMin = toMinutes(ishaHours, PrayerType.ISHA)

    // Prayer intervals (starts & ends):
    // Fajr ends at Sunrise
    // Sunrise ends at Dhuhr
    // Dhuhr ends at Asr
    // Asr ends at Maghrib
    // Maghrib ends at Isha
    // Isha ends at Fajr (next day)
    fun buildItem(
      type: PrayerType,
      startMin: Int,
      endMin: Int,
      sunnah: String
    ): PrayerTimeItem {
      val isCurrent = if (startMin <= endMin) {
        nowMinuteOfDay in startMin until endMin
      } else {
        // spans across midnight (e.g. Isha)
        nowMinuteOfDay >= startMin || nowMinuteOfDay < endMin
      }

      val status = when {
        isCurrent -> PrayerStatus.CURRENT
        nowMinuteOfDay > startMin && (startMin < endMin || nowMinuteOfDay >= startMin) -> {
          // If we haven't crossed midnight
          if (startMin < endMin && nowMinuteOfDay >= endMin) PrayerStatus.PAST
          else PrayerStatus.UPCOMING
        }
        else -> PrayerStatus.UPCOMING
      }

      val remainingText = if (isCurrent) {
        val diff = if (endMin >= nowMinuteOfDay) endMin - nowMinuteOfDay else (1440 - nowMinuteOfDay + endMin)
        val h = diff / 60
        val m = diff % 60
        if (h > 0) "Ends in ${h.pad2()}h ${m.pad2()}m" else "Ends in ${m}m"
      } else {
        val diff = if (startMin >= nowMinuteOfDay) startMin - nowMinuteOfDay else (1440 - nowMinuteOfDay + startMin)
        val h = diff / 60
        val m = diff % 60
        if (h > 0) "In ${h.pad2()}h ${m.pad2()}m" else "In ${m}m"
      }

      return PrayerTimeItem(
        type = type,
        timeFormatted = formatMinutes(startMin),
        startMinutes = startMin,
        endMinutes = endMin,
        endFormatted = formatMinutes(endMin),
        status = status,
        notificationMode = notificationModes[type] ?: PrayerNotificationMode.SOUND,
        remainingTimeFormatted = remainingText,
        sunnahInfo = sunnah
      )
    }

    val fajrItem = buildItem(
      PrayerType.FAJR,
      fajrMin,
      sunriseMin,
      "2 Sunnah Mu'akkadah before Fard (highly emphasized)"
    )
    val sunriseItem = buildItem(
      PrayerType.SUNRISE,
      sunriseMin,
      dhuhrMin,
      "Prohibited to pray at exact sunrise. Ishraq prayer recommended 15 mins later."
    )
    val dhuhrItem = buildItem(
      PrayerType.DHUHR,
      dhuhrMin,
      asrMin,
      "4 Sunnah before Fard, 2 Sunnah after Fard"
    )
    val asrItem = buildItem(
      PrayerType.ASR,
      asrMin,
      maghribMin,
      "4 Sunnah Ghair Mu'akkadah before Fard"
    )
    val maghribItem = buildItem(
      PrayerType.MAGHRIB,
      maghribMin,
      ishaMin,
      "2 Sunnah Mu'akkadah after Fard, Awwabin optional"
    )
    val ishaItem = buildItem(
      PrayerType.ISHA,
      ishaMin,
      fajrMin,
      "4 Sunnah before Fard, 2 Sunnah after Fard, 3 Witr Wajib"
    )

    // Determine current prayer and next prayer
    val items = listOf(fajrItem, sunriseItem, dhuhrItem, asrItem, maghribItem, ishaItem)
    val current = items.firstOrNull { it.status == PrayerStatus.CURRENT }

    // Next prayer determination
    val next = when {
      nowMinuteOfDay < fajrMin -> fajrItem
      nowMinuteOfDay < sunriseMin -> sunriseItem
      nowMinuteOfDay < dhuhrMin -> dhuhrItem
      nowMinuteOfDay < asrMin -> asrItem
      nowMinuteOfDay < maghribMin -> maghribItem
      nowMinuteOfDay < ishaMin -> ishaItem
      else -> fajrItem // wraps to tomorrow Fajr
    }

    val timeToNext = if (next.startMinutes >= nowMinuteOfDay) {
      next.startMinutes - nowMinuteOfDay
    } else {
      1440 - nowMinuteOfDay + next.startMinutes
    }
    val nh = timeToNext / 60
    val nm = timeToNext % 60
    val timeToNextFormatted = if (nh > 0) "${nh.pad2()}h ${nm.pad2()}m" else "${nm}m"

    val isBetween = current == null || current.type == PrayerType.SUNRISE

    return DailyPrayerSchedule(
      fajr = fajrItem,
      sunrise = sunriseItem,
      dhuhr = dhuhrItem,
      asr = asrItem,
      maghrib = maghribItem,
      isha = ishaItem,
      currentPrayer = current,
      nextPrayer = next,
      timeToNextFormatted = timeToNextFormatted,
      isBetweenPrayers = isBetween
    )
  }

  private fun julianDate(year: Int, month: Int, day: Int): Double {
    var y = year
    var m = month
    if (m <= 2) {
      y -= 1
      m += 12
    }
    val a = (y / 100.0).toInt()
    val b = 2 - a + (a / 4.0).toInt()
    return (365.25 * (y + 4716)).toInt() + (30.6001 * (m + 1)).toInt() + day + b - 1524.5
  }

  private fun fixAngle(angle: Double): Double {
    var a = angle - 360.0 * (floor(angle / 360.0))
    if (a < 0.0) a += 360.0
    return a
  }

  fun formatMinutes(totalMinutes: Int): String {
    val mins = (totalMinutes % 1440 + 1440) % 1440
    val hour24 = mins / 60
    val minute = mins % 60
    val isPm = hour24 >= 12
    val hour12 = when (hour24) {
      0 -> 12
      in 1..12 -> hour24
      else -> hour24 - 12
    }
    val amPm = if (isPm) "PM" else "AM"
    return String.format(Locale.US, "%02d:%02d %s", hour12, minute, amPm)
  }

  fun getCurrentMinuteOfDay(): Int {
    val cal = Calendar.getInstance()
    return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
  }

  private fun Int.pad2(): String = String.format(Locale.US, "%02d", this)
}
