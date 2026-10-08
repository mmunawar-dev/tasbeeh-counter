package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
  val isDark = isSystemInDarkTheme()
  val currentCalendar = Calendar.getInstance().apply {
    set(Calendar.YEAR, state.selectedCalendarDateYear)
    set(Calendar.MONTH, state.selectedCalendarDateMonth - 1)
    set(Calendar.DAY_OF_MONTH, state.selectedCalendarDateDay)
  }

  val gregorianDateFormatted = HijriCalendarHelper.formatGregorianDate(currentCalendar, state.selectedLanguage)
  val hijriDate = HijriCalendarHelper.getHijriDate(currentCalendar)
  val hijriDateFormatted = hijriDate.formatted(state.selectedLanguage)

  Box(modifier = Modifier.fillMaxSize()) {
    // Subtle Islamic geometric watermark background in light theme
    if (!isDark) {
      Image(
        painter = painterResource(id = R.drawable.bg_islamic_geom_1791485891066),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alpha = 0.45f,
        modifier = Modifier.fillMaxSize()
      )
    }

    Scaffold(
      containerColor = if (isDark) MaterialTheme.colorScheme.background else Color.Transparent,
      topBar = {
        // TOP BAR WITH ISLAMIC DATE, CITY, AND TOP ACTIONS
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Left: Date & City
          Column {
            Text(
              text = hijriDateFormatted,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
              ),
              color = if (isDark) MaterialTheme.colorScheme.onBackground else Color(0xFF1B231F)
            )

            Spacer(modifier = Modifier.height(2.dp))

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
                tint = if (isDark) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFF5A6660),
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = state.selectedCity.displayName,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = if (isDark) MaterialTheme.colorScheme.onSurface else Color(0xFF2D3833)
              )
              Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = "Change Location",
                tint = if (isDark) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFF5A6660),
                modifier = Modifier.size(15.dp)
              )
            }

            Text(
              text = gregorianDateFormatted,
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
              color = if (isDark) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFF7A8780)
            )
          }

          // Right Actions: Calendar, Tune, Gift, Settings
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
          ) {
            IconButton(
              onClick = onCalendarClick,
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = "Prayer Calendar",
                tint = if (isDark) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFF3F4D46),
                modifier = Modifier.size(20.dp)
              )
            }

            IconButton(
              onClick = onPrayerSettingsClick,
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.Tune,
                contentDescription = "Prayer Settings",
                tint = if (isDark) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFF3F4D46),
                modifier = Modifier.size(20.dp)
              )
            }

            IconButton(
              onClick = onPremiumClick,
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.CardGiftcard,
                contentDescription = "Sakinah Plus Premium",
                tint = if (isDark) MaterialTheme.colorScheme.secondary else Color(0xFF9E783B),
                modifier = Modifier.size(20.dp)
              )
            }

            IconButton(
              onClick = onAppSettingsClick,
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Settings",
                tint = if (isDark) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFF3F4D46),
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
        // 1. HERO CURRENT PRAYER CARD
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
              letterSpacing = 0.8.sp,
              fontSize = 11.sp
            ),
            color = if (isDark) MaterialTheme.colorScheme.primary else Color(0xFF3F554A),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
          )
        }

        // Row 1: Allah's Names & Tasbeeh
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            FeatureDashboardCard(
              title = "Allah's Names",
              subtitle = "99 Names • Asma",
              arabicTag = "أسماء الله",
              icon = Icons.Outlined.AutoStories,
              lightBackground = Color(0xFFE8F1EC),
              lightBorder = Color(0xFFD4E4DC),
              lightIconTint = Color(0xFF28684E),
              lightIconBg = Color(0xFFD5E7DD),
              darkAccentColors = listOf(Color(0xFF1B3B2B), Color(0xFF2E6347)),
              darkIconTint = Color(0xFF6EE7B7),
              onClick = onNavigateToAllahNames,
              modifier = Modifier.weight(1f)
            )

            FeatureDashboardCard(
              title = "Tasbeeh",
              subtitle = "Dhikr & Counter (${state.tasbeehCount}/${state.tasbeehTotalTarget})",
              arabicTag = "المسبحة",
              icon = Icons.Outlined.Fingerprint,
              lightBackground = Color(0xFFEAF1F6),
              lightBorder = Color(0xFFD3E3EE),
              lightIconTint = Color(0xFF27668A),
              lightIconBg = Color(0xFFD3E4F0),
              darkAccentColors = listOf(Color(0xFF1F3847), Color(0xFF2C556D)),
              darkIconTint = Color(0xFF7DD3FC),
              onClick = onNavigateToTasbeeh,
              modifier = Modifier.weight(1f)
            )
          }
        }

        // Row 2: Qibla & Duas
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            FeatureDashboardCard(
              title = "Qibla Direction",
              subtitle = "Compass to Kaaba (${state.qiblaBearingDegrees.toInt()}°)",
              arabicTag = "اتجاه القبلة",
              icon = Icons.Outlined.Explore,
              lightBackground = Color(0xFFF7F3E7),
              lightBorder = Color(0xFFEAE0C7),
              lightIconTint = Color(0xFF8C7132),
              lightIconBg = Color(0xFFEDE3C8),
              darkAccentColors = listOf(Color(0xFF382F1E), Color(0xFF614E29)),
              darkIconTint = Color(0xFFFDE047),
              onClick = onNavigateToQibla,
              modifier = Modifier.weight(1f)
            )

            FeatureDashboardCard(
              title = "Duas",
              subtitle = "Daily Supplications",
              arabicTag = "الأدعية",
              icon = Icons.Outlined.VolunteerActivism,
              lightBackground = Color(0xFFF3EDF7),
              lightBorder = Color(0xFFE3D6EA),
              lightIconTint = Color(0xFF6D4C82),
              lightIconBg = Color(0xFFE4D6ED),
              darkAccentColors = listOf(Color(0xFF32233D), Color(0xFF533866)),
              darkIconTint = Color(0xFFD8B4FE),
              onClick = onNavigateToDuas,
              modifier = Modifier.weight(1f)
            )
          }
        }

        // 3. HOLY SITES LIVE BROADCAST
        item {
          Text(
            text = "HOLY SITES LIVE BROADCAST",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.8.sp,
              fontSize = 11.sp
            ),
            color = if (isDark) MaterialTheme.colorScheme.primary else Color(0xFF3F554A),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
          )
        }

        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            FeatureDashboardCard(
              title = "Live Makkah",
              subtitle = "24/7 Haram Broadcast",
              arabicTag = "بث مباشر مكة",
              icon = Icons.Outlined.LiveTv,
              badge = "LIVE",
              lightBackground = Color(0xFFF8EDE6),
              lightBorder = Color(0xFFEED7C9),
              lightIconTint = Color(0xFF8B3A3A),
              lightIconBg = Color(0xFFEED5C7),
              darkAccentColors = listOf(Color(0xFF2D1E1E), Color(0xFF563333)),
              darkIconTint = Color(0xFFFCA5A5),
              onClick = onNavigateToLiveMakkah,
              modifier = Modifier.weight(1f)
            )

            FeatureDashboardCard(
              title = "Live Madinah",
              subtitle = "24/7 Nabawi Broadcast",
              arabicTag = "بث مباشر المدينة",
              icon = Icons.Outlined.LiveTv,
              badge = "LIVE",
              lightBackground = Color(0xFFEBF2EB),
              lightBorder = Color(0xFFD5E3D5),
              lightIconTint = Color(0xFF31684B),
              lightIconBg = Color(0xFFD6E6D8),
              darkAccentColors = listOf(Color(0xFF1E332E), Color(0xFF315C50)),
              darkIconTint = Color(0xFF5EEAD4),
              onClick = onNavigateToLiveMadinah,
              modifier = Modifier.weight(1f)
            )
          }
        }

        // 4. AdMob Banner Ad (Free Tier)
        if (!state.isPremiumUnlocked) {
          item {
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isDark) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f) else Color(0xFFF2F4F2),
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
}

@Composable
private fun FeatureDashboardCard(
  title: String,
  subtitle: String,
  arabicTag: String,
  icon: ImageVector,
  lightBackground: Color,
  lightBorder: Color,
  lightIconTint: Color,
  lightIconBg: Color,
  darkAccentColors: List<Color>,
  darkIconTint: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  badge: String? = null
) {
  val isDark = isSystemInDarkTheme()

  val cardBg = if (isDark) Color.Transparent else lightBackground
  val cardBorder = if (isDark) darkAccentColors.last().copy(alpha = 0.6f) else lightBorder

  Surface(
    shape = RoundedCornerShape(22.dp),
    color = cardBg,
    border = androidx.compose.foundation.BorderStroke(1.2.dp, cardBorder),
    shadowElevation = if (isDark) 2.dp else 1.5.dp,
    modifier = modifier
      .clip(RoundedCornerShape(22.dp))
      .clickable(onClick = onClick)
  ) {
    Box(
      modifier = Modifier
        .then(
          if (isDark) {
            Modifier.background(
              androidx.compose.ui.graphics.Brush.linearGradient(darkAccentColors)
            )
          } else {
            Modifier
          }
        )
        .padding(horizontal = 14.dp, vertical = 14.dp)
    ) {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Icon Container Pill
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(if (isDark) Color(0x33000000) else lightIconBg),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = if (isDark) darkIconTint else lightIconTint,
              modifier = Modifier.size(20.dp)
            )
          }

          if (badge != null) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFC0392B)
            ) {
              Text(
                text = badge,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 9.sp
                ),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
              )
            }
          } else {
            Text(
              text = arabicTag,
              style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
              ),
              color = if (isDark) darkIconTint.copy(alpha = 0.85f) else lightIconTint
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = title,
          style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          ),
          color = if (isDark) Color.White else Color(0xFF1B231F)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Normal
          ),
          color = if (isDark) Color(0xCCFFFFFF) else Color(0xFF5A6660),
          maxLines = 1
        )
      }
    }
  }
}
