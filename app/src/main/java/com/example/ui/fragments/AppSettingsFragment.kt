package com.example.ui.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.databinding.DialogRateUsBinding
import com.example.databinding.FragmentAppSettingsBinding
import com.example.model.ThemeMode
import com.example.ui.activities.PremiumActivity
import com.example.ui.navigation.navigateWithSlide
import androidx.appcompat.app.AppCompatDelegate
import com.example.service.SakinahPreferences
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
            SakinahPreferences.saveDarkTheme(requireContext(), isChecked)
            val targetMode = if (isChecked) ThemeMode.DARK else ThemeMode.LIGHT
            if (viewModel.uiState.value.themeMode != targetMode) {
                viewModel.setThemeMode(targetMode)
            }
            val nightMode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(nightMode)
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
            showRateUsDialog()
        }

        // Privacy Policy
        binding.rowSettingsPrivacy.setOnClickListener {
            val url = "https://mmunawar-dev.github.io/deen_privacy_policy/"
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireActivity(), "Device does not have a browser", Toast.LENGTH_SHORT).show()
            }
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

    private fun showRateUsDialog() {
        val dialogBinding = DialogRateUsBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val stars = listOf(
            dialogBinding.ivStar1,
            dialogBinding.ivStar2,
            dialogBinding.ivStar3,
            dialogBinding.ivStar4,
            dialogBinding.ivStar5
        )

        fun updateStars(rating: Int) {
            stars.forEachIndexed { index, starView ->
                if (index < rating) {
                    starView.setImageResource(R.drawable.ic_star_filled)
                } else {
                    starView.setImageResource(R.drawable.ic_star_unfilled)
                }
            }
        }

        // Default: 2 stars filled, 3 stars unfilled
        updateStars(2)

        stars.forEachIndexed { index, starView ->
            starView.setOnClickListener {
                updateStars(index + 1)
            }
        }

        dialogBinding.btnRateCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnRateSubmit.setOnClickListener {
            val appPackage = requireContext().packageName
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackage")))
            } catch (_: Exception) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackage")))
            }
            dialog.dismiss()
        }

        dialog.show()
    }
}
