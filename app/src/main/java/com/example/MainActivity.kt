package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.Screen
import com.example.model.ThemeMode
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
      val context = LocalContext.current

      val isDark = when (uiState.themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
      }

      SakinahTheme(darkTheme = isDark) {
        // Enforce genuine RTL when Arabic or Urdu is selected
        CompositionLocalProvider(LocalLayoutDirection provides uiState.selectedLanguage.layoutDirection) {
          Surface(modifier = Modifier.fillMaxSize()) {
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
                  onContinue = { viewModel.navigateTo(Screen.LOCATION_SETUP) }
                )
              }

              Screen.LOCATION_SETUP -> {
                BackHandler { viewModel.navigateBack() }
                LocationSetupScreen(
                  currentCity = uiState.selectedCity,
                  isSearching = uiState.isGpsSearching,
                  onRequestGps = { viewModel.detectLocation(context) },
                  onManualCity = { viewModel.navigateTo(Screen.CITY_SELECT) },
                  onProceedHome = { viewModel.navigateTo(Screen.HOME) }
                )
              }

              Screen.HOME -> {
                HomeScreen(
                  schedule = schedule,
                  state = uiState,
                  onCityClick = { viewModel.navigateTo(Screen.CITY_SELECT) },
                  onCalendarClick = { viewModel.navigateTo(Screen.CALENDAR_VIEW) },
                  onTasbeehClick = { viewModel.navigateTo(Screen.TASBEEH) },
                  onPrayerSettingsClick = { viewModel.navigateTo(Screen.PRAYER_SETTINGS) },
                  onAppSettingsClick = { viewModel.navigateTo(Screen.APP_SETTINGS) },
                  onPrayerClick = { viewModel.openPrayerDetail(it) },
                  onNotificationToggle = { viewModel.cyclePrayerNotification(it) }
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

              Screen.TASBEEH -> {
                BackHandler { viewModel.navigateBack() }
                TasbeehScreen(
                  state = uiState,
                  onIncrement = { ctx -> viewModel.incrementTasbeeh(ctx) },
                  onReset = { viewModel.resetTasbeeh() },
                  onSelectDhikr = { viewModel.selectDhikr(it) },
                  onSetTarget = { viewModel.setTasbeehTarget(it) },
                  onToggleHaptic = { viewModel.toggleHaptic() },
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
          }
        }
      }
    }
  }
}
