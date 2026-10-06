package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerDetailBottomSheet(
  prayerItem: PrayerTimeItem,
  madhab: Madhab,
  calculationMethod: CalculationMethod,
  language: AppLanguage,
  onDismiss: () -> Unit,
  onNotificationModeChange: (PrayerNotificationMode) -> Unit
) {
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    containerColor = MaterialTheme.colorScheme.surface,
    dragHandle = {
      Box(
        modifier = Modifier
          .padding(vertical = 12.dp)
          .width(40.dp)
          .height(4.dp)
          .background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(2.dp))
      )
    }
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 8.dp)
        .padding(bottom = 32.dp)
        .testTag("prayer_detail_sheet")
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = getPrayerIcon(prayerItem.type),
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onPrimaryContainer,
              modifier = Modifier.size(24.dp)
            )
          }

          Column {
            Text(
              text = prayerItem.type.localizedName(language),
              style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = prayerItem.type.arabicName,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        // Status Badge
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = when (prayerItem.status) {
            PrayerStatus.CURRENT -> MaterialTheme.colorScheme.primaryContainer
            PrayerStatus.PAST -> MaterialTheme.colorScheme.surfaceVariant
            PrayerStatus.UPCOMING -> MaterialTheme.colorScheme.secondaryContainer
          }
        ) {
          Text(
            text = when (prayerItem.status) {
              PrayerStatus.CURRENT -> "CURRENTLY ACTIVE"
              PrayerStatus.PAST -> "COMPLETED"
              PrayerStatus.UPCOMING -> "UPCOMING"
            },
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = when (prayerItem.status) {
              PrayerStatus.CURRENT -> MaterialTheme.colorScheme.onPrimaryContainer
              PrayerStatus.PAST -> MaterialTheme.colorScheme.onSurfaceVariant
              PrayerStatus.UPCOMING -> MaterialTheme.colorScheme.onSecondaryContainer
            },
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Timing Card (Starts & Ends)
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "STARTS",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = prayerItem.timeFormatted,
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Divider(
            modifier = Modifier
              .height(40.dp)
              .width(1.dp),
            color = MaterialTheme.colorScheme.outlineVariant
          )

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "ENDS",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = prayerItem.endFormatted,
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Notification Mode Selector (Sound, Vibrate, Silent, Off)
      Text(
        text = "Notification Mode",
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        PrayerNotificationMode.values().forEach { mode ->
          val isSelected = prayerItem.notificationMode == mode
          FilterChip(
            selected = isSelected,
            onClick = { onNotificationModeChange(mode) },
            label = {
              Text(
                text = mode.name.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
              )
            },
            leadingIcon = {
              val icon = when (mode) {
                PrayerNotificationMode.SOUND -> Icons.Outlined.NotificationsActive
                PrayerNotificationMode.VIBRATE -> Icons.Outlined.Vibration
                PrayerNotificationMode.SILENT -> Icons.Outlined.Notifications
                PrayerNotificationMode.OFF -> Icons.Outlined.NotificationsOff
              }
              Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
            },
            modifier = Modifier.weight(1f)
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Sunnah & Nawafil Recommendation
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Sunnah Guidance",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = prayerItem.sunnahInfo,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Context Info
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = "Madhab / Method",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = madhab.displayName,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "Calculation Source",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = calculationMethod.shortName,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }
  }
}
