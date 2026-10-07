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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculation.HijriCalendarHelper
import com.example.calculation.PrayerCalculator
import com.example.model.*
import com.example.ui.components.CurrentPrayerCard
import com.example.ui.components.PrayerRowItem
import java.util.Calendar

@Composable
fun HomeScreen(
  schedule: PrayerCalculator.DailyPrayerSchedule,
  state: SakinahUiState,
  onCityClick: () -> Unit,
  onCalendarClick: () -> Unit,
  onPrayerSettingsClick: () -> Unit,
  onAppSettingsClick: () -> Unit,
  onPremiumClick: () -> Unit,
  onPrayerClick: (PrayerTimeItem) -> Unit,
  onNotificationToggle: (PrayerType) -> Unit,
  onNavigateToAllahNames: () -> Unit,
  onNavigateToTasbeeh: () -> Unit,
  onNavigateToQibla: () -> Unit,
  onNavigateToDuas: () -> Unit,
  onNavigateToLiveMakkah: () -> Unit,
  onNavigateToLiveMadinah: () -> Unit,
  modifier: Modifier = Modifier,
  onNotificationModeChange: (PrayerType, PrayerNotificationMode) -> Unit = { _, _ -> }
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
      // LEVEL 1: TOP BAR WITH ISLAMIC DATE, CITY, PREMIUM & SETTINGS
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(horizontal = 18.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Left: Date & City
        Column {
          Text(
            text = hijriDateFormatted,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            ),
            color = MaterialTheme.colorScheme.primary
          )

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .clickable(onClick = onCityClick)
              .padding(vertical = 2.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.LocationOn,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = state.selectedCity.displayName,
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
              imageVector = Icons.Outlined.KeyboardArrowDown,
              contentDescription = "Change Location",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(16.dp)
            )
          }

          Text(
            text = gregorianDateFormatted,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        // Right Actions: Prayer Times, Calendar, Premium Gift & Settings
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          IconButton(
            onClick = onCalendarClick,
            modifier = Modifier.size(38.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.CalendarMonth,
              contentDescription = "Prayer Calendar",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp)
            )
          }

          IconButton(
            onClick = onPrayerSettingsClick,
            modifier = Modifier.size(38.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.Tune,
              contentDescription = "Prayer Settings",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp)
            )
          }

          IconButton(
            onClick = onPremiumClick,
            modifier = Modifier.size(38.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.CardGiftcard,
              contentDescription = "Sakinah Plus Premium",
              tint = MaterialTheme.colorScheme.secondary,
              modifier = Modifier.size(20.dp)
            )
          }

          IconButton(
            onClick = onAppSettingsClick,
            modifier = Modifier.size(38.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.Settings,
              contentDescription = "Settings",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(padding)
        .testTag("home_screen_content"),
      contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. ONE PREMIUM CURRENT PRAYER CARD (With Quick Notification Controls & Schedule Nav)
      item {
        val primaryPrayer = schedule.currentPrayer ?: schedule.nextPrayer
        val currentNotificationMode = state.notificationModes[primaryPrayer.type] ?: PrayerNotificationMode.SOUND

        CurrentPrayerCard(
          activePrayer = schedule.currentPrayer,
          nextPrayer = schedule.nextPrayer,
          language = state.selectedLanguage,
          sunriseItem = schedule.sunrise,
          currentNotificationMode = currentNotificationMode,
          onNotificationModeChange = onNotificationModeChange,
          onOpenPrayerTimes = onPrayerSettingsClick
        )
      }

      // 2. DAILY WORSHIP UTILITIES (Compact 2-Column Grid)
      item {
        Text(
          text = "DAILY WORSHIP UTILITIES",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
          ),
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
      }

      // Row 1: Allah's Names & Tasbeeh
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          FeatureDashboardCard(
            title = "Allah's Names",
            subtitle = "99 Names • Asma ul-Husna",
            arabicTag = "أسماء الله",
            icon = Icons.Outlined.AutoStories,
            accentColors = listOf(androidx.compose.ui.graphics.Color(0xFF1B3B2B), androidx.compose.ui.graphics.Color(0xFF2E6347)),
            iconTint = androidx.compose.ui.graphics.Color(0xFF6EE7B7),
            onClick = onNavigateToAllahNames,
            modifier = Modifier.weight(1f)
          )

          FeatureDashboardCard(
            title = "Tasbeeh",
            subtitle = "Dhikr & Counter (${state.tasbeehCount}/${state.tasbeehTotalTarget})",
            arabicTag = "المسبحة",
            icon = Icons.Outlined.Fingerprint,
            accentColors = listOf(androidx.compose.ui.graphics.Color(0xFF1F3847), androidx.compose.ui.graphics.Color(0xFF2C556D)),
            iconTint = androidx.compose.ui.graphics.Color(0xFF7DD3FC),
            onClick = onNavigateToTasbeeh,
            modifier = Modifier.weight(1f)
          )
        }
      }

      // Row 2: Qibla & Duas
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          FeatureDashboardCard(
            title = "Qibla Direction",
            subtitle = "Compass to Kaaba (${state.qiblaBearingDegrees.toInt()}°)",
            arabicTag = "اتجاه القبلة",
            icon = Icons.Outlined.Explore,
            accentColors = listOf(androidx.compose.ui.graphics.Color(0xFF382F1E), androidx.compose.ui.graphics.Color(0xFF614E29)),
            iconTint = androidx.compose.ui.graphics.Color(0xFFFDE047),
            onClick = onNavigateToQibla,
            modifier = Modifier.weight(1f)
          )

          FeatureDashboardCard(
            title = "Duas",
            subtitle = "Daily Supplications",
            arabicTag = "الأدعية",
            icon = Icons.Outlined.VolunteerActivism,
            accentColors = listOf(androidx.compose.ui.graphics.Color(0xFF32233D), androidx.compose.ui.graphics.Color(0xFF533866)),
            iconTint = androidx.compose.ui.graphics.Color(0xFFD8B4FE),
            onClick = onNavigateToDuas,
            modifier = Modifier.weight(1f)
          )
        }
      }

      // 5. LEVEL 4: LIVE MAKKAH & LIVE MADINAH
      item {
        Text(
          text = "HOLY SITES LIVE BROADCAST",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
          ),
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
      }

      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          FeatureDashboardCard(
            title = "Live Makkah",
            subtitle = "24/7 Haram Broadcast",
            arabicTag = "بث مباشر مكة",
            icon = Icons.Outlined.LiveTv,
            badge = "LIVE",
            accentColors = listOf(androidx.compose.ui.graphics.Color(0xFF2D1E1E), androidx.compose.ui.graphics.Color(0xFF563333)),
            iconTint = androidx.compose.ui.graphics.Color(0xFFFCA5A5),
            onClick = onNavigateToLiveMakkah,
            modifier = Modifier.weight(1f)
          )

          FeatureDashboardCard(
            title = "Live Madinah",
            subtitle = "24/7 Nabawi Broadcast",
            arabicTag = "بث مباشر المدينة",
            icon = Icons.Outlined.LiveTv,
            badge = "LIVE",
            accentColors = listOf(androidx.compose.ui.graphics.Color(0xFF1E332E), androidx.compose.ui.graphics.Color(0xFF315C50)),
            iconTint = androidx.compose.ui.graphics.Color(0xFF5EEAD4),
            onClick = onNavigateToLiveMadinah,
            modifier = Modifier.weight(1f)
          )
        }
      }

      // 6. AdMob Banner Ad (Free Tier - Native Kotlin AdView via Compose)
      if (!state.isPremiumUnlocked) {
        item {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.padding(vertical = 4.dp)
            ) {
              com.example.ads.AdBannerView()
            }
          }
        }
      }

      // Bottom padding for audio mini-player
      item {
        Spacer(modifier = Modifier.height(84.dp))
      }
    }
  }
}

@Composable
private fun FeatureDashboardCard(
  title: String,
  subtitle: String,
  arabicTag: String,
  icon: ImageVector,
  accentColors: List<androidx.compose.ui.graphics.Color>,
  iconTint: androidx.compose.ui.graphics.Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  badge: String? = null
) {
  val isDark = androidx.compose.foundation.isSystemInDarkTheme()
  val gradient = androidx.compose.ui.graphics.Brush.linearGradient(
    colors = if (isDark) accentColors else listOf(
      MaterialTheme.colorScheme.surface,
      accentColors.first().copy(alpha = 0.12f)
    )
  )

  Surface(
    shape = RoundedCornerShape(18.dp),
    color = androidx.compose.ui.graphics.Color.Transparent,
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (isDark) accentColors.last().copy(alpha = 0.6f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
    ),
    shadowElevation = 2.dp,
    modifier = modifier
      .clip(RoundedCornerShape(18.dp))
      .clickable(onClick = onClick)
  ) {
    Box(
      modifier = Modifier
        .background(gradient)
        .padding(14.dp)
    ) {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(if (isDark) androidx.compose.ui.graphics.Color(0x33000000) else accentColors.first().copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = if (isDark) iconTint else MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
          }

          if (badge != null) {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = MaterialTheme.colorScheme.error
            ) {
              Text(
                text = badge,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 9.sp
                ),
                color = MaterialTheme.colorScheme.onError,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          } else {
            Text(
              text = arabicTag,
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
              ),
              color = if (isDark) iconTint.copy(alpha = 0.85f) else MaterialTheme.colorScheme.primary
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = title,
          style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          ),
          color = if (isDark) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
          color = if (isDark) androidx.compose.ui.graphics.Color(0xCCFFFFFF) else MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1
        )
      }
    }
  }
}
