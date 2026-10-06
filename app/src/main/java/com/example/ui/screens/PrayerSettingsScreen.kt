package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculation.HijriCalendarHelper
import com.example.calculation.PrayerCalculator
import com.example.model.*
import com.example.ui.components.getPrayerIcon
import java.util.Calendar

@Composable
fun PrayerSettingsScreen(
  schedule: PrayerCalculator.DailyPrayerSchedule,
  state: SakinahUiState,
  onNotificationChange: (PrayerType, PrayerNotificationMode) -> Unit,
  onAdjustMinutes: (PrayerType, Int) -> Unit,
  onOpenCalendar: () -> Unit,
  onOpenFiqhSettings: () -> Unit,
  onTogglePreReminder: () -> Unit,
  onBack: () -> Unit
) {
  val currentCalendar = Calendar.getInstance().apply {
    set(Calendar.YEAR, state.selectedCalendarDateYear)
    set(Calendar.MONTH, state.selectedCalendarDateMonth - 1)
    set(Calendar.DAY_OF_MONTH, state.selectedCalendarDateDay)
  }
  val dateFormatted = HijriCalendarHelper.formatGregorianDate(currentCalendar, state.selectedLanguage)

  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    topBar = {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("prayer_settings_back")
          ) {
            Text(
              text = "←",
              style = MaterialTheme.typography.headlineMedium,
              color = MaterialTheme.colorScheme.onBackground
            )
          }
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Prayer Times",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )
        }

        IconButton(
          onClick = onOpenCalendar,
          modifier = Modifier.testTag("prayer_settings_calendar_button")
        ) {
          Icon(
            imageVector = Icons.Outlined.CalendarMonth,
            contentDescription = "Select Date",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
          )
        }
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(horizontal = 20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Date Selector Header Card
      item {
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onOpenCalendar)
            .testTag("prayer_settings_date_card")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "SELECTED DATE",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = dateFormatted,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            Text(
              text = "Change",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
              )
            )
          }
        }
      }

      // Prayers Notifications List
      item {
        Text(
          text = "NOTIFICATIONS PER PRAYER",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
          ),
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      val prayerItems = listOf(
        schedule.fajr,
        schedule.sunrise,
        schedule.dhuhr,
        schedule.asr,
        schedule.maghrib,
        schedule.isha
      )

      items(prayerItems.size) { index ->
        val prayer = prayerItems[index]
        val currentMode = state.notificationModes[prayer.type] ?: PrayerNotificationMode.SOUND
        val adjustment = state.manualAdjustments[prayer.type] ?: 0

        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.surface,
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            // Row Header: Icon + Name + Time
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(34.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = getPrayerIcon(prayer.type),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                  )
                }

                Column {
                  Text(
                    text = prayer.type.englishName,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = prayer.type.arabicName,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              Text(
                text = prayer.timeFormatted,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Compact Segmented Notification Selector
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                .padding(3.dp),
              horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
              val modes = listOf(
                PrayerNotificationMode.SOUND to Icons.Outlined.NotificationsActive,
                PrayerNotificationMode.VIBRATE to Icons.Outlined.Vibration,
                PrayerNotificationMode.SILENT to Icons.Outlined.Notifications,
                PrayerNotificationMode.OFF to Icons.Outlined.NotificationsOff
              )

              modes.forEach { (mode, icon) ->
                val isSelected = currentMode == mode
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = if (isSelected) MaterialTheme.colorScheme.surface else androidx.compose.ui.graphics.Color.Transparent,
                  shadowElevation = if (isSelected) 1.dp else 0.dp,
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onNotificationChange(prayer.type, mode) }
                    .testTag("mode_${prayer.type.name.lowercase()}_${mode.name.lowercase()}")
                ) {
                  Box(
                    modifier = Modifier.padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = icon,
                      contentDescription = mode.label,
                      tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Minute Offset Adjustment Row
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Manual Adjustment: ${if (adjustment >= 0) "+$adjustment" else "$adjustment"} min",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(
                  onClick = { onAdjustMinutes(prayer.type, -1) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Text("-", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                IconButton(
                  onClick = { onAdjustMinutes(prayer.type, 1) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Text("+", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
              }
            }
          }
        }
      }

      // Pre-prayer reminder toggle
      item {
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.surface,
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Pre-Prayer Gentle Alert",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Notify 15 minutes before prayer to prepare for wudu",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Switch(
              checked = state.isPrePrayerReminderEnabled,
              onCheckedChange = { onTogglePreReminder() }
            )
          }
        }
      }

      // Method info footer link
      item {
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onOpenFiqhSettings)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Madhab: ${state.selectedMadhab.displayName}",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Method: ${state.calculationMethod.shortName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Text(
              text = "Edit →",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
            )
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
