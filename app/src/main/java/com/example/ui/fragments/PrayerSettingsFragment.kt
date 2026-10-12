package com.example.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.calculation.HijriCalendarHelper
import com.example.calculation.PrayerCalculator
import com.example.databinding.FragmentPrayerSettingsBinding
import com.example.databinding.ItemPrayerSettingRowBinding
import com.example.model.PrayerNotificationMode
import com.example.model.PrayerType
import com.example.ui.navigation.navigateWithSlide
import com.example.viewmodel.SakinahViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

class PrayerSettingsFragment : BaseFragment<FragmentPrayerSettingsBinding>(FragmentPrayerSettingsBinding::inflate) {

  private val viewModel: SakinahViewModel by activityViewModels()
  private var currentSchedule: PrayerCalculator.DailyPrayerSchedule? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.btnPrayerSettingsBack.setOnClickListener {
      findNavController().navigateUp()
    }

    binding.btnOpenCalendar.setOnClickListener {
      findNavController().navigateWithSlide(R.id.calendarFragment)
    }

    binding.cardSelectedDate.setOnClickListener {
      findNavController().navigateWithSlide(R.id.calendarFragment)
    }

    binding.cardFiqhInfo.setOnClickListener {
      findNavController().navigateWithSlide(R.id.fiqhSelectFragment)
    }

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.uiState.collectLatest { state ->
        val cal = Calendar.getInstance().apply {
          set(Calendar.YEAR, state.selectedCalendarDateYear)
          set(Calendar.MONTH, state.selectedCalendarDateMonth - 1)
          set(Calendar.DAY_OF_MONTH, state.selectedCalendarDateDay)
        }
        binding.tvDateFormatted.text = HijriCalendarHelper.formatGregorianDate(cal, state.selectedLanguage)

        binding.tvFooterMadhab.text = "Madhab: ${state.selectedMadhab.displayName}"
        binding.tvFooterMethod.text = "Method: ${state.calculationMethod.shortName}"

        currentSchedule?.let { schedule ->
          renderPrayerItems(schedule)
        }
      }
    }

    viewModel.scheduleLiveData.observe(viewLifecycleOwner) { schedule ->
      currentSchedule = schedule
      renderPrayerItems(schedule)
    }
  }

  private fun renderPrayerItems(schedule: PrayerCalculator.DailyPrayerSchedule) {
    binding.llPrayersContainer.removeAllViews()
    val inflater = LayoutInflater.from(requireContext())
    val state = viewModel.uiState.value

    val items = listOf(
      schedule.fajr,
      schedule.sunrise,
      schedule.dhuhr,
      schedule.asr,
      schedule.maghrib,
      schedule.isha
    )

    items.forEach { prayer ->
      val itemBinding = ItemPrayerSettingRowBinding.inflate(inflater, binding.llPrayersContainer, false)
      val currentMode = state.notificationModes[prayer.type] ?: PrayerNotificationMode.SOUND

      itemBinding.tvPrayerSettingName.text = prayer.type.englishName
      itemBinding.tvPrayerSettingArabic.text = prayer.type.arabicName
      itemBinding.tvPrayerSettingTime.text = prayer.timeFormatted

      itemBinding.ivPrayerSettingIcon.setImageResource(prayer.type.iconRes)
      itemBinding.ivPrayerSettingIcon.imageTintList = null

      updateNotificationModeIcon(itemBinding.btnModeSilent, currentMode)

      itemBinding.btnModeSilent.setOnClickListener {
        val activeMode = viewModel.uiState.value.notificationModes[prayer.type] ?: PrayerNotificationMode.SOUND
        val nextMode = when (activeMode) {
          PrayerNotificationMode.SOUND -> PrayerNotificationMode.VIBRATE
          PrayerNotificationMode.VIBRATE -> PrayerNotificationMode.OFF
          PrayerNotificationMode.SILENT, PrayerNotificationMode.OFF -> PrayerNotificationMode.SOUND
        }
        viewModel.setPrayerNotification(prayer.type, nextMode)
        updateNotificationModeIcon(itemBinding.btnModeSilent, nextMode)
      }

      itemBinding.root.setOnClickListener {
        viewModel.openPrayerDetail(prayer)
      }

      binding.llPrayersContainer.addView(itemBinding.root)
    }
  }

  private fun updateNotificationModeIcon(button: ImageButton, mode: PrayerNotificationMode) {
    val context = button.context
    when (mode) {
      PrayerNotificationMode.SOUND -> {
        button.setImageResource(android.R.drawable.ic_lock_silent_mode_off)
        button.contentDescription = "Ring on"
        button.setColorFilter(ContextCompat.getColor(context, R.color.emerald_primary))
      }
      PrayerNotificationMode.VIBRATE -> {
        button.setImageResource(R.drawable.ic_sk_vibration)
        button.contentDescription = "Vibrate only"
        button.setColorFilter(ContextCompat.getColor(context, R.color.emerald_primary))
      }
      PrayerNotificationMode.SILENT, PrayerNotificationMode.OFF -> {
        button.setImageResource(android.R.drawable.ic_lock_silent_mode)
        button.contentDescription = "Ring off"
        button.setColorFilter(ContextCompat.getColor(context, R.color.text_secondary))
      }
    }
  }
}
