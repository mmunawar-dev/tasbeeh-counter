package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material.icons.outlined.VolumeOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.LocalSakinahColors

@Composable
fun PrayerRowItem(
  item: PrayerTimeItem,
  language: AppLanguage,
  onRowClick: () -> Unit,
  onNotificationToggle: () -> Unit,
  modifier: Modifier = Modifier
) {
  val sakinahColors = LocalSakinahColors.current
  val isCurrent = item.status == PrayerStatus.CURRENT
  val isPast = item.status == PrayerStatus.PAST

  val rowBackground = when {
    isCurrent -> sakinahColors.activePrayerBackground
    else -> MaterialTheme.colorScheme.surface
  }

  val textColor = when {
    isCurrent -> MaterialTheme.colorScheme.onSurface
    isPast -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
    else -> MaterialTheme.colorScheme.onSurface
  }

  val timeColor = when {
    isCurrent -> sakinahColors.activePrayerAccent
    isPast -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.70f)
    else -> MaterialTheme.colorScheme.onSurface
  }

  Surface(
    shape = RoundedCornerShape(14.dp),
    color = rowBackground,
    border = if (isCurrent) {
      androidx.compose.foundation.BorderStroke(1.5.dp, sakinahColors.activePrayerBorder)
    } else {
      androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    },
    modifier = modifier
      .fillMaxWidth()
      .testTag("prayer_row_${item.type.name.lowercase()}")
      .clip(RoundedCornerShape(14.dp))
      .clickable(onClick = onRowClick)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 13.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Left: Icon + Name + Active indicator
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Icon container
        Box(
          modifier = Modifier
            .size(38.dp)
            .background(
              if (isCurrent) sakinahColors.activePrayerAccent.copy(alpha = 0.15f)
              else MaterialTheme.colorScheme.surfaceVariant,
              RoundedCornerShape(10.dp)
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = getPrayerIcon(item.type),
            contentDescription = null,
            tint = if (isCurrent) sakinahColors.activePrayerAccent else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
        }

        // Names
        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = item.type.localizedName(language),
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                fontSize = 16.sp
              ),
              color = textColor
            )

            if (isCurrent) {
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = sakinahColors.activePrayerAccent,
                modifier = Modifier.padding(top = 1.dp)
              ) {
                Text(
                  text = "NOW",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp
                  ),
                  color = MaterialTheme.colorScheme.onPrimary,
                  modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
              }
            }
          }

          Text(
            text = item.type.arabicName,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (isPast) 0.6f else 0.85f)
          )
        }
      }

      // Right: Time + Notification Mode Icon
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Text(
          text = item.timeFormatted,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
            fontSize = 17.sp,
            letterSpacing = 0.2.sp
          ),
          color = timeColor
        )

        // Notification toggle icon
        IconButton(
          onClick = onNotificationToggle,
          modifier = Modifier
            .size(36.dp)
            .testTag("notify_toggle_${item.type.name.lowercase()}")
        ) {
          val icon = when (item.notificationMode) {
            PrayerNotificationMode.SOUND -> Icons.Outlined.NotificationsActive
            PrayerNotificationMode.VIBRATE -> Icons.Outlined.Vibration
            PrayerNotificationMode.SILENT -> Icons.Outlined.Notifications
            PrayerNotificationMode.OFF -> Icons.Outlined.NotificationsOff
          }
          val tint = when (item.notificationMode) {
            PrayerNotificationMode.SOUND -> MaterialTheme.colorScheme.primary
            PrayerNotificationMode.VIBRATE -> MaterialTheme.colorScheme.secondary
            PrayerNotificationMode.SILENT -> MaterialTheme.colorScheme.onSurfaceVariant
            PrayerNotificationMode.OFF -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
          }

          Icon(
            imageVector = icon,
            contentDescription = "Notification mode: ${item.notificationMode.label}",
            tint = tint,
            modifier = Modifier.size(19.dp)
          )
        }
      }
    }
  }
}
