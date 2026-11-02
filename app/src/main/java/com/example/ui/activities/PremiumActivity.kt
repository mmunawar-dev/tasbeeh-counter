package com.example.ui.activities

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.databinding.ActivityPremiumBinding
import com.example.model.PremiumPlan

class PremiumActivity : AppCompatActivity() {

  private lateinit var binding: ActivityPremiumBinding
  private var selectedPlan: PremiumPlan = PremiumPlan.YEARLY

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityPremiumBinding.inflate(layoutInflater)
    enableEdgeToEdge()
    setContentView(binding.root)
    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->

      val systemBars = insets.getInsets(
        WindowInsetsCompat.Type.systemBars()
      )

      view.updatePadding(
        left = systemBars.left,
        top = 0,
        right = systemBars.right,
        bottom = systemBars.bottom
      )

      insets
    }

    binding.btnPremiumBack.setOnClickListener {
      finish()
    }

    // Underline footer links
    binding.tvPrivacyPolicy.paintFlags = binding.tvPrivacyPolicy.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    binding.tvTermsConditions.paintFlags = binding.tvTermsConditions.paintFlags or Paint.UNDERLINE_TEXT_FLAG

    binding.btnPrivacyPolicy.setOnClickListener {
      Toast.makeText(this, "Privacy Policy: All calculations run 100% on device.", Toast.LENGTH_SHORT).show()
    }

    binding.btnTermsConditions.setOnClickListener {
      Toast.makeText(this, "Terms & Conditions: Pure Islamic companion, no data shared.", Toast.LENGTH_SHORT).show()
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
//      unlockPlan(PremiumPlan.YEARLY)
    }

    binding.btnGoLifetime.setOnClickListener {
      selectedPlan = PremiumPlan.LIFETIME
//      unlockPlan(PremiumPlan.LIFETIME)
    }
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

