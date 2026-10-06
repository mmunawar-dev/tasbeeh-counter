package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.components.CurrentPrayerCard
import com.example.ui.components.PrayerRowItem
import com.example.ui.components.SakinahTopBar
import java.util.Calendar

@Composable
fun HomeScreen(
  schedule: PrayerCalculator.DailyPrayerSchedule,
  state: SakinahUiState,
  onCityClick: () -> Unit,
  onCalendarClick: () -> Unit,
  onTasbeehClick: () -> Unit,
  onPrayerSettingsClick: () -> Unit,
  onAppSettingsClick: () -> Unit,
  onPrayerClick: (PrayerTimeItem) -> Unit,
  onNotificationToggle: (PrayerType) -> Unit,
  modifier: Modifier = Modifier
) {
  val currentCalendar = Calendar.getInstance().apply {
    set(Calendar.YEAR, state.selectedCalendarDateYear)
    set(Calendar.MONTH, state.selectedCalendarDateMonth - 1)
    set(Calendar.DAY_OF_MONTH, state.selectedCalendarDateDay)
  }

  val gregorianDateFormatted = HijriCalendarHelper.formatGregorianDate(currentCalendar, state.selectedLanguage)
  val hijriDate = HijriCalendarHelper.getHijriDate(currentCalendar)
  val hijriDateFormatted = hijriDate.formatted(state.selectedLanguage)

  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    topBar = {
      SakinahTopBar(
        city = state.selectedCity,
        onCityClick = onCityClick,
        onCalendarClick = onCalendarClick,
        onTasbeehClick = onTasbeehClick,
        onPrayerSettingsClick = onPrayerSettingsClick,
        onAppSettingsClick = onAppSettingsClick
      )
    }
  ) { padding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(padding)
        .testTag("home_screen_content"),
      contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Date Header (Gregorian + Hijri)
      item {
        Column(modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)) {
          Text(
            text = gregorianDateFormatted,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.SemiBold,
              fontSize = 17.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = hijriDateFormatted,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // 2. Current Prayer / Next Prayer Hero Section
      item {
        CurrentPrayerCard(
          activePrayer = schedule.currentPrayer,
          nextPrayer = schedule.nextPrayer,
          language = state.selectedLanguage,
          sunriseItem = schedule.sunrise,
          onPrayerClick = onPrayerClick
        )
      }

      // 3. Sunrise Secondary Bar
      item {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onPrayerClick(schedule.sunrise) }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.WbTwilight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
              )
              Text(
                text = "Sunrise",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "• ${schedule.sunrise.type.arabicName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Text(
              text = schedule.sunrise.timeFormatted,
              style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // 4. Five Daily Obligatory Prayers
      item {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          PrayerRowItem(
            item = schedule.fajr,
            language = state.selectedLanguage,
            onRowClick = { onPrayerClick(schedule.fajr) },
            onNotificationToggle = { onNotificationToggle(PrayerType.FAJR) }
          )

          PrayerRowItem(
            item = schedule.dhuhr,
            language = state.selectedLanguage,
            onRowClick = { onPrayerClick(schedule.dhuhr) },
            onNotificationToggle = { onNotificationToggle(PrayerType.DHUHR) }
          )

          PrayerRowItem(
            item = schedule.asr,
            language = state.selectedLanguage,
            onRowClick = { onPrayerClick(schedule.asr) },
            onNotificationToggle = { onNotificationToggle(PrayerType.ASR) }
          )

          PrayerRowItem(
            item = schedule.maghrib,
            language = state.selectedLanguage,
            onRowClick = { onPrayerClick(schedule.maghrib) },
            onNotificationToggle = { onNotificationToggle(PrayerType.MAGHRIB) }
          )

          PrayerRowItem(
            item = schedule.isha,
            language = state.selectedLanguage,
            onRowClick = { onPrayerClick(schedule.isha) },
            onNotificationToggle = { onNotificationToggle(PrayerType.ISHA) }
          )
        }
      }

      // 5. Daily Worship Companion (Tasbeeh & Dhikr Card)
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onTasbeehClick)
            .testTag("home_tasbeeh_card")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Outlined.Fingerprint,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSecondaryContainer,
                  modifier = Modifier.size(24.dp)
                )
              }

              Column {
                Text(
                  text = "Daily Tasbeeh Counter",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "${state.currentDhikr.transliteration} • ${state.tasbeehCount}/${state.tasbeehTotalTarget}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.primaryContainer
            ) {
              Text(
                text = "Count",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
          }
        }
      }

      // Bottom breathing room
      item {
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
