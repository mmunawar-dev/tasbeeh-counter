package com.example.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.R

val slideNavOptions: NavOptions = NavOptions.Builder()
  .setEnterAnim(R.anim.slide_in_right)
  .setExitAnim(R.anim.slide_out_left)
  .setPopEnterAnim(R.anim.slide_in_left)
  .setPopExitAnim(R.anim.slide_out_right)
  .build()

fun NavController.navigateWithSlide(destinationId: Int) {
  try {
    navigate(destinationId, null, slideNavOptions)
  } catch (e: Exception) {
    navigate(destinationId)
  }
}
