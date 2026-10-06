package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CityLocation

@Composable
fun SakinahTopBar(
  city: CityLocation,
  onCityClick: () -> Unit,
  onCalendarClick: () -> Unit,
  onTasbeehClick: () -> Unit,
  onPrayerSettingsClick: () -> Unit,
  onAppSettingsClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    // Left: Tappable Location
    Row(
      modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = ripple(),
          onClick = onCityClick
        )
        .testTag("location_button")
        .padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Outlined.LocationOn,
        contentDescription = "Current location",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = city.name,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.SemiBold,
              fontSize = 17.sp
            ),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = "Change location",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
          )
        }
        Text(
          text = if (city.isGpsDetected) "${city.country} • GPS" else city.country,
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1
        )
      }
    }

    // Right: Action Icons (Tasbeeh, Calendar, Prayer Settings, App Settings)
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
      IconButton(
        onClick = onCalendarClick,
        modifier = Modifier
          .size(40.dp)
          .testTag("calendar_button")
      ) {
        Icon(
          imageVector = Icons.Outlined.CalendarMonth,
          contentDescription = "Prayer Calendar",
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(21.dp)
        )
      }

      IconButton(
        onClick = onTasbeehClick,
        modifier = Modifier
          .size(40.dp)
          .testTag("tasbeeh_button")
      ) {
        Icon(
          imageVector = Icons.Outlined.Fingerprint,
          contentDescription = "Tasbeeh Counter",
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(22.dp)
        )
      }

      IconButton(
        onClick = onPrayerSettingsClick,
        modifier = Modifier
          .size(40.dp)
          .testTag("prayer_settings_button")
      ) {
        Icon(
          imageVector = Icons.Outlined.Tune,
          contentDescription = "Prayer Settings",
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(21.dp)
        )
      }

      IconButton(
        onClick = onAppSettingsClick,
        modifier = Modifier
          .size(40.dp)
          .testTag("app_settings_button")
      ) {
        Icon(
          imageVector = Icons.Outlined.Settings,
          contentDescription = "App Settings",
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(21.dp)
        )
      }
    }
  }
}
