package com.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.Screen
import com.example.model.ThemeMode
import com.example.service.AudioPlayerService
import com.example.ui.components.AllahNameDetailBottomSheet
import com.example.ui.components.AudioMiniPlayer
import com.example.ui.components.ExitBottomSheet
import com.example.ui.components.PrayerDetailBottomSheet
import com.example.ui.screens.*
import com.example.ui.theme.SakinahTheme
import com.example.viewmodel.SakinahViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val viewModel: SakinahViewModel = viewModel()
      val uiState by viewModel.uiState.collectAsStateWithLifecycle()
      val schedule by viewModel.currentSchedule.collectAsStateWithLifecycle()
      val audioPlaybackState by AudioPlayerService.playbackState.collectAsStateWithLifecycle()
      val context = LocalContext.current
      var showExitBottomSheet by remember { mutableStateOf(false) }

      // Automatic location detection on first setup / Home launch if permission is granted
      val locationPermissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
      ) { permissions ->
        val coarseGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val fineGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (coarseGranted || fineGranted) {
          viewModel.detectLocation(context)
        }
      }

      val isDark = when (uiState.themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
      }

      SakinahTheme(darkTheme = isDark) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
          Surface(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {
              when (uiState.currentScreen) {
                Screen.SPLASH -> {
                  SplashScreen(
                    onProceed = { viewModel.navigateTo(Screen.LANGUAGE_SETUP) }
                  )
                }

                Screen.LANGUAGE_SETUP -> {
                  LanguageSelectScreen(
                    selectedLanguage = uiState.selectedLanguage,
                    onLanguageSelected = { viewModel.selectLanguage(it) },
                    onContinue = { viewModel.navigateTo(Screen.ONBOARDING) }
                  )
                }

                Screen.ONBOARDING -> {
                  BackHandler { viewModel.navigateBack() }
                  OnboardingScreen(
                    onComplete = { viewModel.navigateTo(Screen.FIQH_SETUP) }
                  )
                }

                Screen.FIQH_SETUP -> {
                  BackHandler { viewModel.navigateBack() }
                  FiqhSelectScreen(
                    selectedMadhab = uiState.selectedMadhab,
                    selectedMethod = uiState.calculationMethod,
                    onMadhabSelected = { viewModel.selectMadhab(it) },
                    onMethodSelected = { viewModel.selectCalculationMethod(it) },
                    onContinue = {
                      // Automatically detect location when continuing to Home
                      val hasCoarse = androidx.core.content.ContextCompat.checkSelfPermission(
                        context, android.Manifest.permission.ACCESS_COARSE_LOCATION
                      ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                      val hasFine = androidx.core.content.ContextCompat.checkSelfPermission(
                        context, android.Manifest.permission.ACCESS_FINE_LOCATION
                      ) == android.content.pm.PackageManager.PERMISSION_GRANTED

                      if (hasCoarse || hasFine) {
                        viewModel.detectLocation(context)
                      } else {
                        locationPermissionLauncher.launch(
                          arrayOf(
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                          )
                        )
                      }
                      viewModel.navigateTo(Screen.HOME)
                    }
                  )
                }

                Screen.LOCATION_SETUP -> {
                  // Direct bypass if ever invoked
                  BackHandler { viewModel.navigateBack() }
                  viewModel.navigateTo(Screen.HOME)
                }

                Screen.HOME -> {
                  // Root destination back handler shows Exit Confirmation Bottom Sheet
                  BackHandler {
                    showExitBottomSheet = true
                  }
                  HomeScreen(
                    schedule = schedule,
                    state = uiState,
                    onCityClick = { viewModel.navigateTo(Screen.CITY_SELECT) },
                    onCalendarClick = { viewModel.navigateTo(Screen.CALENDAR_VIEW) },
                    onPrayerSettingsClick = { viewModel.navigateTo(Screen.PRAYER_SETTINGS) },
                    onAppSettingsClick = { viewModel.navigateTo(Screen.APP_SETTINGS) },
                    onPremiumClick = { viewModel.navigateTo(Screen.PREMIUM) },
                    onPrayerClick = { viewModel.openPrayerDetail(it) },
                    onNotificationToggle = { viewModel.cyclePrayerNotification(it) },
                    onNotificationModeChange = { type, mode -> viewModel.setPrayerNotification(type, mode) },
                    onNavigateToAllahNames = { viewModel.navigateTo(Screen.ALLAH_NAMES) },
                    onNavigateToTasbeeh = { viewModel.navigateTo(Screen.TASBEEH) },
                    onNavigateToQibla = { viewModel.navigateTo(Screen.QIBLA) },
                    onNavigateToDuas = { viewModel.navigateTo(Screen.DUA_CATEGORIES) },
                    onNavigateToLiveMakkah = { viewModel.navigateTo(Screen.LIVE_MAKKAH) },
                    onNavigateToLiveMadinah = { viewModel.navigateTo(Screen.LIVE_MADINAH) }
                  )
                }

                Screen.CITY_SELECT -> {
                  BackHandler { viewModel.navigateBack() }
                  CitySelectScreen(
                    selectedCity = uiState.selectedCity,
                    isGpsSearching = uiState.isGpsSearching,
                    onSelectCity = { viewModel.selectCity(it) },
                    onRequestGps = { viewModel.detectLocation(context) },
                    onBack = { viewModel.navigateBack() }
                  )
                }

                Screen.PRAYER_SETTINGS -> {
                  BackHandler { viewModel.navigateBack() }
                  PrayerSettingsScreen(
                    schedule = schedule,
                    state = uiState,
                    onNotificationChange = { type, mode -> viewModel.setPrayerNotification(type, mode) },
                    onAdjustMinutes = { type, delta -> viewModel.adjustPrayerMinutes(type, delta) },
                    onOpenCalendar = { viewModel.navigateTo(Screen.CALENDAR_VIEW) },
                    onOpenFiqhSettings = { viewModel.navigateTo(Screen.FIQH_SETTINGS) },
                    onTogglePreReminder = { viewModel.togglePrePrayerReminder() },
                    onBack = { viewModel.navigateBack() }
                  )
                }

                Screen.CALENDAR_VIEW -> {
                  BackHandler { viewModel.navigateBack() }
                  CalendarScreen(
                    state = uiState,
                    onDateSelected = { year, month, day -> viewModel.setSelectedCalendarDate(year, month, day) },
                    onResetToday = { viewModel.resetCalendarToToday() },
                    onBack = { viewModel.navigateBack() }
                  )
                }

                Screen.ALLAH_NAMES -> {
                  BackHandler { viewModel.navigateBack() }
                  AllahNamesScreen(
                    playbackState = audioPlaybackState,
                    onStartFullRecitation = { viewModel.playAllahNamesFullRecitation(context) },
                    onToggleFullRecitation = { viewModel.toggleAudioPlayback(context) },
                    onStopFullRecitation = { viewModel.stopAudioPlayback(context) },
                    onSelectName = { viewModel.selectAllahName(it) },
                    onPlayName = { viewModel.playAllahNameAudio(context, it) },
                    onBack = { viewModel.navigateBack() }
                  )
                }

                Screen.ALLAH_NAME_DETAIL -> {
                  BackHandler { viewModel.navigateBack() }
                  // Handled via modal sheet below
                }

                Screen.TASBEEH -> {
                  BackHandler { viewModel.navigateBack() }
                  TasbeehScreen(
                    currentDhikr = uiState.currentDhikr,
                    count = uiState.tasbeehCount,
                    totalTarget = uiState.tasbeehTotalTarget,
                    laps = uiState.tasbeehLaps,
                    selectedStyle = uiState.selectedTasbeehStyle,
                    isHapticEnabled = uiState.isHapticEnabled,
                    isClickSoundEnabled = uiState.isClickSoundEnabled,
                    onIncrement = { ctx -> viewModel.incrementTasbeeh(ctx) },
                    onReset = { viewModel.resetTasbeeh() },
                    onSelectDhikr = { viewModel.selectDhikr(it) },
                    onSelectStyle = { viewModel.setTasbeehStyle(it) },
                    onSetTarget = { viewModel.setTasbeehTarget(it) },
                    onToggleHaptic = { viewModel.toggleHaptic() },
                    onToggleClickSound = { viewModel.toggleClickSound() },
                    onBack = { viewModel.navigateBack() }
                  )
                }

                Screen.QIBLA -> {
                  BackHandler { viewModel.navigateBack() }
                  QiblaScreen(
                    city = uiState.selectedCity,
                    qiblaBearingDegrees = uiState.qiblaBearingDegrees,
                    selectedStyle = uiState.selectedCompassStyle,
                    onSelectStyle = { viewModel.setCompassStyle(it) },
                    onBack = { viewModel.navigateBack() }
                  )
                }

                Screen.DUA_CATEGORIES -> {
                  BackHandler { viewModel.navigateBack() }
                  DuaCategoriesScreen(
                    onSelectCategory = {
                      viewModel.selectDuaCategory(it)
                      viewModel.navigateTo(Screen.DUA_LIST)
                    },
                    onBack = { viewModel.navigateBack() }
                  )
                }

                Screen.DUA_LIST -> {
                  BackHandler { viewModel.navigateBack() }
                  uiState.selectedDuaCategory?.let { category ->
                    DuaListScreen(
                      category = category,
                      onSelectDua = {
                        viewModel.selectDuaItem(it)
                        viewModel.navigateTo(Screen.DUA_DETAIL)
                      },
                      onBack = { viewModel.navigateBack() }
                    )
                  } ?: run {
                    viewModel.navigateBack()
                  }
                }

                Screen.DUA_DETAIL -> {
                  BackHandler { viewModel.navigateBack() }
                  uiState.selectedDuaItem?.let { dua ->
                    DuaDetailScreen(
                      dua = dua,
                      isPlaying = uiState.activeAudioTrack?.id == "dua_${dua.id}" && uiState.activeAudioTrack?.isPlaying == true,
                      onPlayAudio = { viewModel.playDuaAudio(dua) },
                      onBack = { viewModel.navigateBack() }
                    )
                  } ?: run {
                    viewModel.navigateBack()
                  }
                }

                Screen.LIVE_MAKKAH -> {
                  BackHandler { viewModel.navigateBack() }
                  LiveStreamScreen(
                    title = "Live Makkah",
                    arabicTitle = "بث مباشر من المسجد الحرام بمكة المكرمة",
                    locationName = "Masjid al-Haram, Makkah",
                    channelName = "Saudi Quran TV (القرآن الكريم)",
                    youtubeUrl = "https://www.youtube.com/watch?v=live_makkah",
                    streamDescription = "Continuous 24/7 live transmission of prayers, circumambulation (Tawaf), and recitations from the Holy Kaaba.",
                    isMakkah = true,
                    onBack = { viewModel.navigateBack() }
                  )
                }

                Screen.LIVE_MADINAH -> {
                  BackHandler { viewModel.navigateBack() }
                  LiveStreamScreen(
                    title = "Live Madinah",
                    arabicTitle = "بث مباشر من المسجد النبوي الشريف",
                    locationName = "Al-Masjid an-Nabawi, Madinah",
                    channelName = "Saudi Sunnah TV (السنة النبوية)",
                    youtubeUrl = "https://www.youtube.com/watch?v=live_madinah",
                    streamDescription = "Continuous 24/7 live transmission of prayers, Rawdah Sharif visits, and peaceful recitations from the Prophet's Mosque.",
                    isMakkah = false,
                    onBack = { viewModel.navigateBack() }
                  )
                }

                Screen.APP_SETTINGS -> {
                  BackHandler { viewModel.navigateBack() }
                  AppSettingsScreen(
                    state = uiState,
                    onNavigateToLanguage = { viewModel.navigateTo(Screen.LANGUAGE_SETTINGS) },
                    onNavigateToFiqh = { viewModel.navigateTo(Screen.FIQH_SETTINGS) },
                    onNavigateToCity = { viewModel.navigateTo(Screen.CITY_SELECT) },
                    onNavigateToPrayerSettings = { viewModel.navigateTo(Screen.PRAYER_SETTINGS) },
                    onNavigateToPremium = { viewModel.navigateTo(Screen.PREMIUM) },
                    onThemeModeChange = { viewModel.setThemeMode(it) },
                    onToggleHaptic = { viewModel.toggleHaptic() },
                    onBack = { viewModel.navigateBack() }
                  )
                }

                Screen.LANGUAGE_SETTINGS -> {
                  BackHandler { viewModel.navigateBack() }
                  LanguageSelectScreen(
                    selectedLanguage = uiState.selectedLanguage,
                    onLanguageSelected = { viewModel.selectLanguage(it) },
                    onContinue = { viewModel.navigateBack() },
                    isSettingsMode = true,
                    onBack = { viewModel.navigateBack() }
                  )
                }

                Screen.FIQH_SETTINGS -> {
                  BackHandler { viewModel.navigateBack() }
                  FiqhSelectScreen(
                    selectedMadhab = uiState.selectedMadhab,
                    selectedMethod = uiState.calculationMethod,
                    onMadhabSelected = { viewModel.selectMadhab(it) },
                    onMethodSelected = { viewModel.selectCalculationMethod(it) },
                    onContinue = { viewModel.navigateBack() },
                    isSettingsMode = true,
                    onBack = { viewModel.navigateBack() }
                  )
                }

                Screen.PREMIUM -> {
                  BackHandler { viewModel.navigateBack() }
                  PremiumScreen(
                    state = uiState,
                    onUnlock = { viewModel.unlockPremium() },
                    onBack = { viewModel.navigateBack() }
                  )
                }
              }

              // Persistent Audio Mini-Player floating over content
              AudioMiniPlayer(
                activeTrack = uiState.activeAudioTrack,
                onTogglePlayPause = { viewModel.toggleAudioPlayback(context) },
                onClose = { viewModel.stopAudioPlayback(context) },
                modifier = Modifier
                  .align(Alignment.BottomCenter)
                  .navigationBarsPadding()
                  .padding(bottom = 8.dp)
              )

              // Global Prayer Detail Bottom Sheet
              uiState.activeDetailPrayer?.let { item ->
                PrayerDetailBottomSheet(
                  prayerItem = item,
                  madhab = uiState.selectedMadhab,
                  calculationMethod = uiState.calculationMethod,
                  language = uiState.selectedLanguage,
                  onDismiss = { viewModel.closePrayerDetail() },
                  onNotificationModeChange = { mode ->
                    viewModel.setPrayerNotification(item.type, mode)
                    viewModel.closePrayerDetail()
                  }
                )
              }

              // Global Allah's Name Detail Bottom Sheet
              uiState.selectedAllahName?.let { name ->
                AllahNameDetailBottomSheet(
                  name = name,
                  isPlaying = uiState.activeAudioTrack?.isPlaying == true,
                  onPlayAudio = { viewModel.playAllahNameAudio(context, name) },
                  onDismiss = { viewModel.clearSelectedAllahName() }
                )
              }

              // Exit Confirmation Bottom Sheet on Root Home Back Press
              if (showExitBottomSheet) {
                ExitBottomSheet(
                  onDismiss = { showExitBottomSheet = false },
                  onConfirmExit = {
                    showExitBottomSheet = false
                    finish()
                  }
                )
              }
            }
          }
        }
      }
    }
  }
}
