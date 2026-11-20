package com.example.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.calculation.QiblaCalculator
import com.example.databinding.FragmentQiblaBinding
import com.example.databinding.ItemCompassStyleBinding
import com.example.model.CompassStyle
import com.example.model.ThemeMode
import com.example.ui.navigation.navigateWithSlide
import com.example.ui.screens.QiblaDial
import com.example.ui.theme.SakinahTheme
import com.example.viewmodel.SakinahViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class QiblaFragment : BaseFragment<FragmentQiblaBinding>(FragmentQiblaBinding::inflate) {

  private val viewModel: SakinahViewModel by activityViewModels()

  private val styleBindings = mutableMapOf<CompassStyle, ItemCompassStyleBinding>()
  private var renderedStyle: CompassStyle? = null
  private var lastAligned: Boolean? = null
  private var lastRotateText: String? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Equivalent of Compose statusBarsPadding()
    ViewCompat.setOnApplyWindowInsetsListener(binding.qiblaRoot) { v, insets ->
      val bars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
      v.setPadding(v.paddingLeft, bars.top, v.paddingRight, v.paddingBottom)
      insets
    }
    ViewCompat.requestApplyInsets(binding.qiblaRoot)

    binding.backBtn.setOnClickListener { findNavController().navigateUp() }

    val openCitySelect = View.OnClickListener {
      findNavController().navigateWithSlide(R.id.citySelectFragment)
    }
    binding.layoutLocation.setOnClickListener(openCitySelect)
    binding.btnQiblaChangeLocation.setOnClickListener(openCitySelect)

    parentFragmentManager.setFragmentResultListener(
      CitySelectFragment.REQUEST_KEY_CITY_SELECTION,
      viewLifecycleOwner
    ) { _, _ ->
      // Location update is already published to viewModel.uiState
    }

    buildStyleChips()
    setupDial()
    showRotateHint(0f, false, force = true)

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.uiState.collectLatest { state ->
        val city = state.selectedCity
        binding.tvQiblaCity.text = city.displayName
        binding.tvQiblaBearing.text = "Bearing: ${state.qiblaBearingDegrees.toInt()}° from North"
        binding.tvQiblaDistance.text = "${QiblaCalculator.calculateDistanceKm(city.latitude, city.longitude)} km"
        renderStyleSelection(state.selectedCompassStyle)
      }
    }
  }

  private fun previewDrawableFor(style: CompassStyle): Int = when (style) {
    CompassStyle.CLASSIC_GOLD -> R.drawable.ic_compass_preview_classic_gold
    CompassStyle.EMERALD_MINIMAL -> R.drawable.ic_compass_preview_emerald_minimal
    CompassStyle.ASTROLABE -> R.drawable.ic_compass_preview_astrolabe
    CompassStyle.NIGHT_CELESTIAL -> R.drawable.ic_compass_preview_night_celestial
    CompassStyle.KAABA_ARROW -> R.drawable.ic_compass_preview_kaaba_arrow
  }

  private fun buildStyleChips() {
    val inflater = LayoutInflater.from(requireContext())
    binding.llCompassStyles.removeAllViews()
    styleBindings.clear()
    CompassStyle.values().forEach { style ->
      val item = ItemCompassStyleBinding.inflate(inflater, binding.llCompassStyles, false)
      item.ivCompassPreview.setImageResource(previewDrawableFor(style))
      item.tvStyleTitle.text = style.title
      item.root.setOnClickListener { viewModel.setCompassStyle(style) }
      styleBindings[style] = item
      binding.llCompassStyles.addView(item.root)
    }
  }

  private fun renderStyleSelection(selected: CompassStyle) {
    if (renderedStyle == selected) return
    renderedStyle = selected
//    binding.tvSelectedStyleName.text = selected.title
//    binding.tvStyleActiveBadge.visibility = View.VISIBLE
    styleBindings.forEach { (style, item) ->
      val isSelected = style == selected
      item.viewSelectionRing.setBackgroundResource(
        if (isSelected) R.drawable.bg_compass_preview_selected else R.drawable.bg_compass_preview_unselected
      )
    }
  }

  private fun setupDial() {
    binding.composeQiblaDial.apply {
      setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
      setContent {
        val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
        val isDark = when (uiState.themeMode) {
          ThemeMode.SYSTEM -> isSystemInDarkTheme()
          ThemeMode.LIGHT -> false
          ThemeMode.DARK -> true
        }
        SakinahTheme(darkTheme = isDark) {
          QiblaDial(
            qiblaBearingDegrees = uiState.qiblaBearingDegrees,
            selectedStyle = uiState.selectedCompassStyle,
            onHeadingChanged = { _, relativeQiblaAngle, isAligned ->
              showRotateHint(relativeQiblaAngle, isAligned, force = false)
            }
          )
        }
      }
    }
  }

  private fun showRotateHint(relativeQiblaAngle: Float, isAligned: Boolean, force: Boolean) {
    if (!isBindingAvailable) return
    val b = binding
    if (isAligned) {
      if (force || lastAligned != true) {
        b.llQiblaAligned.visibility = View.VISIBLE
        b.llQiblaRotate.visibility = View.GONE
        lastAligned = true
      }
      return
    }
    val turnDifference = (relativeQiblaAngle + 360f) % 360f
    val turnDirection = if (turnDifference in 1f..180f) "right \u2794" else "left \u2B05"
    val degreesRemaining = if (turnDifference <= 180f) turnDifference.toInt() else (360 - turnDifference).toInt()
    val text = "Rotate phone $turnDirection ($degreesRemaining° to align)"
    if (force || lastAligned != false) {
      b.llQiblaAligned.visibility = View.GONE
      b.llQiblaRotate.visibility = View.VISIBLE
      lastAligned = false
    }
    if (text != lastRotateText) {
      b.tvQiblaRotate.text = text
      lastRotateText = text
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    styleBindings.clear()
    renderedStyle = null
    lastAligned = null
    lastRotateText = null
  }
}