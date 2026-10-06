package com.example.ui.screens

import android.content.Context
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
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DhikrPreset
import com.example.model.SakinahUiState

@Composable
fun TasbeehScreen(
  state: SakinahUiState,
  onIncrement: (Context) -> Unit,
  onReset: () -> Unit,
  onSelectDhikr: (DhikrPreset) -> Unit,
  onSetTarget: (Int) -> Unit,
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
      // Dhikr Presets Horizontal Carousel
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

      Spacer(modifier = Modifier.height(16.dp))

      // Current Dhikr Arabic Display Card
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = state.currentDhikr.arabicText,
            style = MaterialTheme.typography.headlineLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 30.sp,
              textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.height(6.dp))
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

      Spacer(modifier = Modifier.weight(1f))

      // Main Meditative Bead Counter Dial
      Box(
        modifier = Modifier
          .size(240.dp)
          .testTag("tasbeeh_counter_dial")
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = false, radius = 120.dp),
            onClick = { onIncrement(context) }
          ),
        contentAlignment = Alignment.Center
      ) {
        // Circular Progress Ring
        CircularProgressIndicator(
          progress = { animatedProgress },
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.primary,
          trackColor = MaterialTheme.colorScheme.surfaceVariant,
          strokeWidth = 10.dp,
          strokeCap = StrokeCap.Round
        )

        // Inner Circle Tap Surface
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
                fontSize = 52.sp
              ),
              color = MaterialTheme.colorScheme.onSurface
            )

            Text(
              text = "of ${state.tasbeehTotalTarget} beads",
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (state.tasbeehLaps > 0) {
              Spacer(modifier = Modifier.height(4.dp))
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.primaryContainer
              ) {
                Text(
                  text = "Lap ${state.tasbeehLaps}",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                  ),
                  color = MaterialTheme.colorScheme.onPrimaryContainer,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
              }
            }
          }
        }
      }

      Text(
        text = "Tap circle anywhere to count",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        modifier = Modifier.padding(top = 12.dp)
      )

      Spacer(modifier = Modifier.weight(1f))

      // Target Selector (33, 99, 100)
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
