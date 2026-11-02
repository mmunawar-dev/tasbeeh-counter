package com.example.ui.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.calculation.HijriCalendarHelper
import com.example.calculation.PrayerCalculator
import com.example.databinding.FragmentCalendarBinding
import com.example.model.PrayerType
import com.example.viewmodel.SakinahViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarFragment : BaseFragment<FragmentCalendarBinding>(FragmentCalendarBinding::inflate) {

  private val viewModel: SakinahViewModel by activityViewModels()

  private var viewingYear: Int = 2026
  private var viewingMonth: Int = 10

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.btnCalendarBack.setOnClickListener {
      findNavController().navigateUp()
    }

    val state = viewModel.uiState.value
    viewingYear = state.selectedCalendarDateYear
    viewingMonth = state.selectedCalendarDateMonth

    binding.btnPrevMonth.setOnClickListener {
      if (viewingMonth == 1) {
        viewingMonth = 12
        viewingYear--
      } else {
        viewingMonth--
      }
      updateCalendarUI()
    }

    binding.btnNextMonth.setOnClickListener {
      if (viewingMonth == 12) {
        viewingMonth = 1
        viewingYear++
      } else {
        viewingMonth++
      }
      updateCalendarUI()
    }

    binding.btnCalendarToday.setOnClickListener {
      viewModel.resetCalendarToToday()
      val today = Calendar.getInstance()
      viewingYear = today.get(Calendar.YEAR)
      viewingMonth = today.get(Calendar.MONTH) + 1
      updateCalendarUI()
    }

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.uiState.collectLatest {
        updateCalendarUI()
      }
    }
  }

  private fun updateCalendarUI() {
    val state = viewModel.uiState.value
    val cal = Calendar.getInstance().apply {
      set(Calendar.YEAR, viewingYear)
      set(Calendar.MONTH, viewingMonth - 1)
      set(Calendar.DAY_OF_MONTH, 1)
    }

    val monthName = SimpleDateFormat("MMMM yyyy", Locale.US).format(cal.time)
    binding.tvCalendarMonthName.text = monthName

    val maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1 // 0=Sunday

    binding.llCalendarWeeksContainer.removeAllViews()
    val density = resources.displayMetrics.density

    var currentDay = 1
    var weekRow = LinearLayout(requireContext()).apply {
      orientation = LinearLayout.HORIZONTAL
      layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      ).apply {
        setMargins(0, (4 * density).toInt(), 0, (4 * density).toInt())
      }
    }

    // Leading empty slots
    for (i in 0 until firstDayOfWeek) {
      val empty = View(requireContext()).apply {
        layoutParams = LinearLayout.LayoutParams(0, (36 * density).toInt(), 1f)
      }
      weekRow.addView(empty)
    }

    var dayOfWeekCounter = firstDayOfWeek
    val todayCal = Calendar.getInstance()

    while (currentDay <= maxDays) {
      val dayNum = currentDay
      val isSelected = viewingYear == state.selectedCalendarDateYear &&
        viewingMonth == state.selectedCalendarDateMonth &&
        dayNum == state.selectedCalendarDateDay

      val isToday = todayCal.get(Calendar.YEAR) == viewingYear &&
        todayCal.get(Calendar.MONTH) + 1 == viewingMonth &&
        todayCal.get(Calendar.DAY_OF_MONTH) == dayNum

      val dayView = TextView(requireContext()).apply {
        layoutParams = LinearLayout.LayoutParams(0, (36 * density).toInt(), 1f)
        gravity = Gravity.CENTER
        text = dayNum.toString()
        textSize = 13f

        if (isSelected) {
          setBackgroundResource(R.drawable.bg_circle_emerald_container)
          setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
          setTypeface(typeface, Typeface.BOLD)
        } else if (isToday) {
          setBackgroundResource(R.drawable.bg_dot_active)
          setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
          setTypeface(typeface, Typeface.BOLD)
        } else {
          setBackgroundColor(0)
          setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
        }

        setOnClickListener {
          viewModel.setSelectedCalendarDate(viewingYear, viewingMonth, dayNum)
        }
      }

      weekRow.addView(dayView)
      dayOfWeekCounter++

      if (dayOfWeekCounter == 7) {
        binding.llCalendarWeeksContainer.addView(weekRow)
        weekRow = LinearLayout(requireContext()).apply {
          orientation = LinearLayout.HORIZONTAL
          layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
          ).apply {
            setMargins(0, (4 * density).toInt(), 0, (4 * density).toInt())
          }
        }
        dayOfWeekCounter = 0
      }

      currentDay++
    }

    // Trailing empty slots
    if (dayOfWeekCounter > 0) {
      for (i in dayOfWeekCounter until 7) {
        val empty = View(requireContext()).apply {
          layoutParams = LinearLayout.LayoutParams(0, (36 * density).toInt(), 1f)
        }
        weekRow.addView(empty)
      }
      binding.llCalendarWeeksContainer.addView(weekRow)
    }

    renderPreviewSchedule()
  }

  private fun renderPreviewSchedule() {
    val state = viewModel.uiState.value
    val selectedCal = Calendar.getInstance().apply {
      set(Calendar.YEAR, state.selectedCalendarDateYear)
      set(Calendar.MONTH, state.selectedCalendarDateMonth - 1)
      set(Calendar.DAY_OF_MONTH, state.selectedCalendarDateDay)
    }
    val dateStr = HijriCalendarHelper.formatGregorianDate(selectedCal, state.selectedLanguage)
    val hijriStr = HijriCalendarHelper.getHijriDate(selectedCal).formatted(state.selectedLanguage)
    binding.tvPreviewDateLabel.text = "$dateStr • $hijriStr"

    val schedule = PrayerCalculator.calculate(
      year = state.selectedCalendarDateYear,
      month = state.selectedCalendarDateMonth,
      day = state.selectedCalendarDateDay,
      city = state.selectedCity,
      madhab = state.selectedMadhab,
      method = state.calculationMethod
    )

    binding.llPreviewPrayersContainer.removeAllViews()
    val density = resources.displayMetrics.density

    schedule.obligatoryPrayers.forEachIndexed { index, item ->
      val row = LinearLayout(requireContext()).apply {
        orientation = LinearLayout.HORIZONTAL
        layoutParams = LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT,
          LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
          setMargins(0, (6 * density).toInt(), 0, (6 * density).toInt())
        }
        gravity = Gravity.CENTER_VERTICAL
      }

      val nameLayout = LinearLayout(requireContext()).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
      }

      val tvName = TextView(requireContext()).apply {
        text = item.type.englishName
        textSize = 15f
        setTypeface(typeface, Typeface.BOLD)
        setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
      }

//      val tvArabic = TextView(requireContext()).apply {
//        text = item.type.arabicName
//        textSize = 11f
//        setTextColor(ContextCompat.getColor(requireContext(), R.color.text_secondary))
//      }

      nameLayout.addView(tvName)
//      nameLayout.addView(tvArabic)

      val tvTime = TextView(requireContext()).apply {
        text = item.timeFormatted
        textSize = 15f
        setTypeface(typeface, Typeface.BOLD)
        setTextColor(ContextCompat.getColor(requireContext(), R.color.emerald_primary))
      }

      val ivIcon = android.widget.ImageView(requireContext()).apply {
        layoutParams = LinearLayout.LayoutParams((24 * density).toInt(), (24 * density).toInt()).apply {
          marginEnd = (12 * density).toInt()
        }
        setImageResource(item.type.iconRes)
      }

      row.addView(ivIcon)
      row.addView(nameLayout)
      row.addView(tvTime)

      binding.llPreviewPrayersContainer.addView(row)

      if (index < schedule.obligatoryPrayers.lastIndex) {
        val divider = View(requireContext()).apply {
          layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            (1 * density).toInt()
          ).apply {
            setMargins(0, (6 * density).toInt(), 0, (6 * density).toInt())
          }
          setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.outline_border))
        }
        binding.llPreviewPrayersContainer.addView(divider)
      }
    }
  }
}
