package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.PrayerNotificationMode
import com.example.model.PrayerTimeItem
import com.example.model.PrayerType

@Composable
fun CurrentPrayerCard(
  activePrayer: PrayerTimeItem?,
  nextPrayer: PrayerTimeItem,
  language: AppLanguage,
  sunriseItem: PrayerTimeItem,
  currentNotificationMode: PrayerNotificationMode,
  onNotificationModeChange: (PrayerType, PrayerNotificationMode) -> Unit,
  onOpenPrayerTimes: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isDark = isSystemInDarkTheme()
  val isCurrentActive = activePrayer != null && activePrayer.type != PrayerType.SUNRISE
  val primaryDisplay = if (isCurrentActive) activePrayer!! else nextPrayer

  // Premium jewel two-color gradient with strong text contrast
  val gradientColors = if (isDark) {
    if (isCurrentActive) {
      listOf(Color(0xFF0F382A), Color(0xFF1E5240))
    } else {
      listOf(Color(0xFF142922), Color(0xFF1E3D34))
    }
  } else {
    if (isCurrentActive) {
      listOf(Color(0xFF0B4634), Color(0xFF186850))
    } else {
      listOf(Color(0xFF1A4537), Color(0xFF285E4D))
    }
  }

  val gradientBrush = Brush.linearGradient(colors = gradientColors)
  val borderColor = if (isCurrentActive) Color(0xFF38B289) else Color(0xFF568070)

  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    border = BorderStroke(1.2.dp, borderColor),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    modifier = modifier
      .fillMaxWidth()
      .testTag("current_prayer_card")
      .clip(RoundedCornerShape(22.dp))
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(gradientBrush)
        .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        // TOP ROW: Status badge & Compact "Prayer Times →" Button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Status Pill (Single language, clean badge)
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0x33000000),
            border = BorderStroke(
              1.dp,
              if (isCurrentActive) Color(0x8055E6B7) else Color(0x80ECC25D)
            )
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(7.dp)
                  .background(
                    if (isCurrentActive) Color(0xFF55E6B7) else Color(0xFFECC25D),
                    CircleShape
                  )
              )
              Text(
                text = if (isCurrentActive) "CURRENT PRAYER" else "UPCOMING PRAYER",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  fontSize = 10.sp
                ),
                color = if (isCurrentActive) Color(0xFF55E6B7) else Color(0xFFECC25D)
              )
            }
          }

          // Compact Button: Opens dedicated Prayer Times screen
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0x33FFFFFF),
            border = BorderStroke(1.dp, Color(0x66FFFFFF)),
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .clickable(onClick = onOpenPrayerTimes)
              .testTag("prayer_schedule_action_button")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(5.dp),
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Text(
                text = "Prayer Times",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.5.sp
                ),
                color = Color.White
              )
              Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                contentDescription = "Prayer Times",
                tint = Color.White,
                modifier = Modifier.size(13.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // PRAYER NAME & REMAINING TIME ROW (No repeated Urdu, no labels, no click to bottom sheet)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Prayer Name (Clean, single language name)
          Text(
            text = primaryDisplay.type.localizedName(language),
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              fontSize = 32.sp,
              letterSpacing = (-0.5).sp
            ),
            color = Color.White
          )

          // Remaining Time Pill (High contrast badge)
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isCurrentActive) Color(0xFFECC25D) else Color(0x33000000),
            border = if (isCurrentActive) null else BorderStroke(1.dp, Color(0x6655E6B7))
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = null,
                tint = if (isCurrentActive) Color(0xFF18281C) else Color(0xFF55E6B7),
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = "${primaryDisplay.remainingTimeFormatted} remaining",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                ),
                color = if (isCurrentActive) Color(0xFF18281C) else Color(0xFF55E6B7)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // START & END TIMES ROW
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Started ${primaryDisplay.timeFormatted}",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Medium,
              fontSize = 13.5.sp
            ),
            color = Color(0xFFE0F2EB)
          )

          Text(
            text = "•",
            color = Color(0x80FFFFFF)
          )

          Text(
            text = "Ends ${primaryDisplay.endFormatted}",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Medium,
              fontSize = 13.5.sp
            ),
            color = Color(0xFFE0F2EB)
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SUBTLE DIVIDER
        HorizontalDivider(
          modifier = Modifier.fillMaxWidth(),
          color = Color(0x33FFFFFF),
          thickness = 0.8.dp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // QUICK NOTIFICATION CONTROLS: Sound, Vibrate, Silent, Off
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = when (currentNotificationMode) {
                PrayerNotificationMode.SOUND -> Icons.Outlined.NotificationsActive
                PrayerNotificationMode.VIBRATE -> Icons.Outlined.Vibration
                PrayerNotificationMode.SILENT -> Icons.Outlined.Notifications
                PrayerNotificationMode.OFF -> Icons.Outlined.NotificationsOff
              },
              contentDescription = null,
              tint = Color(0xFFD0EDE2),
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "Alert:",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
              ),
              color = Color(0xFFD0EDE2)
            )
          }

          // Segmented Toggle Pills
          Row(
            modifier = Modifier
              .background(Color(0x26000000), RoundedCornerShape(10.dp))
              .padding(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            val modes = listOf(
              Triple(PrayerNotificationMode.SOUND, Icons.Outlined.NotificationsActive, "Sound"),
              Triple(PrayerNotificationMode.VIBRATE, Icons.Outlined.Vibration, "Vibrate"),
              Triple(PrayerNotificationMode.SILENT, Icons.Outlined.Notifications, "Silent"),
              Triple(PrayerNotificationMode.OFF, Icons.Outlined.NotificationsOff, "Off")
            )

            modes.forEach { (mode, icon, label) ->
              val isSelected = currentNotificationMode == mode
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) Color.White else Color.Transparent,
                shadowElevation = if (isSelected) 2.dp else 0.dp,
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .clickable {
                    onNotificationModeChange(primaryDisplay.type, mode)
                  }
                  .testTag("quick_mode_${primaryDisplay.type.name.lowercase()}_${mode.name.lowercase()}")
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) Color(0xFF0F382A) else Color(0xCCFFFFFF),
                    modifier = Modifier.size(13.dp)
                  )
                  if (isSelected) {
                    Text(
                      text = label,
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.5.sp
                      ),
                      color = Color(0xFF0F382A)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
