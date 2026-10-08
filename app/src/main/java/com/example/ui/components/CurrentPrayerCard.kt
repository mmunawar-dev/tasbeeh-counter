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

  // Light theme matching reference UI:
  // Warm creamy parchment/sand background gradient with subtle warm tone
  val lightBgGradient = listOf(Color(0xFFF9F5EC), Color(0xFFF4EFE2))
  val lightBorder = Color(0xFFE8DECB)

  // Dark theme: deep emerald jewel gradient
  val darkBgGradient = if (isCurrentActive) {
    listOf(Color(0xFF0F382A), Color(0xFF1E5240))
  } else {
    listOf(Color(0xFF142922), Color(0xFF1E3D34))
  }
  val darkBorder = if (isCurrentActive) Color(0xFF38B289) else Color(0xFF568070)

  val gradientColors = if (isDark) darkBgGradient else lightBgGradient
  val borderColor = if (isDark) darkBorder else lightBorder

  Card(
    shape = RoundedCornerShape(26.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    border = BorderStroke(1.2.dp, borderColor),
    elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 4.dp else 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .testTag("current_prayer_card")
      .clip(RoundedCornerShape(26.dp))
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(Brush.verticalGradient(colors = gradientColors))
        .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        // TOP ROW: Status badge (e.g. UPCOMING PRAYER) & "Prayer Times →" Button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Status Pill: Ochre/Olive outlined chip as in reference UI
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isDark) Color(0x33000000) else Color(0x1F8A6B38),
            border = BorderStroke(
              1.dp,
              if (isDark) {
                if (isCurrentActive) Color(0x8055E6B7) else Color(0x80ECC25D)
              } else {
                Color(0xFFC7B18E)
              }
            )
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(6.dp)
                  .background(
                    if (isDark) {
                      if (isCurrentActive) Color(0xFF55E6B7) else Color(0xFFECC25D)
                    } else {
                      Color(0xFF8A6B38)
                    },
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
                color = if (isDark) {
                  if (isCurrentActive) Color(0xFF55E6B7) else Color(0xFFECC25D)
                } else {
                  Color(0xFF7A5C2B)
                }
              )
            }
          }

          // "Prayer Times →" Button (outlined pill with arrow)
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isDark) Color(0x33FFFFFF) else Color(0x1F8A6B38),
            border = BorderStroke(1.dp, if (isDark) Color(0x66FFFFFF) else Color(0xFFC7B18E)),
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .clickable(onClick = onOpenPrayerTimes)
              .testTag("prayer_schedule_action_button")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Text(
                text = "Prayer Times",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                ),
                color = if (isDark) Color.White else Color(0xFF5A4420)
              )
              Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                contentDescription = "Prayer Times",
                tint = if (isDark) Color.White else Color(0xFF5A4420),
                modifier = Modifier.size(13.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // PRAYER NAME & REMAINING TIME ROW
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Prayer Name: Deep forest green in light mode (e.g. Dhuhr), Crisp white in dark mode
          Text(
            text = primaryDisplay.type.localizedName(language),
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 32.sp,
              letterSpacing = (-0.5).sp
            ),
            color = if (isDark) Color.White else Color(0xFF133E31)
          )

          // Remaining Time Pill (Ochre outlined badge with clock icon)
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = if (isDark) {
              if (isCurrentActive) Color(0xFFECC25D) else Color(0x33000000)
            } else {
              Color(0x1F8A6B38)
            },
            border = BorderStroke(
              1.dp,
              if (isDark) {
                if (isCurrentActive) Color(0xFFECC25D) else Color(0x6655E6B7)
              } else {
                Color(0xFFC7B18E)
              }
            )
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = null,
                tint = if (isDark) {
                  if (isCurrentActive) Color(0xFF18281C) else Color(0xFF55E6B7)
                } else {
                  Color(0xFF7A5C2B)
                },
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = "In ${primaryDisplay.remainingTimeFormatted} remaining",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 11.5.sp
                ),
                color = if (isDark) {
                  if (isCurrentActive) Color(0xFF18281C) else Color(0xFF55E6B7)
                } else {
                  Color(0xFF6E5122)
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // START & END TIMES ROW: "Started 01:00 PM - Ends 05:01 PM"
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Started ${primaryDisplay.timeFormatted}",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Medium,
              fontSize = 13.5.sp
            ),
            color = if (isDark) Color(0xFFE0F2EB) else Color(0xFF42524A)
          )

          Text(
            text = "-",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = if (isDark) Color(0x80FFFFFF) else Color(0xFF8A9A92)
          )

          Text(
            text = "Ends ${primaryDisplay.endFormatted}",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Medium,
              fontSize = 13.5.sp
            ),
            color = if (isDark) Color(0xFFE0F2EB) else Color(0xFF42524A)
          )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // SUBTLE DIVIDER
        HorizontalDivider(
          modifier = Modifier.fillMaxWidth(),
          color = if (isDark) Color(0x33FFFFFF) else Color(0xFFE2D6C2),
          thickness = 0.9.dp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // QUICK NOTIFICATION CONTROLS: "Alert:" & [ ☼ | ◷ | Silent | ⍈ ]
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
              tint = if (isDark) Color(0xFFD0EDE2) else Color(0xFF5A4420),
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "Alert",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
              ),
              color = if (isDark) Color(0xFFD0EDE2) else Color(0xFF333333)
            )
          }

          // Segmented Toggle Pills: White active pill with green highlight in light mode
          Row(
            modifier = Modifier
              .background(
                if (isDark) Color(0x26000000) else Color(0xFFE6DEC9),
                RoundedCornerShape(12.dp)
              )
              .padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
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
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) {
                  if (isDark) Color.White else Color(0xFF7FA894)
                } else {
                  Color.Transparent
                },
                shadowElevation = if (isSelected) 1.dp else 0.dp,
                modifier = Modifier
                  .clip(RoundedCornerShape(10.dp))
                  .clickable {
                    onNotificationModeChange(primaryDisplay.type, mode)
                  }
                  .testTag("quick_mode_${primaryDisplay.type.name.lowercase()}_${mode.name.lowercase()}")
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) {
                      if (isDark) Color(0xFF0F382A) else Color.White
                    } else {
                      if (isDark) Color(0xCCFFFFFF) else Color(0xFF635640)
                    },
                    modifier = Modifier.size(13.dp)
                  )
                  if (isSelected) {
                    Text(
                      text = label,
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                      ),
                      color = if (isDark) Color(0xFF0F382A) else Color.White
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
