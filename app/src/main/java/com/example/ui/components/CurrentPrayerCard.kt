package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CheckCircle
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
import com.example.model.PrayerTimeItem
import com.example.model.PrayerType
import com.example.ui.theme.LocalSakinahColors

@Composable
fun CurrentPrayerCard(
  activePrayer: PrayerTimeItem?,
  nextPrayer: PrayerTimeItem,
  language: AppLanguage,
  sunriseItem: PrayerTimeItem,
  onPrayerClick: (PrayerTimeItem) -> Unit,
  modifier: Modifier = Modifier
) {
  val sakinahColors = LocalSakinahColors.current
  val isCurrentActive = activePrayer != null && activePrayer.type != PrayerType.SUNRISE
  val primaryDisplay = if (isCurrentActive) activePrayer!! else nextPrayer

  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isCurrentActive) sakinahColors.activePrayerBackground else MaterialTheme.colorScheme.surface
    ),
    border = if (isCurrentActive) {
      androidx.compose.foundation.BorderStroke(1.5.dp, sakinahColors.activePrayerBorder)
    } else {
      androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    },
    modifier = modifier
      .fillMaxWidth()
      .testTag("current_prayer_card")
      .clip(RoundedCornerShape(20.dp))
      .clickable { onPrayerClick(primaryDisplay) }
  ) {
    Box(modifier = Modifier.fillMaxWidth()) {
      // Left vertical accent bar
      Box(
        modifier = Modifier
          .width(4.dp)
          .fillMaxHeight()
          .align(Alignment.CenterStart)
          .background(
            if (isCurrentActive) sakinahColors.activePrayerAccent else MaterialTheme.colorScheme.secondary,
            RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
          )
      )

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 20.dp, end = 18.dp, top = 16.dp, bottom = 16.dp)
      ) {
        // Status Row (e.g., "ASR • CURRENT PRAYER" or "UPCOMING • NEXT PRAYER")
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .background(
                  if (isCurrentActive) sakinahColors.activePrayerAccent else MaterialTheme.colorScheme.secondary,
                  CircleShape
                )
            )
            Text(
              text = if (isCurrentActive) {
                "${primaryDisplay.type.localizedName(language).uppercase()} • CURRENT PRAYER"
              } else {
                "NEXT PRAYER • ${primaryDisplay.type.localizedName(language).uppercase()}"
              },
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
              ),
              color = if (isCurrentActive) sakinahColors.activePrayerAccent else MaterialTheme.colorScheme.secondary
            )
          }

          // Arabic Name in elegant script
          Text(
            text = primaryDisplay.type.arabicName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Main Time & Remaining Countdown Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Bottom
        ) {
          Column {
            Text(
              text = primaryDisplay.timeFormatted,
              style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 34.sp
              ),
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Starts ${primaryDisplay.timeFormatted} • Ends ${primaryDisplay.endFormatted}",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          // Countdown Badge
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isCurrentActive) {
              sakinahColors.activePrayerAccent.copy(alpha = 0.12f)
            } else {
              MaterialTheme.colorScheme.secondaryContainer
            },
            modifier = Modifier.padding(bottom = 4.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = null,
                tint = if (isCurrentActive) sakinahColors.activePrayerAccent else MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = primaryDisplay.remainingTimeFormatted,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 12.sp
                ),
                color = if (isCurrentActive) sakinahColors.activePrayerAccent else MaterialTheme.colorScheme.onSecondaryContainer
              )
            }
          }
        }
      }
    }
  }
}
