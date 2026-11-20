package com.example.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.R
import com.example.databinding.ActivityPremiumBinding
import com.example.model.PremiumPlan

class PremiumActivity : AppCompatActivity() {

  private lateinit var binding: ActivityPremiumBinding

  // Map UI card IDs to plans:
  // card_plan_weekly  → MONTHLY Plan (Figma: Monthly Plan)
  // card_plan_yearly  → LIFETIME Plan (Figma: Lifetime Plan — Recommended)
  private var selectedPlan: PremiumPlan = PremiumPlan.LIFETIME

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityPremiumBinding.inflate(layoutInflater)
    enableEdgeToEdge()
    setContentView(binding.root)

    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
      val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      view.updatePadding(
        left = systemBars.left,
        top = 0,
        right = systemBars.right,
        bottom = systemBars.bottom
      )
      insets
    }

    // Close button
    binding.btnPremiumClose.setOnClickListener {
      finish()
    }

    // Default selection = Lifetime (Recommended — matches Figma dark frame)
    updatePlanSelection(PremiumPlan.LIFETIME)

    // Monthly Plan card (mapped to card_plan_weekly ID for binding compatibility)
    binding.cardMonthly.setOnClickListener {
      updatePlanSelection(PremiumPlan.WEEKLY)
    }

    // Lifetime Plan card (mapped to card_plan_yearly ID for binding compatibility)
    binding.cardPlanYearly.setOnClickListener {
      updatePlanSelection(PremiumPlan.LIFETIME)
    }

    // Continue CTA
    binding.btnPremiumContinue.setOnClickListener {
      val planName = when (selectedPlan) {
        PremiumPlan.LIFETIME -> "Lifetime Plan"
        PremiumPlan.WEEKLY   -> "Monthly Plan"
        else                 -> selectedPlan.title
      }
      Toast.makeText(
        this,
        "Continuing with $planName...",
        Toast.LENGTH_SHORT
      ).show()
      finish()
    }

    // Footer links
    binding.txtTermCondition.setOnClickListener {
      Toast.makeText(
        this,
        "Terms & Conditions: Ad-free pure Islamic companion.",
        Toast.LENGTH_SHORT
      ).show()
    }

    binding.btnTermsConditions.setOnClickListener {
      Toast.makeText(
        this,
        "Privacy Policy: We respect your privacy. No personal data sold.",
        Toast.LENGTH_SHORT
      ).show()
    }
  }

  private fun updatePlanSelection(plan: PremiumPlan) {
    selectedPlan = plan

    if (plan == PremiumPlan.WEEKLY) {
      // Monthly selected (cardMonthly)
      binding.cardMonthly.setBackgroundResource(R.drawable.bg_premium_card_selected)
      binding.ivRadioWeekly.setImageResource(R.drawable.ic_radio_selected_gold)

      binding.cardPlanYearly.setBackgroundResource(R.drawable.bg_premium_card_unselected)
      binding.ivRadioYearly.setImageResource(R.drawable.ic_radio_circle_empty)
    } else {
      // Lifetime selected (cardPlanYearly)
      binding.cardMonthly.setBackgroundResource(R.drawable.bg_premium_card_unselected)
      binding.ivRadioWeekly.setImageResource(R.drawable.ic_radio_circle_empty)

      binding.cardPlanYearly.setBackgroundResource(R.drawable.bg_premium_card_selected)
      binding.ivRadioYearly.setImageResource(R.drawable.ic_radio_selected_gold)
    }
  }
}
