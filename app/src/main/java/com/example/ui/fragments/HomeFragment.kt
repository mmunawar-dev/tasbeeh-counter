package com.example.ui.fragments

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.calculation.HijriCalendarHelper
import com.example.calculation.PrayerCalculator
import com.example.databinding.FragmentHomeBinding
import com.example.databinding.ItemPrayerRowBinding
import com.example.model.PrayerNotificationMode
import com.example.model.PrayerTimeItem
import com.example.model.PrayerType
import com.example.ui.activities.PremiumActivity
import com.example.ui.customviews.ShimmerDrawable
import com.example.ui.navigation.navigateWithSlide
import com.example.viewmodel.SakinahViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

  private val viewModel: SakinahViewModel by activityViewModels()
  private var currentActivePrayerType: PrayerType? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    applyShimmerEffect(binding.btnOpenPremium)
    setupClickListeners()
    observeViewModel()
  }

  private fun applyShimmerEffect(view: View) {

    val shimmerDrawable = ShimmerDrawable()

    view.doOnLayout {

      // IMPORTANT:
      // Give the drawable the actual size of the view.
      shimmerDrawable.setBounds(
        0,
        0,
        view.width,
        view.height
      )

      view.overlay.add(shimmerDrawable)

      val animator = ValueAnimator.ofFloat(0f, 1f).apply {

        duration = 1600L

        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.RESTART

        interpolator = LinearInterpolator()

        addUpdateListener { animation ->

          val progress = animation.animatedValue as Float

          shimmerDrawable.setProgress(progress)
        }
      }

      animator.start()

      view.addOnAttachStateChangeListener(
        object : View.OnAttachStateChangeListener {

          override fun onViewAttachedToWindow(v: View) {
            if (!animator.isRunning) {
              animator.start()
            }
          }

          override fun onViewDetachedFromWindow(v: View) {
            animator.cancel()
            view.overlay.remove(shimmerDrawable)
          }
        }
      )
    }
  }
  private fun setupClickListeners() {
    binding.layoutLocationSelector.setOnClickListener {
      findNavController().navigateWithSlide(R.id.citySelectFragment)
    }

    parentFragmentManager.setFragmentResultListener(
      CitySelectFragment.REQUEST_KEY_CITY_SELECTION,
      viewLifecycleOwner
    ) { _, _ ->
      // Location update is already published to viewModel.uiState
    }





    binding.btnOpenPremium.setOnClickListener {
      startActivity(Intent(requireContext(), PremiumActivity::class.java))
    }

    binding.btnOpenAppSettings.setOnClickListener {
      findNavController().navigateWithSlide(R.id.appSettingsFragment)
    }

    binding.btnHeroPrayerTimes.setOnClickListener {
      findNavController().navigateWithSlide(R.id.prayerSettingsFragment)
    }


    binding.cardUtilAllahNames.setOnClickListener {
      findNavController().navigateWithSlide(R.id.allahNamesFragment)
    }

    binding.cardUtilTasbeeh.setOnClickListener {
      findNavController().navigateWithSlide(R.id.tasbeehFragment)
    }

    binding.cardUtilQibla.setOnClickListener {
      findNavController().navigateWithSlide(R.id.qiblaFragment)
    }

    binding.cardUtilDuas.setOnClickListener {
      findNavController().navigateWithSlide(R.id.duaCategoriesFragment)
    }

    binding.cardUtilLiveMakkah.setOnClickListener {
      findNavController().navigateWithSlide(R.id.liveMakkahFragment)
    }

    binding.cardUtilLiveMadinah.setOnClickListener {
      findNavController().navigateWithSlide(R.id.liveMadinahFragment)
    }

  }

  private fun observeViewModel() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.uiState.collectLatest { state ->
        binding.tvLocationCity.text = "${state.selectedCity.name}, ${state.selectedCity.country}"
        binding.tvLocationCountry.text = "${state.selectedCity.country} • Aladhan API"

        val cal = Calendar.getInstance().apply {
          set(Calendar.YEAR, state.selectedCalendarDateYear)
          set(Calendar.MONTH, state.selectedCalendarDateMonth - 1)
          set(Calendar.DAY_OF_MONTH, state.selectedCalendarDateDay)
        }
        val hijri = HijriCalendarHelper.getHijriDate(cal)
        binding.tvHijriDate.text = hijri.formatted(state.selectedLanguage)
        binding.tvGregorianDate.text = HijriCalendarHelper.formatGregorianDate(cal, state.selectedLanguage)

        binding.tvCardTasbeehSubtitle.text = "Dhikr & Counter"
        binding.tvCardQiblaSubtitle.text = "Compass to Kaaba"

      }
    }



    viewModel.isApiLoadingLiveData.observe(viewLifecycleOwner) { loading ->
      binding.pbApiLoading.visibility = if (loading) View.VISIBLE else View.GONE
    }

    viewModel.scheduleLiveData.observe(viewLifecycleOwner) { schedule ->
      renderSchedule(schedule)
    }
  }

  private fun renderSchedule(schedule: PrayerCalculator.DailyPrayerSchedule) {
    val current = schedule.currentPrayer
    val next = schedule.nextPrayer

    val activePrayer = current ?: next
    val isCurrent = current != null
    currentActivePrayerType = if (isCurrent) activePrayer.type else null

    binding.tvHeroLabel.text = if (isCurrent) {
      "CURRENT PRAYER"
    } else {
      "UPCOMING PRAYER"
    }
    binding.dotHeroStatus.setBackgroundResource(
      if (isCurrent) R.drawable.bg_dot_active else R.drawable.bg_dot_inactive
    )

//    binding.tvHeroArabic.text = activePrayer.type.arabicName
    binding.tvHeroTime.text = activePrayer.type.name.lowercase().replaceFirstChar { it.uppercase() }
    binding.tvHeroInterval.text = if (isCurrent) {
      "Started ${activePrayer.timeFormatted}"
    } else {
      "Starts ${activePrayer.timeFormatted}"
    }
    binding.tvHeroIntervalEnd.text = "Ends ${activePrayer.endFormatted}"

    val rawRemaining = activePrayer.remainingTimeFormatted
    val cleanRemaining = rawRemaining
      .replace("Ends in ", "")
      .replace("In ", "")
      .trim()
    binding.tvHeroCountdown.text = if (cleanRemaining.isNotEmpty()) cleanRemaining else rawRemaining
    binding.tvHeroCountdownLabel.text = "Remaining"


    binding.tvSunriseTime.text = schedule.sunrise.timeFormatted


    updateHeroPrayerItems(schedule, current?.type)
  }

  private fun updateHeroPrayerItems(
    schedule: PrayerCalculator.DailyPrayerSchedule,
    currentPrayerType: PrayerType?
  ) {
    updatePrayerItem(
      binding.layoutPrayerFajr,
      binding.tvPrayerNameFajr,
      binding.tvPrayerTimeFajr,binding.img1,
      schedule.fajr,
      currentPrayerType == PrayerType.FAJR
    )
    updatePrayerItem(
      binding.layoutPrayerDhuhr,
      binding.tvPrayerNameDhuhr,
      binding.tvPrayerTimeDhuhr,binding.img2,
      schedule.dhuhr,
      currentPrayerType == PrayerType.DHUHR
    )
    updatePrayerItem(
      binding.layoutPrayerAsr,
      binding.tvPrayerNameAsr,
      binding.tvPrayerTimeAsr,binding.img3,
      schedule.asr,
      currentPrayerType == PrayerType.ASR
    )
    updatePrayerItem(
      binding.layoutPrayerMaghrib,
      binding.tvPrayerNameMaghrib,
      binding.tvPrayerTimeMaghrib,binding.img4,
      schedule.maghrib,
      currentPrayerType == PrayerType.MAGHRIB
    )
    updatePrayerItem(
      binding.layoutPrayerIsha,
      binding.tvPrayerNameIsha,
      binding.tvPrayerTimeIsha,binding.img5,
      schedule.isha,
      currentPrayerType == PrayerType.ISHA
    )
  }

  private fun updatePrayerItem(
    container: View,
    tvName: TextView,
    tvTime: TextView,
    dot: ImageView,
    item: PrayerTimeItem,
    isCurrent: Boolean
  ) {
    val context = container.context
    tvTime.text = item.timeFormatted
    if (isCurrent) {
      container.setBackgroundResource(R.drawable.bg_hero_prayer_item_active)
      tvName.setTextColor(ContextCompat.getColor(context, R.color.hero_pill_text))
      tvTime.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
      dot.setImageResource(R.drawable.bg_prayer_line_active)
    } else {
      container.background = null
      tvName.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
      tvTime.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
      dot.setImageResource(R.drawable.bg_prayer_line_inactive)
    }

  }
}
