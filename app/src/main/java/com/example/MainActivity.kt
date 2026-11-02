package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.databinding.ActivityMainBinding
import com.example.model.Screen
import com.example.model.ThemeMode
import com.example.ui.components.PrayerDetailBottomSheet
import com.example.ui.fragments.*
import com.example.ui.theme.SakinahTheme
import com.example.viewmodel.SakinahViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel: SakinahViewModel by viewModels()

  private var prayerDetailDialog: BottomSheetDialog? = null

  private val locationPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
    if (coarseGranted || fineGranted) {
      viewModel.detectLocation(this)
      com.example.service.PrayerNotificationScheduler.scheduleAll(this)
    }
  }

  private val notificationPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) { _ ->
    com.example.service.PrayerNotificationScheduler.scheduleAll(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupBackPressHandling()
    setupMiniPlayerListeners()
    observeViewModel()
    checkLocationPermission()
    checkNotificationPermission()

    com.example.service.PrayerNotificationManager.createNotificationChannels(this)
    com.example.service.PrayerNotificationScheduler.schedulePeriodicSync(this)
    com.example.service.PrayerNotificationScheduler.scheduleAll(this)
  }

  private fun checkNotificationPermission() {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
      val isGranted = ContextCompat.checkSelfPermission(
        this, Manifest.permission.POST_NOTIFICATIONS
      ) == PackageManager.PERMISSION_GRANTED
      if (!isGranted) {
        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
      }
    }
  }

  private fun setupBackPressHandling() {
    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        val navHostFragment = supportFragmentManager
          .findFragmentById(R.id.main_fragment_container) as? androidx.navigation.fragment.NavHostFragment
        val navController = navHostFragment?.navController
        val currentDest = navController?.currentDestination?.id

        if (currentDest == R.id.homeFragment || currentDest == null) {
          showExitBottomSheet()
        } else {
          if (!navController.navigateUp()) {
            showExitBottomSheet()
          }
        }
      }
    })
  }

  private fun showExitBottomSheet() {
    val existing = supportFragmentManager.findFragmentByTag("exit_sheet")
    if (existing == null) {
      ExitBottomSheetFragment {
        finish()
      }.show(supportFragmentManager, "exit_sheet")
    }
  }

  private fun checkLocationPermission() {
    val hasCoarse = ContextCompat.checkSelfPermission(
      this, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val hasFine = ContextCompat.checkSelfPermission(
      this, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (hasCoarse || hasFine) {
      viewModel.detectLocation(this)
    }
  }

  private fun setupMiniPlayerListeners() {
    binding.includeAudioMiniPlayer.btnAudioMiniPlayPause.setOnClickListener {
      viewModel.toggleAudioPlayback(this)
    }

    binding.includeAudioMiniPlayer.btnAudioMiniClose.setOnClickListener {
      viewModel.stopAudioPlayback(this)
    }
  }

  private fun observeViewModel() {
    lifecycleScope.launch {
      viewModel.uiState.collectLatest { state ->
        applyThemeMode(state.themeMode)
        updateMiniPlayer(state.activeAudioTrack)
        updatePrayerDetailSheet(state)
      }
    }
  }

  private fun applyThemeMode(mode: ThemeMode) {
    val nightMode = when (mode) {
      ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
      ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
      ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
    }
    val current = AppCompatDelegate.getDefaultNightMode().let {
      if (it == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM else it
    }
    if (current != nightMode) {
      AppCompatDelegate.setDefaultNightMode(nightMode)
    }
  }

  private fun updateMiniPlayer(track: com.example.model.AudioTrack?) {
    if (track != null) {
      binding.flMiniPlayerHost.visibility = View.VISIBLE
      binding.includeAudioMiniPlayer.tvAudioMiniTitle.text = track.title
      binding.includeAudioMiniPlayer.tvAudioMiniSubtitle.text = track.arabicTitle

      val progress = (track.progressFraction * 100).toInt()
      binding.includeAudioMiniPlayer.pbAudioMiniProgress.progress = progress

      if (track.isPlaying) {
        binding.includeAudioMiniPlayer.btnAudioMiniPlayPause.setImageResource(android.R.drawable.ic_media_pause)
      } else {
        binding.includeAudioMiniPlayer.btnAudioMiniPlayPause.setImageResource(android.R.drawable.ic_media_play)
      }
    } else {
      binding.flMiniPlayerHost.visibility = View.GONE
    }
  }

  private fun updatePrayerDetailSheet(state: com.example.model.SakinahUiState) {
    val activeItem = state.activeDetailPrayer
    if (activeItem != null) {
      if (prayerDetailDialog == null) {
        prayerDetailDialog = BottomSheetDialog(this).apply {
          val composeView = ComposeView(this@MainActivity).apply {
            setContent {
              SakinahTheme {
                PrayerDetailBottomSheet(
                  prayerItem = activeItem,
                  madhab = state.selectedMadhab,
                  calculationMethod = state.calculationMethod,
                  language = state.selectedLanguage,
                  onDismiss = {
                    viewModel.closePrayerDetail()
                    dismiss()
                  },
                  onNotificationModeChange = { mode ->
                    viewModel.setPrayerNotification(activeItem.type, mode)
                    viewModel.closePrayerDetail()
                    dismiss()
                  }
                )
              }
            }
          }
          setContentView(composeView)
          setOnDismissListener {
            viewModel.closePrayerDetail()
            prayerDetailDialog = null
          }
          show()
        }
      }
    } else {
      prayerDetailDialog?.dismiss()
      prayerDetailDialog = null
    }
  }
}
