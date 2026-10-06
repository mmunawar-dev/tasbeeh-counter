package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculation.HijriCalendarHelper
import com.example.calculation.PrayerCalculator
import com.example.model.SakinahUiState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarScreen(
  state: SakinahUiState,
  onDateSelected: (year: Int, month: Int, day: Int) -> Unit,
  onResetToday: () -> Unit,
  onBack: () -> Unit
) {
  var viewingYear by remember { mutableStateOf(state.selectedCalendarDateYear) }
  var viewingMonth by remember { mutableStateOf(state.selectedCalendarDateMonth) } // 1-12

  val cal = remember(viewingYear, viewingMonth) {
    Calendar.getInstance().apply {
      set(Calendar.YEAR, viewingYear)
      set(Calendar.MONTH, viewingMonth - 1)
      set(Calendar.DAY_OF_MONTH, 1)
    }
  }

  val monthName = remember(viewingYear, viewingMonth) {
    SimpleDateFormat("MMMM yyyy", Locale.US).format(cal.time)
  }

  val maxDaysInMonth = remember(viewingYear, viewingMonth) {
    cal.getActualMaximum(Calendar.DAY_OF_MONTH)
  }

  // Calculate prayer schedule for the currently selected date to preview
  val selectedDateSchedule = remember(
    state.selectedCalendarDateYear,
    state.selectedCalendarDateMonth,
    state.selectedCalendarDateDay,
    state.selectedCity,
    state.selectedMadhab,
    state.calculationMethod
  ) {
    PrayerCalculator.calculate(
      year = state.selectedCalendarDateYear,
      month = state.selectedCalendarDateMonth,
      day = state.selectedCalendarDateDay,
      city = state.selectedCity,
      madhab = state.selectedMadhab,
      method = state.calculationMethod
    )
  }

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
            modifier = Modifier.testTag("calendar_back_button")
          ) {
            Text(
              text = "←",
              style = MaterialTheme.typography.headlineMedium,
              color = MaterialTheme.colorScheme.onBackground
            )
          }
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Prayer Calendar",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )
        }

        TextButton(
          onClick = {
            onResetToday()
            val today = Calendar.getInstance()
            viewingYear = today.get(Calendar.YEAR)
            viewingMonth = today.get(Calendar.MONTH) + 1
          },
          modifier = Modifier.testTag("calendar_today_button")
        ) {
          Icon(
            imageVector = Icons.Outlined.Today,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text("Today", fontWeight = FontWeight.SemiBold)
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
      // Month Header Navigation
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              IconButton(onClick = {
                if (viewingMonth == 1) {
                  viewingMonth = 12
                  viewingYear--
                } else {
                  viewingMonth--
                }
              }) {
                Icon(Icons.Outlined.ChevronLeft, contentDescription = "Previous Month")
              }

              Text(
                text = monthName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )

              IconButton(onClick = {
                if (viewingMonth == 12) {
                  viewingMonth = 1
                  viewingYear++
                } else {
                  viewingMonth++
                }
              }) {
                Icon(Icons.Outlined.ChevronRight, contentDescription = "Next Month")
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Day of week labels
            val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              daysOfWeek.forEach { dayLabel ->
                Text(
                  text = dayLabel,
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.width(32.dp),
                  textAlign = TextAlign.Center
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Calendar Days Grid
            val days = (1..maxDaysInMonth).toList()
            val chunkedDays = days.chunked(7)

            chunkedDays.forEach { week ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround
              ) {
                week.forEach { dayNumber ->
                  val isSelected = viewingYear == state.selectedCalendarDateYear &&
                    viewingMonth == state.selectedCalendarDateMonth &&
                    dayNumber == state.selectedCalendarDateDay

                  val isToday = Calendar.getInstance().let {
                    it.get(Calendar.YEAR) == viewingYear &&
                      it.get(Calendar.MONTH) + 1 == viewingMonth &&
                      it.get(Calendar.DAY_OF_MONTH) == dayNumber
                  }

                  Box(
                    modifier = Modifier
                      .size(36.dp)
                      .clip(CircleShape)
                      .background(
                        when {
                          isSelected -> MaterialTheme.colorScheme.primary
                          isToday -> MaterialTheme.colorScheme.secondaryContainer
                          else -> androidx.compose.ui.graphics.Color.Transparent
                        }
                      )
                      .clickable {
                        onDateSelected(viewingYear, viewingMonth, dayNumber)
                      },
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = "$dayNumber",
                      style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 13.sp
                      ),
                      color = when {
                        isSelected -> MaterialTheme.colorScheme.onPrimary
                        isToday -> MaterialTheme.colorScheme.onSecondaryContainer
                        else -> MaterialTheme.colorScheme.onSurface
                      }
                    )
                  }
                }

                // Fill remainder of row if incomplete week
                repeat(7 - week.size) {
                  Spacer(modifier = Modifier.width(36.dp))
                }
              }
            }
          }
        }
      }

      // Schedule Preview for Selected Date
      item {
        val selectedCal = Calendar.getInstance().apply {
          set(Calendar.YEAR, state.selectedCalendarDateYear)
          set(Calendar.MONTH, state.selectedCalendarDateMonth - 1)
          set(Calendar.DAY_OF_MONTH, state.selectedCalendarDateDay)
        }
        val previewDateFormatted = HijriCalendarHelper.formatGregorianDate(selectedCal, state.selectedLanguage)
        val previewHijri = HijriCalendarHelper.getHijriDate(selectedCal).formatted(state.selectedLanguage)

        Column(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "PRAYER TIMES FOR SELECTED DATE",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.8.sp
            ),
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "$previewDateFormatted • $previewHijri",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(12.dp))

          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              selectedDateSchedule.obligatoryPrayers.forEach { item ->
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    Text(
                      text = item.type.englishName,
                      style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = item.type.arabicName,
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }

                  Text(
                    text = item.timeFormatted,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                  )
                }

                if (item.type != com.example.model.PrayerType.ISHA) {
                  Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                }
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}
