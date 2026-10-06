package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.PrayerNotificationMode
import com.example.model.PrayerTimeItem
import com.example.model.PrayerType
import com.example.ui.theme.LocalSakinahColors

@Composable
fun CurrentPrayerCard(
  activePrayer: PrayerTimeItem?,
  nextPrayer: PrayerTimeItem,
  language: AppLanguage,
  sunriseItem: PrayerTimeItem,
  currentNotificationMode: PrayerNotificationMode,
  onNotificationModeChange: (PrayerType, PrayerNotificationMode) -> Unit,
  onOpenPrayerTimes: () -> Unit,
  onPrayerClick: (PrayerTimeItem) -> Unit,
  modifier: Modifier = Modifier
) {
  val sakinahColors = LocalSakinahColors.current
  val isCurrentActive = activePrayer != null && activePrayer.type != PrayerType.SUNRISE
  val primaryDisplay = if (isCurrentActive) activePrayer!! else nextPrayer

  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isCurrentActive) {
        sakinahColors.activePrayerBackground
      } else {
        MaterialTheme.colorScheme.surface
      }
    ),
    border = if (isCurrentActive) {
      androidx.compose.foundation.BorderStroke(1.5.dp, sakinahColors.activePrayerBorder)
    } else {
      androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f))
    },
    elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrentActive) 2.dp else 0.dp),
    modifier = modifier
      .fillMaxWidth()
      .testTag("current_prayer_card")
      .clip(RoundedCornerShape(22.dp))
  ) {
    Box(modifier = Modifier.fillMaxWidth()) {
      // Left vertical accent decorative bar
      Box(
        modifier = Modifier
          .width(5.dp)
          .matchParentSize()
          .align(Alignment.CenterStart)
          .background(
            if (isCurrentActive) sakinahColors.activePrayerAccent else MaterialTheme.colorScheme.secondary,
            RoundedCornerShape(topStart = 22.dp, bottomStart = 22.dp)
          )
      )

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 20.dp, end = 18.dp, top = 16.dp, bottom = 16.dp)
      ) {
        // TOP HEADER ROW: Status Label on Left + Prayer Times Navigation on Right
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Status Indicator Pill (e.g. "CURRENT PRAYER" or "NEXT PRAYER")
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(9.dp)
                .background(
                  if (isCurrentActive) sakinahColors.activePrayerAccent else MaterialTheme.colorScheme.secondary,
                  CircleShape
                )
            )
            Text(
              text = if (isCurrentActive) {
                "CURRENT PRAYER"
              } else {
                "NEXT PRAYER"
              },
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.1.sp,
                fontSize = 11.sp
              ),
              color = if (isCurrentActive) sakinahColors.activePrayerAccent else MaterialTheme.colorScheme.secondary
            )
          }

          // COMPACT ACTION AREA: Open Dedicated Prayer Schedule / Settings Screen
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .clickable(onClick = onOpenPrayerTimes)
              .testTag("prayer_schedule_action_button")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp),
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(13.dp)
              )
              Text(
                text = "Prayer Times",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 11.sp
                ),
                color = MaterialTheme.colorScheme.primary
              )
              Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                contentDescription = "Open Full Prayer Schedule",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(13.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // PRAYER NAME AND ARABIC SCRIPT ROW
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onPrayerClick(primaryDisplay) },
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = primaryDisplay.type.localizedName(language).uppercase(),
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                letterSpacing = 0.5.sp
              ),
              color = MaterialTheme.colorScheme.onSurface
            )

            Text(
              text = if (isCurrentActive) {
                "Current Prayer • Ends in ${primaryDisplay.remainingTimeFormatted}"
              } else {
                "Starts in ${primaryDisplay.remainingTimeFormatted} • Next Prayer"
              },
              style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              ),
              color = if (isCurrentActive) {
                sakinahColors.activePrayerAccent
              } else {
                MaterialTheme.colorScheme.onSurfaceVariant
              }
            )
          }

          // Elegant Arabic Calligraphy Name
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
            modifier = Modifier.padding(start = 8.dp)
          ) {
            Text(
              text = primaryDisplay.type.arabicName,
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
              ),
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // MAIN TIME & COUNTDOWN ROW
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onPrayerClick(primaryDisplay) },
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Bottom
        ) {
          Column {
            Text(
              text = primaryDisplay.timeFormatted,
              style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 38.sp,
                letterSpacing = (-0.5).sp
              ),
              color = MaterialTheme.colorScheme.onSurface
            )

            Text(
              text = "Window: ${primaryDisplay.timeFormatted} – ${primaryDisplay.endFormatted}",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          // Prominent Countdown Badge
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isCurrentActive) {
              sakinahColors.activePrayerAccent.copy(alpha = 0.14f)
            } else {
              MaterialTheme.colorScheme.secondaryContainer
            },
            border = if (isCurrentActive) {
              androidx.compose.foundation.BorderStroke(1.dp, sakinahColors.activePrayerAccent.copy(alpha = 0.3f))
            } else {
              null
            },
            modifier = Modifier.padding(bottom = 4.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = null,
                tint = if (isCurrentActive) sakinahColors.activePrayerAccent else MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(15.dp)
              )
              Text(
                text = if (isCurrentActive) {
                  "Ends in ${primaryDisplay.remainingTimeFormatted}"
                } else {
                  "Starts in ${primaryDisplay.remainingTimeFormatted}"
                },
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                ),
                color = if (isCurrentActive) sakinahColors.activePrayerAccent else MaterialTheme.colorScheme.onSecondaryContainer
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        HorizontalDivider(
          modifier = Modifier.fillMaxWidth(),
          color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
          thickness = 0.8.dp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // PRAYER NOTIFICATION QUICK CONTROLS: Sound, Vibrate, Silent, Off
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
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(15.dp)
            )
            Text(
              text = "Alert:",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 11.5.sp
              ),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          // Compact Segmented Controls
          Row(
            modifier = Modifier
              .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(10.dp)
              )
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
                color = if (isSelected) {
                  MaterialTheme.colorScheme.surface
                } else {
                  Color.Transparent
                },
                shadowElevation = if (isSelected) 1.dp else 0.dp,
                border = if (isSelected) {
                  androidx.compose.foundation.BorderStroke(
                    0.8.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                  )
                } else null,
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .clickable {
                    onNotificationModeChange(primaryDisplay.type, mode)
                  }
                  .testTag("quick_mode_${primaryDisplay.type.name.lowercase()}_${mode.name.lowercase()}")
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                  Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) {
                      MaterialTheme.colorScheme.primary
                    } else {
                      MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(13.dp)
                  )
                  if (isSelected) {
                    Text(
                      text = label,
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                      ),
                      color = MaterialTheme.colorScheme.primary
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
