package com.example.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.MainActivity
import com.example.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

  private lateinit var binding: FragmentSplashBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = FragmentSplashBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.splashContentContainer.alpha = 0f
    binding.splashContentContainer.scaleX = 0.92f
    binding.splashContentContainer.scaleY = 0.92f

    binding.splashContentContainer.animate()
      .alpha(1f)
      .scaleX(1f)
      .scaleY(1f)
      .setDuration(700)
      .withEndAction {
        lifecycleScope.launch {
          delay(1200)
          val prefs = getSharedPreferences("sakinah_prefs", Context.MODE_PRIVATE)
          val isOnboarded = prefs.getBoolean("onboarding_completed", false)
          val nextIntent = if (isOnboarded) {
            Intent(this@SplashActivity, MainActivity::class.java)
          } else {
            Intent(this@SplashActivity, OnboardingActivity::class.java)
          }
          startActivity(nextIntent)
          finish()
        }
      }
      .start()
  }
}
