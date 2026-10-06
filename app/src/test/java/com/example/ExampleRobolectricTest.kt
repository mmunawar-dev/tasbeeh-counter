package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.calculation.HijriCalendarHelper
import com.example.calculation.PrayerCalculator
import com.example.model.CalculationMethod
import com.example.model.CityLocation
import com.example.model.Madhab
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Calendar

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Sakinah", appName)
  }

  @Test
  fun `prayer calculation computes valid timings for Rawalpindi`() {
    val schedule = PrayerCalculator.calculate(
      year = 2026,
      month = 10,
      day = 6,
      city = CityLocation.DEFAULT_CITY,
      madhab = Madhab.HANAFI,
      method = CalculationMethod.KARACHI
    )

    assertNotNull(schedule.fajr)
    assertNotNull(schedule.dhuhr)
    assertNotNull(schedule.asr)
    assertNotNull(schedule.maghrib)
    assertNotNull(schedule.isha)

    // Fajr must precede Dhuhr, Asr, Maghrib
    assertTrue(schedule.fajr.startMinutes < schedule.dhuhr.startMinutes)
    assertTrue(schedule.dhuhr.startMinutes < schedule.asr.startMinutes)
    assertTrue(schedule.asr.startMinutes < schedule.maghrib.startMinutes)
  }

  @Test
  fun `hijri date conversion produces valid islamic date`() {
    val cal = Calendar.getInstance().apply {
      set(Calendar.YEAR, 2026)
      set(Calendar.MONTH, Calendar.OCTOBER)
      set(Calendar.DAY_OF_MONTH, 6)
    }

    val hijri = HijriCalendarHelper.getHijriDate(cal)
    assertTrue(hijri.year >= 1447)
    assertTrue(hijri.monthIndex in 1..12)
    assertTrue(hijri.day in 1..30)
  }

  @Test
  fun `notification modes cycle correctly and support all alert types`() {
    val modes = com.example.model.PrayerNotificationMode.entries
    assertEquals(4, modes.size)
    assertTrue(modes.contains(com.example.model.PrayerNotificationMode.SOUND))
    assertTrue(modes.contains(com.example.model.PrayerNotificationMode.VIBRATE))
    assertTrue(modes.contains(com.example.model.PrayerNotificationMode.SILENT))
    assertTrue(modes.contains(com.example.model.PrayerNotificationMode.OFF))
  }
}
