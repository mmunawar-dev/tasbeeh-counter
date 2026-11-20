package com.example.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.databinding.FragmentPremiumBinding
import com.example.model.PremiumPlan
import com.example.viewmodel.SakinahViewModel

class PremiumFragment : BaseFragment<FragmentPremiumBinding>(FragmentPremiumBinding::inflate) {

  private val viewModel: SakinahViewModel by activityViewModels()
  private var selectedPlan = PremiumPlan.WEEKLY

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Close button (top right)
    binding.btnPremiumClose.setOnClickListener {
      findNavController().navigateUp()
    }

    // Default selection is Weekly Premium as shown in reference design
    updatePlanSelection(PremiumPlan.WEEKLY)

    // Option cards selection
    binding.cardMonthly.setOnClickListener {
      updatePlanSelection(PremiumPlan.WEEKLY)
    }

    binding.cardPlanYearly.setOnClickListener {
      updatePlanSelection(PremiumPlan.YEARLY)
    }

    // Continue CTA Button
    binding.btnPremiumContinue.setOnClickListener {
      Toast.makeText(
        requireContext(),
        "Starting 3-Day Free Trial for ${selectedPlan.title}...",
        Toast.LENGTH_SHORT
      ).show()
      findNavController().navigateUp()
    }

    // Footer Links
    binding.btnTermsConditions.setOnClickListener {
      Toast.makeText(
        requireContext(),
        "Terms & Conditions: Ad-free pure Islamic companion, cancel anytime.",
        Toast.LENGTH_SHORT
      ).show()
    }

    binding.txtTermCondition.setOnClickListener {
      Toast.makeText(
        requireContext(),
        "Checking Google Play Store... No active purchases found.",
        Toast.LENGTH_SHORT
      ).show()
    }

    binding.btnPremiumContinue.setOnClickListener {
      Toast.makeText(
        requireContext(),
        "Subscription auto-renews after 3-day trial. Manage or cancel anytime in Play Store.",
        Toast.LENGTH_LONG
      ).show()
    }
  }

  private fun updatePlanSelection(plan: PremiumPlan) {
    selectedPlan = plan
    if (plan == PremiumPlan.WEEKLY) {
      binding.cardMonthly.setBackgroundResource(R.drawable.bg_premium_card_selected)
      binding.ivRadioWeekly.setImageResource(R.drawable.ic_check_circle_teal)

      binding.cardPlanYearly.setBackgroundResource(R.drawable.bg_premium_card_unselected)
      binding.ivRadioYearly.setImageResource(R.drawable.ic_radio_circle_empty)
    } else {
      binding.cardMonthly.setBackgroundResource(R.drawable.bg_premium_card_unselected)
      binding.ivRadioWeekly.setImageResource(R.drawable.ic_radio_circle_empty)

      binding.cardPlanYearly.setBackgroundResource(R.drawable.bg_premium_card_selected)
      binding.ivRadioYearly.setImageResource(R.drawable.ic_check_circle_teal)
    }
  }
}
