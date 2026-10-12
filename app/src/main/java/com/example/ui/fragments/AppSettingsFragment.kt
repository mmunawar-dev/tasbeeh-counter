package com.example.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.databinding.FragmentAppSettingsBinding
import com.example.model.ThemeMode
import com.example.ui.activities.PremiumActivity
import com.example.ui.navigation.navigateWithSlide
import com.example.viewmodel.SakinahViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AppSettingsFragment :
    BaseFragment<FragmentAppSettingsBinding>(FragmentAppSettingsBinding::inflate) {

    private val viewModel: SakinahViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSettingsBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val openPremium = {
            startActivity(Intent(requireContext(), PremiumActivity::class.java))
        }
        binding.cardSettingsPremium.setOnClickListener { openPremium() }
        binding.btnGoPremium.setOnClickListener { openPremium() }

        binding.rowSettingsLanguage.setOnClickListener {
            findNavController().navigateWithSlide(R.id.languageSelectFragment)
        }

        binding.rowSettingsFiqh.setOnClickListener {
            findNavController().navigateWithSlide(R.id.fiqhSelectFragment)
        }

        binding.rowSettingsLocation.setOnClickListener {
            findNavController().navigateWithSlide(R.id.citySelectFragment)
        }

        binding.rowSettingsPrayerNotifications.setOnClickListener {
            findNavController().navigateWithSlide(R.id.prayerSettingsFragment)
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            val targetMode = if (isChecked) ThemeMode.DARK else ThemeMode.LIGHT
            if (viewModel.uiState.value.themeMode != targetMode) {
                viewModel.setThemeMode(targetMode)
            }
        }

        // Share App
        binding.rowSettingsShare.setOnClickListener {
            val appPackage = requireContext().packageName
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Sakinah - Modern Islamic Companion")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Experience peace and serenity in your daily prayers with Sakinah. Features accurate prayer times, Qibla compass, Tasbeeh counter, and authentic Duas.\n\nDownload now: https://play.google.com/store/apps/details?id=$appPackage"
                )
            }
            startActivity(Intent.createChooser(shareIntent, "Share Sakinah via"))
        }

        // Rate App
        binding.rowSettingsRate.setOnClickListener {
            val appPackage = requireContext().packageName
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackage")))
            } catch (_: Exception) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackage")))
            }
        }

        // Privacy Policy
        binding.rowSettingsPrivacy.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Privacy & Data Protection")
                .setMessage("Sakinah is built with complete privacy in mind.\n\n• 100% On-Device Calculations: All prayer times and Qibla calculations occur directly on your device.\n• No Location Tracking: Your location coordinates are never stored on external servers or sold to third parties.\n• No Data Harvesting: Your personal dhikr counters, notes, and preferences remain local to your phone.\n\nPeace of mind for your worship.")
                .setPositiveButton("Understood", null)
                .show()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.tvSettingsLanguageVal.text = state.selectedLanguage.displayName
                binding.tvSettingsFiqhVal.text =
                    "${state.selectedMadhab.displayName} • ${state.calculationMethod.shortName}"
                binding.tvSettingsLocationVal.text = state.selectedCity.name

                binding.tvSettingsThemeVal.text = state.themeMode.label
                val isDark = state.themeMode == ThemeMode.DARK
                if (binding.switchTheme.isChecked != isDark) {
                    binding.switchTheme.isChecked = isDark
                }
            }
        }
    }
}
