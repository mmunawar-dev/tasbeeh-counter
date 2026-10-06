package com.example.repository

import com.example.calculation.PrayerCalculator
import com.example.model.*
import com.example.network.AladhanApiClient
import com.example.network.AladhanResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class AladhanPrayerRepository {

  suspend fun fetchRealtimePrayerTimings(
    city: CityLocation,
    madhab: Madhab,
    method: CalculationMethod,
    manualAdjustments: Map<PrayerType, Int> = emptyMap(),
    notificationModes: Map<PrayerType, PrayerNotificationMode> = emptyMap()
  ): Result<PrayerCalculator.DailyPrayerSchedule> = withContext(Dispatchers.IO) {
    try {
      val response: AladhanResponse = AladhanApiClient.apiService.getTimingsByCity(
        city = city.name,
        country = city.country,
        method = method.aladhanMethodId,
        school = madhab.aladhanSchoolId
      )

      val data = response.data
      if (response.code == 200 && data != null) {
        val timings = data.timings

        // Parse Aladhan "HH:mm" 24h strings
        fun parseToMinutes(timeStr: String, type: PrayerType): Int {
          // Some responses might contain "(EET)" or seconds
          val clean = timeStr.trim().split(" ")[0]
          val parts = clean.split(":")
          val h = parts.getOrNull(0)?.toIntOrNull() ?: 12
          val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
          val adj = manualAdjustments[type] ?: 0
          val total = h * 60 + m + adj
          return (total % 1440 + 1440) % 1440
        }

        val fajrMin = parseToMinutes(timings.fajr, PrayerType.FAJR)
        val sunriseMin = parseToMinutes(timings.sunrise, PrayerType.SUNRISE)
        val dhuhrMin = parseToMinutes(timings.dhuhr, PrayerType.DHUHR)
        val asrMin = parseToMinutes(timings.asr, PrayerType.ASR)
        val maghribMin = parseToMinutes(timings.maghrib, PrayerType.MAGHRIB)
        val ishaMin = parseToMinutes(timings.isha, PrayerType.ISHA)

        val nowMinuteOfDay = PrayerCalculator.getCurrentMinuteOfDay()

        fun buildItem(
          type: PrayerType,
          startMin: Int,
          endMin: Int,
          sunnah: String
        ): PrayerTimeItem {
          val isCurrent = if (startMin <= endMin) {
            nowMinuteOfDay in startMin until endMin
          } else {
            nowMinuteOfDay >= startMin || nowMinuteOfDay < endMin
          }

          val status = when {
            isCurrent -> PrayerStatus.CURRENT
            nowMinuteOfDay > startMin && (startMin < endMin || nowMinuteOfDay >= startMin) -> {
              if (startMin < endMin && nowMinuteOfDay >= endMin) PrayerStatus.PAST
              else PrayerStatus.UPCOMING
            }
            else -> PrayerStatus.UPCOMING
          }

          val remainingText = if (isCurrent) {
            val diff = if (endMin >= nowMinuteOfDay) endMin - nowMinuteOfDay else (1440 - nowMinuteOfDay + endMin)
            val h = diff / 60
            val m = diff % 60
            if (h > 0) "Ends in ${String.format(Locale.US, "%02d", h)}h ${String.format(Locale.US, "%02d", m)}m" else "Ends in ${m}m"
          } else {
            val diff = if (startMin >= nowMinuteOfDay) startMin - nowMinuteOfDay else (1440 - nowMinuteOfDay + startMin)
            val h = diff / 60
            val m = diff % 60
            if (h > 0) "In ${String.format(Locale.US, "%02d", h)}h ${String.format(Locale.US, "%02d", m)}m" else "In ${m}m"
          }

          return PrayerTimeItem(
            type = type,
            timeFormatted = PrayerCalculator.formatMinutes(startMin),
            startMinutes = startMin,
            endMinutes = endMin,
            endFormatted = PrayerCalculator.formatMinutes(endMin),
            status = status,
            notificationMode = notificationModes[type] ?: PrayerNotificationMode.SOUND,
            remainingTimeFormatted = remainingText,
            sunnahInfo = sunnah
          )
        }

        val fajrItem = buildItem(PrayerType.FAJR, fajrMin, sunriseMin, "2 Sunnah Mu'akkadah before Fard")
        val sunriseItem = buildItem(PrayerType.SUNRISE, sunriseMin, dhuhrMin, "Ishraq prayer recommended 15 mins later")
        val dhuhrItem = buildItem(PrayerType.DHUHR, dhuhrMin, asrMin, "4 Sunnah before Fard, 2 after Fard")
        val asrItem = buildItem(PrayerType.ASR, asrMin, maghribMin, "4 Sunnah Ghair Mu'akkadah before Fard")
        val maghribItem = buildItem(PrayerType.MAGHRIB, maghribMin, ishaMin, "2 Sunnah Mu'akkadah after Fard")
        val ishaItem = buildItem(PrayerType.ISHA, ishaMin, fajrMin, "4 Sunnah before Fard, 2 after Fard, 3 Witr Wajib")

        val current = listOf(fajrItem, sunriseItem, dhuhrItem, asrItem, maghribItem, ishaItem)
          .firstOrNull { it.status == PrayerStatus.CURRENT }

        val next = when {
          nowMinuteOfDay < fajrMin -> fajrItem
          nowMinuteOfDay < sunriseMin -> sunriseItem
          nowMinuteOfDay < dhuhrMin -> dhuhrItem
          nowMinuteOfDay < asrMin -> asrItem
          nowMinuteOfDay < maghribMin -> maghribItem
          nowMinuteOfDay < ishaMin -> ishaItem
          else -> fajrItem
        }

        val timeToNext = if (next.startMinutes >= nowMinuteOfDay) {
          next.startMinutes - nowMinuteOfDay
        } else {
          1440 - nowMinuteOfDay + next.startMinutes
        }
        val nh = timeToNext / 60
        val nm = timeToNext % 60
        val timeToNextFormatted = if (nh > 0) "${String.format(Locale.US, "%02d", nh)}h ${String.format(Locale.US, "%02d", nm)}m" else "${nm}m"

        val schedule = PrayerCalculator.DailyPrayerSchedule(
          fajr = fajrItem,
          sunrise = sunriseItem,
          dhuhr = dhuhrItem,
          asr = asrItem,
          maghrib = maghribItem,
          isha = ishaItem,
          currentPrayer = current,
          nextPrayer = next,
          timeToNextFormatted = timeToNextFormatted,
          isBetweenPrayers = current == null || current.type == PrayerType.SUNRISE
        )

        Result.success(schedule)
      } else {
        Result.failure(Exception("Aladhan API response status not OK: ${response.status}"))
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
