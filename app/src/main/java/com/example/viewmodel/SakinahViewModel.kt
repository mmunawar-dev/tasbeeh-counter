package com.example.viewmodel

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.calculation.PrayerCalculator
import com.example.calculation.QiblaCalculator
import com.example.model.*
import com.example.repository.AladhanPrayerRepository
import com.example.service.AudioPlayerService
import kotlinx.coroutines.Job
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
import kotlin.math.abs

class SakinahViewModel : ViewModel() {

  private val aladhanRepository = AladhanPrayerRepository()

  private val _uiState = MutableStateFlow(SakinahUiState())
  val uiState: StateFlow<SakinahUiState> = _uiState.asStateFlow()

  val uiStateLiveData: LiveData<SakinahUiState> = _uiState.asLiveData(viewModelScope.coroutineContext)

  private val _currentMinuteTick = MutableStateFlow(PrayerCalculator.getCurrentMinuteOfDay())

  private val _aladhanStatus = MutableLiveData("Aladhan API: Synchronizing...")
  val aladhanStatusLiveData: LiveData<String> = _aladhanStatus

  private val _isApiLoading = MutableLiveData(false)
  val isApiLoadingLiveData: LiveData<Boolean> = _isApiLoading

  private val _selectedPlan = MutableLiveData(PremiumPlan.YEARLY)
  val selectedPlanLiveData: LiveData<PremiumPlan> = _selectedPlan

  private val _apiSchedule = MutableStateFlow<PrayerCalculator.DailyPrayerSchedule?>(null)

  private val backStack = mutableListOf(Screen.SPLASH)
  private var audioJob: Job? = null

  init {
    val cal = Calendar.getInstance()
    val initialBearing = QiblaCalculator.calculateQiblaBearing(
      CityLocation.DEFAULT_CITY.latitude,
      CityLocation.DEFAULT_CITY.longitude
    )

    _uiState.update {
      it.copy(
        selectedCalendarDateYear = cal.get(Calendar.YEAR),
        selectedCalendarDateMonth = cal.get(Calendar.MONTH) + 1,
        selectedCalendarDateDay = cal.get(Calendar.DAY_OF_MONTH),
        qiblaBearingDegrees = initialBearing
      )
    }

    viewModelScope.launch {
      while (isActive) {
        delay(15_000)
        _currentMinuteTick.value = PrayerCalculator.getCurrentMinuteOfDay()
      }
    }

    viewModelScope.launch {
      AudioPlayerService.playbackState.collect { playback ->
        if (playback.isServiceRunning) {
          val durationSec = if (playback.durationMs > 0) playback.durationMs / 1000 else 225
          val currentSec = playback.currentPositionMs / 1000
          val fraction = if (playback.durationMs > 0) {
            playback.currentPositionMs.toFloat() / playback.durationMs.toFloat()
          } else 0f

          val track = AudioTrack(
            id = "asma_ul_husna_full",
            title = playback.trackTitle,
            arabicTitle = "أسماء الله الحسنى",
            subtitle = playback.trackSubtitle,
            audioSource = "allah_names.mp3",
            isPlaying = playback.isPlaying,
            currentPositionSeconds = currentSec,
            durationSeconds = durationSec,
            progressFraction = fraction
          )
          _uiState.update { it.copy(activeAudioTrack = track) }
        } else if (_uiState.value.activeAudioTrack?.id == "asma_ul_husna_full") {
          _uiState.update { it.copy(activeAudioTrack = null) }
        }
      }
    }

    fetchAladhanPrayerTimes()
  }

  val currentSchedule: StateFlow<PrayerCalculator.DailyPrayerSchedule> = combine(
    _uiState,
    _currentMinuteTick,
    _apiSchedule
  ) { state, minuteOfDay, apiData ->
    if (apiData != null &&
      state.selectedCalendarDateDay == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    ) {
      apiData
    } else {
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
    }
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

  val scheduleLiveData: LiveData<PrayerCalculator.DailyPrayerSchedule> =
    currentSchedule.asLiveData(viewModelScope.coroutineContext)

  fun fetchAladhanPrayerTimes() {
    viewModelScope.launch {
      _isApiLoading.value = true
      _aladhanStatus.value = "Aladhan API: Fetching real-time timings..."
      val state = _uiState.value
      val result = aladhanRepository.fetchRealtimePrayerTimings(
        city = state.selectedCity,
        madhab = state.selectedMadhab,
        method = state.calculationMethod,
        manualAdjustments = state.manualAdjustments,
        notificationModes = state.notificationModes
      )

      result.onSuccess { schedule ->
        _apiSchedule.value = schedule
        _isApiLoading.value = false
        _aladhanStatus.value = "Aladhan API: Real-time timings live"
      }.onFailure {
        _isApiLoading.value = false
        _aladhanStatus.value = "Aladhan API: Offline (using solar calculator)"
      }
    }
  }

  fun selectPremiumPlan(plan: PremiumPlan) {
    _selectedPlan.value = plan
  }

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
    fetchAladhanPrayerTimes()
  }

  fun selectCalculationMethod(method: CalculationMethod) {
    _uiState.update { it.copy(calculationMethod = method) }
    fetchAladhanPrayerTimes()
  }

  fun selectCity(city: CityLocation) {
    val newBearing = QiblaCalculator.calculateQiblaBearing(city.latitude, city.longitude)
    _uiState.update {
      it.copy(
        selectedCity = city,
        calculationMethod = city.recommendedMethod,
        selectedMadhab = city.recommendedMadhab,
        qiblaBearingDegrees = newBearing
      )
    }
    fetchAladhanPrayerTimes()
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
    fetchAladhanPrayerTimes()
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
    fetchAladhanPrayerTimes()
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

  fun setTasbeehStyle(style: TasbeehStyle) {
    _uiState.update { it.copy(selectedTasbeehStyle = style) }
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

  // Allah's Names functions
  fun selectAllahName(name: AllahName) {
    _uiState.update { it.copy(selectedAllahName = name) }
  }

  fun clearSelectedAllahName() {
    _uiState.update { it.copy(selectedAllahName = null) }
  }

  fun playAllahNamesFullRecitation(context: Context) {
    AudioPlayerService.startPlaying(
      context = context,
      title = "Asma-ul-Husna (99 Names of Allah)",
      subtitle = "Complete Melodious Recitation"
    )
  }

  fun playAllahNameAudio(context: Context, name: AllahName) {
    AudioPlayerService.startPlaying(
      context = context,
      title = "${name.number}. ${name.transliteration} (${name.arabic})",
      subtitle = name.englishMeaning
    )
  }

  fun playAllahNameAudio(name: AllahName) {
    val track = AudioTrack(
      id = "allah_name_${name.number}",
      title = "${name.number}. ${name.transliteration}",
      arabicTitle = name.arabic,
      subtitle = name.englishMeaning,
      audioSource = "allah_names.mp3",
      isPlaying = true,
      durationSeconds = 12
    )
    startAudioTrack(track)
  }

  fun toggleAudioPlayback(context: Context) {
    if (AudioPlayerService.playbackState.value.isServiceRunning) {
      AudioPlayerService.togglePlayback(context)
    } else {
      AudioPlayerService.startPlaying(context)
    }
  }

  fun stopAudioPlayback(context: Context) {
    AudioPlayerService.stopPlayback(context)
    _uiState.update { it.copy(activeAudioTrack = null) }
  }

  // Duas functions
  fun selectDuaCategory(category: DuaCategory) {
    _uiState.update { it.copy(selectedDuaCategory = category) }
  }

  fun selectDuaItem(item: DuaItem) {
    _uiState.update { it.copy(selectedDuaItem = item) }
  }

  fun clearSelectedDua() {
    _uiState.update { it.copy(selectedDuaItem = null) }
  }

  fun playDuaAudio(dua: DuaItem) {
    val track = AudioTrack(
      id = "dua_${dua.id}",
      title = dua.title,
      arabicTitle = dua.arabic.take(28) + "...",
      subtitle = dua.reference,
      audioSource = "dua_audio",
      isPlaying = true,
      durationSeconds = 25
    )
    startAudioTrack(track)
  }

  // Audio system controller
  private fun startAudioTrack(track: AudioTrack) {
    audioJob?.cancel()
    _uiState.update { it.copy(activeAudioTrack = track) }

    audioJob = viewModelScope.launch {
      var currentSec = 0
      while (isActive && currentSec < track.durationSeconds) {
        delay(1000)
        currentSec++
        val fraction = currentSec.toFloat() / track.durationSeconds.toFloat()
        _uiState.update { state ->
          state.activeAudioTrack?.let {
            state.copy(
              activeAudioTrack = it.copy(
                currentPositionSeconds = currentSec,
                progressFraction = fraction
              )
            )
          } ?: state
        }
      }
      // Completed playback
      _uiState.update { state ->
        state.activeAudioTrack?.let {
          state.copy(activeAudioTrack = it.copy(isPlaying = false, progressFraction = 1f))
        } ?: state
      }
    }
  }

  fun toggleAudioPlayPause() {
    _uiState.update { state ->
      val current = state.activeAudioTrack ?: return@update state
      val nextPlaying = !current.isPlaying
      state.copy(activeAudioTrack = current.copy(isPlaying = nextPlaying))
    }
  }

  fun stopAudio() {
    audioJob?.cancel()
    _uiState.update { it.copy(activeAudioTrack = null) }
  }

  // Qibla heading update
  fun updateQiblaHeading(azimuthDegrees: Float) {
    _uiState.update { state ->
      val bearing = state.qiblaBearingDegrees
      val diff = abs(azimuthDegrees - bearing)
      val isAligned = diff <= 4f || diff >= 356f
      state.copy(
        qiblaHeadingDegrees = azimuthDegrees,
        isQiblaAligned = isAligned
      )
    }
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
          }
        }

        if (bestLocation != null) {
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
          delay(800)
          selectCity(CityLocation.DEFAULT_CITY.copy(isGpsDetected = true))
        }
      } catch (_: Exception) {
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
    }
  }
}
