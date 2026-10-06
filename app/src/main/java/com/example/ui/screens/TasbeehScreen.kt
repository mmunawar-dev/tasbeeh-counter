package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DhikrPreset
import com.example.model.SakinahUiState
import com.example.model.TasbeehStyle

@Composable
fun TasbeehScreen(
  state: SakinahUiState,
  onIncrement: (Context) -> Unit,
  onReset: () -> Unit,
  onSelectDhikr: (DhikrPreset) -> Unit,
  onSetTarget: (Int) -> Unit,
  onSelectStyle: (TasbeehStyle) -> Unit,
  onToggleHaptic: () -> Unit,
  onBack: () -> Unit
) {
  val context = LocalContext.current
  val progress = if (state.tasbeehTotalTarget > 0) {
    (state.tasbeehCount.toFloat() / state.tasbeehTotalTarget.toFloat()).coerceIn(0f, 1f)
  } else 0f

  val animatedProgress by animateFloatAsState(
    targetValue = progress,
    animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
    label = "tasbeeh_progress"
  )

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
            modifier = Modifier.testTag("tasbeeh_back_button")
          ) {
            Text(
              text = "←",
              style = MaterialTheme.typography.headlineMedium,
              color = MaterialTheme.colorScheme.onBackground
            )
          }
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Tasbeeh Companion",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )
        }

        Row {
          IconButton(
            onClick = onToggleHaptic,
            modifier = Modifier.testTag("tasbeeh_haptic_toggle")
          ) {
            Icon(
              imageVector = Icons.Outlined.Vibration,
              contentDescription = "Haptic feedback toggle",
              tint = if (state.isHapticEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
          }

          IconButton(
            onClick = onReset,
            modifier = Modifier.testTag("tasbeeh_reset_button")
          ) {
            Icon(
              imageVector = Icons.Outlined.Refresh,
              contentDescription = "Reset Count",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(horizontal = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // 1. Dhikr Presets Carousel
      LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
      ) {
        items(DhikrPreset.PRESETS, key = { it.id }) { preset ->
          val isSelected = preset.id == state.currentDhikr.id
          FilterChip(
            selected = isSelected,
            onClick = { onSelectDhikr(preset) },
            label = {
              Text(
                text = preset.transliteration,
                style = MaterialTheme.typography.labelMedium
              )
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.testTag("dhikr_${preset.id}")
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 2. Active Dhikr Card
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = state.currentDhikr.arabicText,
            style = MaterialTheme.typography.headlineLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 28.sp,
              textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = state.currentDhikr.transliteration,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "“${state.currentDhikr.translation}”",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 3. Counter Style Switcher Tabs
      LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 2.dp)
      ) {
        items(TasbeehStyle.values()) { style ->
          val isSelected = state.selectedTasbeehStyle == style
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .clickable { onSelectStyle(style) }
          ) {
            Text(
              text = style.title,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 11.sp
              ),
              color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.weight(1f))

      // 4. Five Distinct Digital Counter Views
      when (state.selectedTasbeehStyle) {
        TasbeehStyle.MINIMAL -> {
          // Minimalist Digital View
          Box(
            modifier = Modifier
              .size(240.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.surface)
              .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
              .clickable { onIncrement(context) },
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "${state.tasbeehCount}",
                style = MaterialTheme.typography.displayLarge.copy(
                  fontWeight = FontWeight.Light,
                  fontSize = 68.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "target ${state.tasbeehTotalTarget}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        TasbeehStyle.CIRCULAR -> {
          // Circular Progress Dial
          Box(
            modifier = Modifier
              .size(240.dp)
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 120.dp),
                onClick = { onIncrement(context) }
              ),
            contentAlignment = Alignment.Center
          ) {
            CircularProgressIndicator(
              progress = { animatedProgress },
              modifier = Modifier.fillMaxSize(),
              color = MaterialTheme.colorScheme.primary,
              trackColor = MaterialTheme.colorScheme.surfaceVariant,
              strokeWidth = 10.dp,
              strokeCap = StrokeCap.Round
            )
            Box(
              modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                  text = "${state.tasbeehCount}",
                  style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 54.sp
                  ),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "of ${state.tasbeehTotalTarget} beads",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }

        TasbeehStyle.PREMIUM_DARK -> {
          // Obsidian Emerald LED View
          Surface(
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFF111714),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF234B3D)),
            shadowElevation = 8.dp,
            modifier = Modifier
              .size(240.dp)
              .clip(RoundedCornerShape(28.dp))
              .clickable { onIncrement(context) }
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Text(
                text = String.format("%03d", state.tasbeehCount),
                style = MaterialTheme.typography.displayLarge.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 58.sp,
                  fontFamily = FontFamily.Monospace,
                  letterSpacing = 4.sp
                ),
                color = Color(0xFF55C7A1)
              )
              Text(
                text = "LED TALLY • LAP ${state.tasbeehLaps}",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  fontSize = 10.sp
                ),
                color = Color(0xFF759A8D)
              )
            }
          }
        }

        TasbeehStyle.TRADITIONAL -> {
          // Classic Misbaha Prayer Beads
          Surface(
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier
              .width(260.dp)
              .height(180.dp)
              .clip(RoundedCornerShape(32.dp))
              .clickable { onIncrement(context) }
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center,
              modifier = Modifier.padding(16.dp)
            ) {
              Text(
                text = "📿",
                fontSize = 32.sp
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "${state.tasbeehCount}",
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = "Tap bead to advance",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        TasbeehStyle.POCKET -> {
          // Pocket Mechanical Clicker
          Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
              .size(220.dp)
              .clip(RoundedCornerShape(24.dp))
              .clickable { onIncrement(context) }
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
              ) {
                Text(
                  text = "${state.tasbeehCount}",
                  style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
              }
              Spacer(modifier = Modifier.height(14.dp))
              Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(
                    text = "CLICK",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onPrimary,
                      fontSize = 10.sp
                    )
                  )
                }
              }
            }
          }
        }
      }

      Text(
        text = "Tap anywhere on counter to count • Lap ${state.tasbeehLaps}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        modifier = Modifier.padding(top = 10.dp)
      )

      Spacer(modifier = Modifier.weight(1f))

      // 5. Target Selector Chips (33, 99, 100)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        val targets = listOf(33, 99, 100)
        Text(
          text = "Target:",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(end = 12.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          targets.forEach { target ->
            val isSelected = state.tasbeehTotalTarget == target
            FilterChip(
              selected = isSelected,
              onClick = { onSetTarget(target) },
              label = { Text("$target") }
            )
          }
        }
      }
    }
  }
}
