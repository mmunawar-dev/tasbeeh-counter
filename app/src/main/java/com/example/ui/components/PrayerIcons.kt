package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.Brightness5
import androidx.compose.material.icons.outlined.Brightness6
import androidx.compose.material.icons.outlined.BrightnessLow
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.WbTwilight
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.model.PrayerType

@Composable
fun getPrayerIcon(type: PrayerType): ImageVector {
  return when (type) {
    PrayerType.FAJR -> Icons.Outlined.BrightnessLow
    PrayerType.SUNRISE -> Icons.Outlined.WbTwilight
    PrayerType.DHUHR -> Icons.Outlined.WbSunny
    PrayerType.ASR -> Icons.Outlined.Brightness5
    PrayerType.MAGHRIB -> Icons.Outlined.Brightness6
    PrayerType.ISHA -> Icons.Outlined.Bedtime
  }
}
