package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalculationMethod
import com.example.model.Madhab
import com.example.ui.theme.LocalSakinahColors

@Composable
fun FiqhSelectScreen(
  selectedMadhab: Madhab,
  selectedMethod: CalculationMethod,
  onMadhabSelected: (Madhab) -> Unit,
  onMethodSelected: (CalculationMethod) -> Unit,
  onContinue: () -> Unit,
  isSettingsMode: Boolean = false,
  onBack: (() -> Unit)? = null
) {
  val sakinahColors = LocalSakinahColors.current
  val scrollState = rememberScrollState()

  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    bottomBar = {
      Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 24.dp, vertical = 14.dp)
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          if (!isSettingsMode) {
            Text(
              text = "You can easily change this anytime in Settings.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(bottom = 10.dp)
            )
          }

          Button(
            onClick = onContinue,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("fiqh_continue_button")
          ) {
            Text(
              text = if (isSettingsMode) "Save Changes" else "Continue",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
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
        .statusBarsPadding()
        .verticalScroll(scrollState)
        .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
      if (isSettingsMode && onBack != null) {
        IconButton(
          onClick = onBack,
          modifier = Modifier
            .testTag("back_button")
            .padding(bottom = 8.dp)
        ) {
          Text(
            text = "←",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
          )
        }
      }

      Text(
        text = "Choose your prayer method",
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onBackground
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Different schools of jurisprudence observe slightly different start times for Asr prayer. Select your preference below.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(24.dp))

      Text(
        text = "ASR TIME CALCULATION",
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.8.sp
        ),
        color = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.height(10.dp))

      Madhab.values().forEach { madhab ->
        val isSelected = madhab == selectedMadhab
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = if (isSelected) sakinahColors.activePrayerBackground else MaterialTheme.colorScheme.surface,
          border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) sakinahColors.activePrayerBorder else MaterialTheme.colorScheme.outlineVariant
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onMadhabSelected(madhab) }
            .testTag("madhab_${madhab.name.lowercase()}")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Text(
                  text = madhab.displayName,
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                  ),
                  color = MaterialTheme.colorScheme.onSurface
                )
                if (madhab == Madhab.HANAFI) {
                  Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                  ) {
                    Text(
                      text = "Recommended for your location",
                      style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                      color = MaterialTheme.colorScheme.onSecondaryContainer,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = madhab.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Surface(
              shape = CircleShape,
              color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier.size(24.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                if (isSelected) {
                  Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(14.dp)
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      Text(
        text = "CALCULATION AUTHORITY",
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.8.sp
        ),
        color = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.height(10.dp))

      CalculationMethod.values().forEach { method ->
        val isSelected = method == selectedMethod
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isSelected) sakinahColors.activePrayerBackground else MaterialTheme.colorScheme.surface,
          border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) sakinahColors.activePrayerBorder else MaterialTheme.colorScheme.outlineVariant
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onMethodSelected(method) }
            .testTag("method_${method.name.lowercase()}")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = method.displayName,
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Used in: ${method.recommendedRegion}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            if (isSelected) {
              Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}
