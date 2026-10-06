package com.example.viewmodel

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculation.PrayerCalculator
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar

class SakinahViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(SakinahUiState())
  val uiState: StateFlow<SakinahUiState> = _uiState.asStateFlow()

  // Track minute of day to refresh prayer calculations in real-time
  private val _currentMinuteTick = MutableStateFlow(PrayerCalculator.getCurrentMinuteOfDay())

  // Navigation backstack
  private val backStack = mutableListOf(Screen.SPLASH)

  init {
    // Initialize date from system calendar
    val cal = Calendar.getInstance()
    _uiState.update {
      it.copy(
        selectedCalendarDateYear = cal.get(Calendar.YEAR),
        selectedCalendarDateMonth = cal.get(Calendar.MONTH) + 1,
        selectedCalendarDateDay = cal.get(Calendar.DAY_OF_MONTH)
      )
    }

    // Auto tick every 15 seconds to keep countdown and prayer status accurate
    viewModelScope.launch {
      while (isActive) {
        delay(15_000)
        _currentMinuteTick.value = PrayerCalculator.getCurrentMinuteOfDay()
      }
    }
  }

  // Combined daily schedule for the currently selected city and date
  val currentSchedule: StateFlow<PrayerCalculator.DailyPrayerSchedule> = combine(
    _uiState,
    _currentMinuteTick
  ) { state, minuteOfDay ->
    PrayerCalculator.calculate(
      year = state.selectedCalendarDateYear,
      month = state.selectedCalendarDateMonth,
      day = state.selectedCalendarDateDay,
      city = state.selectedCity,
      madhab = state.selectedMadhab,
      method = state.calculationMethod,
      manualAdjustmentsMinutes = state.manualAdjustments,
      notificationModes = state.notificationModes,
      nowMinuteOfDay = minuteOfDay
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = PrayerCalculator.calculate(
      year = 2026,
      month = 10,
      day = 6,
      city = CityLocation.DEFAULT_CITY,
      madhab = Madhab.HANAFI,
      method = CalculationMethod.KARACHI
    )
  )

  fun navigateTo(screen: Screen) {
    backStack.add(screen)
    _uiState.update { it.copy(currentScreen = screen) }
  }

  fun navigateBack(): Boolean {
    if (backStack.size > 1) {
      backStack.removeAt(backStack.lastIndex)
      val previous = backStack.last()
      _uiState.update { it.copy(currentScreen = previous) }
      return true
    }
    return false
  }

  fun selectLanguage(language: AppLanguage) {
    _uiState.update { it.copy(selectedLanguage = language) }
  }

  fun selectMadhab(madhab: Madhab) {
    _uiState.update { it.copy(selectedMadhab = madhab) }
  }

  fun selectCalculationMethod(method: CalculationMethod) {
    _uiState.update { it.copy(calculationMethod = method) }
  }

  fun selectCity(city: CityLocation) {
    _uiState.update {
      it.copy(
        selectedCity = city,
        calculationMethod = city.recommendedMethod,
        selectedMadhab = city.recommendedMadhab
      )
    }
  }

  fun cyclePrayerNotification(prayerType: PrayerType) {
    _uiState.update { state ->
      val current = state.notificationModes[prayerType] ?: PrayerNotificationMode.SOUND
      val next = when (current) {
        PrayerNotificationMode.SOUND -> PrayerNotificationMode.VIBRATE
        PrayerNotificationMode.VIBRATE -> PrayerNotificationMode.SILENT
        PrayerNotificationMode.SILENT -> PrayerNotificationMode.OFF
        PrayerNotificationMode.OFF -> PrayerNotificationMode.SOUND
      }
      val updated = state.notificationModes.toMutableMap()
      updated[prayerType] = next
      state.copy(notificationModes = updated)
    }
  }

  fun setPrayerNotification(prayerType: PrayerType, mode: PrayerNotificationMode) {
    _uiState.update { state ->
      val updated = state.notificationModes.toMutableMap()
      updated[prayerType] = mode
      state.copy(notificationModes = updated)
    }
  }

  fun adjustPrayerMinutes(prayerType: PrayerType, delta: Int) {
    _uiState.update { state ->
      val current = state.manualAdjustments[prayerType] ?: 0
      val updated = state.manualAdjustments.toMutableMap()
      updated[prayerType] = (current + delta).coerceIn(-60, 60)
      state.copy(manualAdjustments = updated)
    }
  }

  fun setThemeMode(mode: ThemeMode) {
    _uiState.update { it.copy(themeMode = mode) }
  }

  fun setSelectedCalendarDate(year: Int, month: Int, day: Int) {
    _uiState.update {
      it.copy(
        selectedCalendarDateYear = year,
        selectedCalendarDateMonth = month,
        selectedCalendarDateDay = day
      )
    }
  }

  fun resetCalendarToToday() {
    val cal = Calendar.getInstance()
    setSelectedCalendarDate(
      year = cal.get(Calendar.YEAR),
      month = cal.get(Calendar.MONTH) + 1,
      day = cal.get(Calendar.DAY_OF_MONTH)
    )
  }

  fun openPrayerDetail(prayerItem: PrayerTimeItem) {
    _uiState.update { it.copy(activeDetailPrayer = prayerItem) }
  }

  fun closePrayerDetail() {
    _uiState.update { it.copy(activeDetailPrayer = null) }
  }

  // Tasbeeh functions
  fun incrementTasbeeh(context: Context) {
    _uiState.update { state ->
      val newCount = state.tasbeehCount + 1
      if (newCount >= state.tasbeehTotalTarget) {
        // Trigger completion vibration
        triggerHaptic(context, isTargetReached = true)
        state.copy(
          tasbeehCount = 0,
          tasbeehLaps = state.tasbeehLaps + 1
        )
      } else {
        triggerHaptic(context, isTargetReached = false)
        state.copy(tasbeehCount = newCount)
      }
    }
  }

  fun resetTasbeeh() {
    _uiState.update { it.copy(tasbeehCount = 0, tasbeehLaps = 0) }
  }

  fun selectDhikr(preset: DhikrPreset) {
    _uiState.update {
      it.copy(
        currentDhikr = preset,
        tasbeehTotalTarget = preset.targetCount,
        tasbeehCount = 0
      )
    }
  }

  fun setTasbeehTarget(target: Int) {
    _uiState.update { it.copy(tasbeehTotalTarget = target, tasbeehCount = 0) }
  }

  fun toggleHaptic() {
    _uiState.update { it.copy(isHapticEnabled = !it.isHapticEnabled) }
  }

  fun togglePrePrayerReminder() {
    _uiState.update { it.copy(isPrePrayerReminderEnabled = !it.isPrePrayerReminderEnabled) }
  }

  fun unlockPremium() {
    _uiState.update { it.copy(isPremiumUnlocked = true) }
  }

  fun detectLocation(context: Context) {
    viewModelScope.launch {
      _uiState.update { it.copy(isGpsSearching = true) }
      try {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        var bestLocation: Location? = null
        if (lm != null) {
          try {
            val providers = lm.getProviders(true)
            for (provider in providers) {
              val l = lm.getLastKnownLocation(provider) ?: continue
              if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                bestLocation = l
              }
            }
          } catch (_: SecurityException) {
            // Permission might not be granted
          }
        }

        if (bestLocation != null) {
          // Identify nearest city or create custom location
          val lat = bestLocation.latitude
          val lng = bestLocation.longitude
          val nearest = CityLocation.PRESET_CITIES.minByOrNull { city ->
            val dLat = city.latitude - lat
            val dLng = city.longitude - lng
            dLat * dLat + dLng * dLng
          }

          if (nearest != null) {
            selectCity(
              nearest.copy(
                isGpsDetected = true,
                latitude = lat,
                longitude = lng
              )
            )
          }
        } else {
          // Fallback simulation to Rawalpindi GPS detected
          delay(800)
          selectCity(
            CityLocation.DEFAULT_CITY.copy(isGpsDetected = true)
          )
        }
      } catch (_: Exception) {
        // Fallback gracefully
        selectCity(CityLocation.DEFAULT_CITY)
      } finally {
        _uiState.update { it.copy(isGpsSearching = false) }
      }
    }
  }

  private fun triggerHaptic(context: Context, isTargetReached: Boolean) {
    if (!_uiState.value.isHapticEnabled) return
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        val vibrator = vibratorManager?.defaultVibrator
        if (isTargetReached) {
          vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 70, 70, 100), -1))
        } else {
          vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        }
      } else {
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (isTargetReached) {
          vibrator?.vibrate(longArrayOf(0, 60, 60, 90), -1)
        } else {
          vibrator?.vibrate(20)
        }
      }
    } catch (_: Exception) {
      // Haptics optional
    }
  }
}
