package com.example.ui.activities

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.R
import com.example.databinding.ActivityPremiumBinding
import com.example.model.PremiumPlan

class PremiumActivity : AppCompatActivity() {

  private lateinit var binding: ActivityPremiumBinding
  private var selectedPlan: PremiumPlan = PremiumPlan.YEARLY

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityPremiumBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.toolbarPremium.setNavigationOnClickListener {
      finish()
    }

    setupPlanSelection()
    updatePlanUI(PremiumPlan.YEARLY)

    binding.btnSubscribe.setOnClickListener {
      Toast.makeText(
        this,
        "Subscribed to ${selectedPlan.title} (${selectedPlan.priceFormatted})! Thank you for supporting Sakinah.",
        Toast.LENGTH_LONG
      ).show()
      // Save state
      val prefs = getSharedPreferences("sakinah_prefs", Context.MODE_PRIVATE)
      prefs.edit().putBoolean("is_premium", true).apply()
      finish()
    }

    binding.tvRestorePurchases.setOnClickListener {
      Toast.makeText(this, "Purchases restored successfully.", Toast.LENGTH_SHORT).show()
    }
  }

  private fun setupPlanSelection() {
    binding.cardPlanMonthly.setOnClickListener {
      updatePlanUI(PremiumPlan.MONTHLY)
    }

    binding.cardPlanYearly.setOnClickListener {
      updatePlanUI(PremiumPlan.YEARLY)
    }

    binding.cardPlanLifetime.setOnClickListener {
      updatePlanUI(PremiumPlan.LIFETIME)
    }
  }

  private fun updatePlanUI(plan: PremiumPlan) {
    selectedPlan = plan

    val activeBg = ContextCompat.getColor(this, R.color.active_prayer_bg)
    val defaultBg = ContextCompat.getColor(this, R.color.surface_light)
    val activeBorder = ContextCompat.getColor(this, R.color.active_prayer_border)
    val defaultBorder = ContextCompat.getColor(this, R.color.outline_border)

    // Reset Monthly
    if (plan == PremiumPlan.MONTHLY) {
      binding.cardPlanMonthly.setCardBackgroundColor(activeBg)
      binding.cardPlanMonthly.strokeColor = activeBorder
      binding.cardPlanMonthly.strokeWidth = dpToPx(2)
    } else {
      binding.cardPlanMonthly.setCardBackgroundColor(defaultBg)
      binding.cardPlanMonthly.strokeColor = defaultBorder
      binding.cardPlanMonthly.strokeWidth = dpToPx(1)
    }

    // Reset Yearly
    if (plan == PremiumPlan.YEARLY) {
      binding.cardPlanYearly.setCardBackgroundColor(activeBg)
      binding.cardPlanYearly.strokeColor = activeBorder
      binding.cardPlanYearly.strokeWidth = dpToPx(2)
    } else {
      binding.cardPlanYearly.setCardBackgroundColor(defaultBg)
      binding.cardPlanYearly.strokeColor = defaultBorder
      binding.cardPlanYearly.strokeWidth = dpToPx(1)
    }

    // Reset Lifetime
    if (plan == PremiumPlan.LIFETIME) {
      binding.cardPlanLifetime.setCardBackgroundColor(activeBg)
      binding.cardPlanLifetime.strokeColor = activeBorder
      binding.cardPlanLifetime.strokeWidth = dpToPx(2)
    } else {
      binding.cardPlanLifetime.setCardBackgroundColor(defaultBg)
      binding.cardPlanLifetime.strokeColor = defaultBorder
      binding.cardPlanLifetime.strokeWidth = dpToPx(1)
    }

    // Update CTA button text
    binding.btnSubscribe.text = when (plan) {
      PremiumPlan.MONTHLY -> "Subscribe Monthly • $1.99/mo"
      PremiumPlan.YEARLY -> "Start Free Trial • Then $9.99/yr"
      PremiumPlan.LIFETIME -> "Unlock Lifetime Access • $24.99"
    }
  }

  private fun dpToPx(dp: Int): Int {
    val density = resources.displayMetrics.density
    return (dp * density).toInt()
  }
}
