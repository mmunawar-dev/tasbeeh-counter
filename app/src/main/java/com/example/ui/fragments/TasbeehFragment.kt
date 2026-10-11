package com.example.ui.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
import com.example.databinding.FragmentTasbeehBinding
import com.example.databinding.ItemDhikrPresetBinding
import com.example.databinding.ItemTasbeehStyleBinding
import com.example.databinding.ItemTasbeehTargetBinding
import com.example.databinding.SheetTasbeehPickerBinding
import com.example.model.DhikrPreset
import com.example.model.SakinahUiState
import com.example.model.TasbeehStyle
import com.example.model.ThemeMode
import com.example.ui.screens.TasbeehInteractiveVisual
import com.example.ui.theme.SakinahTheme
import com.example.viewmodel.SakinahViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TasbeehFragment : BaseFragment<FragmentTasbeehBinding>(FragmentTasbeehBinding::inflate) {

  private val viewModel: SakinahViewModel by activityViewModels()

  private val styleBindings = mutableMapOf<TasbeehStyle, ItemTasbeehStyleBinding>()
  private var renderedStyle: TasbeehStyle? = null
  private var celebrationJob: Job? = null
  private var lastCountLaps: Pair<Int, Int>? = null
  private var activeSheet: BottomSheetDialog? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Equivalent of the status-bar inset handling of the Compose TopAppBar
    ViewCompat.setOnApplyWindowInsetsListener(binding.tasbeehTopBar) { v, insets ->
      val bars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
      v.setPadding(v.paddingLeft, bars.top, v.paddingRight, v.paddingBottom)
      insets
    }
    ViewCompat.requestApplyInsets(binding.tasbeehTopBar)

    binding.btnTasbeehBack.setOnClickListener { findNavController().navigateUp() }
    binding.btnTasbeehSound.setOnClickListener { viewModel.cycleTasbeehClickMode(requireContext()) }
    binding.btnTasbeehHaptic.visibility = View.GONE
    binding.btnTasbeehDhikrList.setOnClickListener { showDhikrPicker() }
    binding.btnTasbeehReset.setOnClickListener { viewModel.resetTasbeeh() }
    binding.btnDhikrChange.setOnClickListener { showDhikrPicker() }
    binding.btnDhikrTarget.setOnClickListener { showTargetPicker() }

    buildStyleChips()
    setupVisual()

    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.uiState.collectLatest { state -> render(state) }
    }
  }

  private fun render(state: SakinahUiState) {
    val ctx = requireContext()
    val dhikr = state.currentDhikr
    val count = state.tasbeehCount
    val target = state.tasbeehTotalTarget
    val laps = state.tasbeehLaps

    binding.tvTasbeehSubtitle.text = "Lap $laps • Target $target"

    // Toolbar toggle for Ring / Vibrate / Silent
    val emerald = Color.parseColor("#10B981")
    val muted = ContextCompat.getColor(ctx, R.color.sk_on_surface_variant)
    when (state.tasbeehClickMode) {
      com.example.model.TasbeehClickMode.RING -> {
        binding.btnTasbeehSound.setImageResource(R.drawable.ic_sk_volume_up)
        tint(binding.btnTasbeehSound, emerald)
        binding.btnTasbeehSound.contentDescription = "Ring mode (Sound on tap)"
      }
      com.example.model.TasbeehClickMode.VIBRATE -> {
        binding.btnTasbeehSound.setImageResource(R.drawable.ic_sk_vibration)
        tint(binding.btnTasbeehSound, emerald)
        binding.btnTasbeehSound.contentDescription = "Vibrate mode (Haptic on tap)"
      }
      com.example.model.TasbeehClickMode.SILENT -> {
        binding.btnTasbeehSound.setImageResource(R.drawable.ic_sk_volume_mute)
        tint(binding.btnTasbeehSound, muted)
        binding.btnTasbeehSound.contentDescription = "Silent mode (No sound or vibration)"
      }
    }
    binding.btnTasbeehHaptic.visibility = View.GONE

    // Active dhikr card
    binding.tvDhikrBadge.text = dhikr.transliteration.uppercase()
    binding.btnDhikrTarget.text = "/$target"
    binding.tvDhikrArabic.text = dhikr.arabicText
    binding.tvDhikrTranslit.text = dhikr.transliteration
    binding.tvDhikrTranslation.text = "\u201C${dhikr.translation}\u201D"
    val progress = (count.toFloat() / target.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    binding.pbDhikrProgress.progress = (progress * binding.pbDhikrProgress.max).toInt()

    renderStyleSelection(state.selectedTasbeehStyle)
    detectCompletion(count, laps, target)
  }

  private fun tint(button: ImageButton, color: Int) {
    button.imageTintList = ColorStateList.valueOf(color)
  }

  // Detect completion pulse (same semantics as the Compose LaunchedEffect(count, laps))
  private fun detectCompletion(count: Int, laps: Int, target: Int) {
    val key = count to laps
    if (key == lastCountLaps) return
    lastCountLaps = key
    celebrationJob?.cancel()
    if (count == 0 && laps > 0) {
      binding.tvTasbeehCelebration.text = "Masha'Allah! Target $target completed! Starting Lap ${laps + 1}"
      showCelebration(true)
      celebrationJob = viewLifecycleOwner.lifecycleScope.launch {
        delay(2200)
        showCelebration(false)
      }
    }
  }

  private fun showCelebration(visible: Boolean) {
    if (!isBindingAvailable) return
    val banner = binding.llTasbeehCelebration
    if (visible) {
      if (banner.visibility != View.VISIBLE) {
        banner.alpha = 0f
        banner.visibility = View.VISIBLE
        banner.animate().alpha(1f).setDuration(300).start()
      }
    } else {
      banner.animate().alpha(0f).setDuration(300).withEndAction { banner.visibility = View.GONE }.start()
    }
  }

  private fun subtitleFor(style: TasbeehStyle): String = when (style) {
    TasbeehStyle.SEEDS -> "Olive wood beads"
    TasbeehStyle.HAND_TASBEEH -> "Physical hand clicker"
    TasbeehStyle.BALLOONS_LINE -> "Floating balloons line"
    else -> style.subtitle
  }

  private fun buildStyleChips() {
    val inflater = LayoutInflater.from(requireContext())
    listOf(TasbeehStyle.SEEDS, TasbeehStyle.HAND_TASBEEH, TasbeehStyle.BALLOONS_LINE).forEach { style ->
      val item = ItemTasbeehStyleBinding.inflate(inflater, binding.llTasbeehStyles, false)
      item.tvStyleTitle.text = style.title
      item.tvStyleSubtitle.text = subtitleFor(style)
      item.root.setOnClickListener { viewModel.setTasbeehStyle(style) }
      styleBindings[style] = item
      binding.llTasbeehStyles.addView(item.root)
    }
  }

  private fun renderStyleSelection(selected: TasbeehStyle) {
    if (renderedStyle == selected) return
    renderedStyle = selected
    val ctx = requireContext()
    styleBindings.forEach { (style, item) ->
      val isSelected = style == selected
      item.root.setBackgroundResource(
        if (isSelected) R.drawable.bg_sk_chip_tasbeeh_selected else R.drawable.bg_sk_chip_tasbeeh_unselected
      )
      item.ivStyleCheck.visibility = if (isSelected) View.VISIBLE else View.GONE
      item.tvStyleTitle.setTextColor(
        ContextCompat.getColor(ctx, if (isSelected) R.color.sk_on_primary_container else R.color.sk_on_surface)
      )
      item.tvStyleTitle.typeface = Typeface.create(
        "sans-serif-medium", if (isSelected) Typeface.BOLD else Typeface.NORMAL
      )
      item.tvStyleSubtitle.setTextColor(
        ContextCompat.getColor(ctx, if (isSelected) R.color.sk_primary else R.color.sk_on_surface_variant)
      )
    }
  }

  private fun setupVisual() {
    binding.composeTasbeehVisual.apply {
      setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
      setContent {
        val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
        val isDark = when (uiState.themeMode) {
          ThemeMode.SYSTEM -> isSystemInDarkTheme()
          ThemeMode.LIGHT -> false
          ThemeMode.DARK -> true
        }
        SakinahTheme(darkTheme = isDark) {
          TasbeehInteractiveVisual(
            selectedStyle = uiState.selectedTasbeehStyle,
            count = uiState.tasbeehCount,
            totalTarget = uiState.tasbeehTotalTarget,
            dhikrName = uiState.currentDhikr.transliteration,
            onTap = { viewModel.incrementTasbeeh(context) }
          )
        }
      }
    }
  }

  // -------------------------------------------------------------
  // Bottom sheets: Dhikr catalog & target picker
  // -------------------------------------------------------------
  private fun showSheet(title: String, fill: (SheetTasbeehPickerBinding) -> Unit) {
    activeSheet?.dismiss()
    val sheetBinding = SheetTasbeehPickerBinding.inflate(layoutInflater)
    sheetBinding.tvSheetTitle.text = title
    fill(sheetBinding)
    val dialog = BottomSheetDialog(requireContext())
    dialog.setContentView(sheetBinding.root)
    dialog.setOnShowListener {
      dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let { sheet ->
        sheet.setBackgroundColor(Color.TRANSPARENT)
        BottomSheetBehavior.from(sheet).apply {
          skipCollapsed = true
          state = BottomSheetBehavior.STATE_EXPANDED
        }
      }
    }
    dialog.setOnDismissListener { if (activeSheet === dialog) activeSheet = null }
    activeSheet = dialog
    dialog.show()
  }

  private fun showDhikrPicker() {
    val selected = viewModel.uiState.value.currentDhikr
    showSheet("Select Dhikr Preset") { sheet ->
      DhikrPreset.PRESETS.forEach { preset: DhikrPreset ->
        val item = ItemDhikrPresetBinding.inflate(layoutInflater, sheet.llSheetItems, false)
        item.tvPresetTranslit.text = preset.transliteration
        item.tvPresetTranslation.text = preset.translation
        item.tvPresetArabic.text = preset.arabicText
        if (preset.id == selected.id) item.root.setBackgroundResource(R.drawable.bg_sk_sheet_item_selected)
        item.root.setOnClickListener {
          viewModel.selectDhikr(preset)
          activeSheet?.dismiss()
        }
        sheet.llSheetItems.addView(item.root)
      }
    }
  }

  private fun showTargetPicker() {
    val current = viewModel.uiState.value.tasbeehTotalTarget
    showSheet("Set Dhikr Target") { sheet ->
      listOf(33, 99, 100, 500, 1000).forEach { targetVal ->
        val item = ItemTasbeehTargetBinding.inflate(layoutInflater, sheet.llSheetItems, false)
        val isSelected = targetVal == current
        item.tvTargetLabel.text = "$targetVal Counts"
        item.tvTargetLabel.typeface = if (isSelected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        item.ivTargetCheck.visibility = if (isSelected) View.VISIBLE else View.GONE
        if (isSelected) item.root.setBackgroundResource(R.drawable.bg_sk_sheet_item_selected)
        item.root.setOnClickListener {
          viewModel.setTasbeehTarget(targetVal)
          activeSheet?.dismiss()
        }
        sheet.llSheetItems.addView(item.root)
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    celebrationJob?.cancel()
    activeSheet?.dismiss()
    activeSheet = null
    styleBindings.clear()
    renderedStyle = null
    lastCountLaps = null
  }
}