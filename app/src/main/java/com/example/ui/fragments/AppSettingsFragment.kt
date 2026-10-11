package com.example.ui.fragments

import android.content.Intent
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

        binding.cardSettingsPremium.setOnClickListener {
            startActivity(Intent(requireContext(), PremiumActivity::class.java))
        }

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


        binding.switchTheme.setOnCheckedChangeListener { button, isChecked ->
            run {
                if (isChecked) {
                    viewModel.setThemeMode(ThemeMode.DARK)
                } else {
                    viewModel.setThemeMode(ThemeMode.LIGHT)
                }

            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                binding.tvSettingsLanguageVal.text = state.selectedLanguage.displayName
                binding.tvSettingsFiqhVal.text =
                    "${state.selectedMadhab.displayName} • ${state.calculationMethod.shortName}"
                binding.tvSettingsLocationVal.text = state.selectedCity.name

                binding.tvSettingsThemeVal.text = state.themeMode.label

            }
        }
    }
}
