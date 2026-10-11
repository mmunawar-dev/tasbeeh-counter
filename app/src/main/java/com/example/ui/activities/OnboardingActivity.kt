package com.example.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.MainActivity
import com.example.R
import com.example.databinding.FragmentOnboardingBinding
import com.example.ui.adapters.OnboardingAdapter

class OnboardingActivity : AppCompatActivity() {

  private lateinit var binding: FragmentOnboardingBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = FragmentOnboardingBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val adapter = OnboardingAdapter()
    binding.vpOnboarding.adapter = adapter

    binding.vpOnboarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
      override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        updateDots(position)
        if (position == adapter.pages.lastIndex) {
          binding.tvBtnOnboardingText.text = "Get Started"
        } else {
          binding.tvBtnOnboardingText.text = "Next"
        }
      }
    })

    binding.btnOnboardingNext.setOnClickListener {
      val current = binding.vpOnboarding.currentItem
      if (current < adapter.pages.lastIndex) {
        binding.vpOnboarding.currentItem = current + 1
      } else {
        completeOnboarding()
      }
    }
  }

  private fun completeOnboarding() {
    val prefs = getSharedPreferences("sakinah_prefs", Context.MODE_PRIVATE)
    prefs.edit().putBoolean("onboarding_completed", true).apply()
    startActivity(Intent(this, MainActivity::class.java))
    finish()
  }

  private fun updateDots(activeIndex: Int) {
    val dots = listOf(binding.dot0, binding.dot1, binding.dot2)
    val density = resources.displayMetrics.density

    dots.forEachIndexed { index, dot ->
      if (index == activeIndex) {
        dot.setBackgroundResource(R.drawable.bg_dot_active)
        dot.layoutParams = dot.layoutParams.apply {
          width = (22 * density).toInt()
          height = (6 * density).toInt()
        }
      } else {
        dot.setBackgroundResource(R.drawable.bg_dot_inactive)
        dot.layoutParams = dot.layoutParams.apply {
          width = (6 * density).toInt()
          height = (6 * density).toInt()
        }
      }
    }
  }
}
