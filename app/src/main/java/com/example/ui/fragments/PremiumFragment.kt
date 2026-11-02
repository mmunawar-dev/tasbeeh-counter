package com.example.ui.fragments

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.databinding.FragmentPremiumBinding
import com.example.model.PremiumPlan
import com.example.viewmodel.SakinahViewModel

class PremiumFragment : BaseFragment<FragmentPremiumBinding>(FragmentPremiumBinding::inflate) {

  private val viewModel: SakinahViewModel by activityViewModels()
  private var selectedPlan = PremiumPlan.LIFETIME

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.btnPremiumBack.setOnClickListener {
      findNavController().navigateUp()
    }

    // Underline footer links
    binding.tvPrivacyPolicy.paintFlags = binding.tvPrivacyPolicy.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    binding.tvTermsConditions.paintFlags = binding.tvTermsConditions.paintFlags or Paint.UNDERLINE_TEXT_FLAG

    binding.btnPrivacyPolicy.setOnClickListener {
      Toast.makeText(requireContext(), "Privacy Policy: All calculations run 100% on device.", Toast.LENGTH_SHORT).show()
    }

    binding.btnTermsConditions.setOnClickListener {
      Toast.makeText(requireContext(), "Terms & Conditions: Pure Islamic companion, no data shared.", Toast.LENGTH_SHORT).show()
    }

    // Default selection is Lifetime Plan as highlighted in screenshot
    updatePlanSelection(PremiumPlan.LIFETIME)

    binding.cardPlanYearly.setOnClickListener {
      updatePlanSelection(PremiumPlan.YEARLY)
    }

    binding.cardPlanLifetime.setOnClickListener {
      updatePlanSelection(PremiumPlan.LIFETIME)
    }

    binding.btnSelectYearly.setOnClickListener {
      selectedPlan = PremiumPlan.YEARLY
      unlockPlan(PremiumPlan.YEARLY)
    }

    binding.btnGoLifetime.setOnClickListener {
      selectedPlan = PremiumPlan.LIFETIME
      unlockPlan(PremiumPlan.LIFETIME)
    }
  }

  private fun unlockPlan(plan: PremiumPlan) {
    viewModel.unlockPremium()
    val planName = if (plan == PremiumPlan.LIFETIME) "Lifetime Access" else "Yearly Plan"
    Toast.makeText(requireContext(), "Alhamdulillah! Sakinah Plus ($planName) unlocked.", Toast.LENGTH_SHORT).show()
    findNavController().navigateUp()
  }

  private fun updatePlanSelection(plan: PremiumPlan) {
    selectedPlan = plan
    val density = resources.displayMetrics.density
    val goldColor = Color.parseColor("#E5A93C")
    val defaultBorderColor = Color.parseColor("#386A50")

    if (plan == PremiumPlan.YEARLY) {
      binding.cardPlanYearly.strokeColor = goldColor
      binding.cardPlanYearly.strokeWidth = (2f * density).toInt()

      binding.cardPlanLifetime.strokeColor = defaultBorderColor
      binding.cardPlanLifetime.strokeWidth = (1.2f * density).toInt()
    } else {
      binding.cardPlanLifetime.strokeColor = goldColor
      binding.cardPlanLifetime.strokeWidth = (2f * density).toInt()

      binding.cardPlanYearly.strokeColor = defaultBorderColor
      binding.cardPlanYearly.strokeWidth = (1.2f * density).toInt()
    }
  }
}
