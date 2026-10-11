package com.example.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.databinding.FragmentPremiumBinding
import com.example.model.PremiumPlan
import com.example.viewmodel.SakinahViewModel

class PremiumFragment : BaseFragment<FragmentPremiumBinding>(FragmentPremiumBinding::inflate) {

  private val viewModel: SakinahViewModel by activityViewModels()
  private var selectedPlan = PremiumPlan.YEARLY

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.btnPremiumBack.setOnClickListener {
      findNavController().navigateUp()
    }

    updatePlanSelection(PremiumPlan.YEARLY)

    binding.cardPlanMonthly.setOnClickListener {
      updatePlanSelection(PremiumPlan.MONTHLY)
    }

    binding.cardPlanYearly.setOnClickListener {
      updatePlanSelection(PremiumPlan.YEARLY)
    }

    binding.cardPlanLifetime.setOnClickListener {
      updatePlanSelection(PremiumPlan.LIFETIME)
    }

    binding.btnUnlockPremium.setOnClickListener {
      viewModel.unlockPremium()
      Toast.makeText(requireContext(), "Alhamdulillah! Sakinah Plus activated.", Toast.LENGTH_SHORT).show()
      findNavController().navigateUp()
    }
  }

  private fun updatePlanSelection(plan: PremiumPlan) {
    selectedPlan = plan
    val context = requireContext()

    binding.rbPlanMonthly.isChecked = plan == PremiumPlan.MONTHLY
    binding.rbPlanYearly.isChecked = plan == PremiumPlan.YEARLY
    binding.rbPlanLifetime.isChecked = plan == PremiumPlan.LIFETIME

    // Monthly
    if (plan == PremiumPlan.MONTHLY) {
      binding.cardPlanMonthly.setCardBackgroundColor(ContextCompat.getColor(context, R.color.active_prayer_bg))
      binding.cardPlanMonthly.strokeColor = ContextCompat.getColor(context, R.color.active_prayer_border)
      binding.cardPlanMonthly.strokeWidth = (1.5f * resources.displayMetrics.density).toInt()
    } else {
      binding.cardPlanMonthly.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surface_light))
      binding.cardPlanMonthly.strokeColor = ContextCompat.getColor(context, R.color.outline_border)
      binding.cardPlanMonthly.strokeWidth = (1f * resources.displayMetrics.density).toInt()
    }

    // Yearly
    if (plan == PremiumPlan.YEARLY) {
      binding.cardPlanYearly.setCardBackgroundColor(ContextCompat.getColor(context, R.color.active_prayer_bg))
      binding.cardPlanYearly.strokeColor = ContextCompat.getColor(context, R.color.active_prayer_border)
      binding.cardPlanYearly.strokeWidth = (1.5f * resources.displayMetrics.density).toInt()
    } else {
      binding.cardPlanYearly.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surface_light))
      binding.cardPlanYearly.strokeColor = ContextCompat.getColor(context, R.color.outline_border)
      binding.cardPlanYearly.strokeWidth = (1f * resources.displayMetrics.density).toInt()
    }

    // Lifetime
    if (plan == PremiumPlan.LIFETIME) {
      binding.cardPlanLifetime.setCardBackgroundColor(ContextCompat.getColor(context, R.color.active_prayer_bg))
      binding.cardPlanLifetime.strokeColor = ContextCompat.getColor(context, R.color.active_prayer_border)
      binding.cardPlanLifetime.strokeWidth = (1.5f * resources.displayMetrics.density).toInt()
    } else {
      binding.cardPlanLifetime.setCardBackgroundColor(ContextCompat.getColor(context, R.color.surface_light))
      binding.cardPlanLifetime.strokeColor = ContextCompat.getColor(context, R.color.outline_border)
      binding.cardPlanLifetime.strokeWidth = (1f * resources.displayMetrics.density).toInt()
    }
  }
}
