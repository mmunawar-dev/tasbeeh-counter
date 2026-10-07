package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.model.AppLanguage
import com.example.ui.theme.LocalSakinahColors

@Composable
fun LanguageSelectScreen(
  selectedLanguage: AppLanguage,
  onLanguageSelected: (AppLanguage) -> Unit,
  onContinue: () -> Unit,
  isSettingsMode: Boolean = false,
  onBack: (() -> Unit)? = null
) {
  val sakinahColors = LocalSakinahColors.current

  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    bottomBar = {
      Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 24.dp, vertical = 16.dp)
      ) {
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
            .testTag("language_continue_button")
        ) {
          Text(
            text = if (isSettingsMode) "Save Changes" else "Continue",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
          )
        }
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .statusBarsPadding()
        .padding(horizontal = 24.dp),
      contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      if (isSettingsMode && onBack != null) {
        item {
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
      }

      item {
        Column {
          Text(
            text = "Choose your language",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "Select from ${AppLanguage.entries.size} available languages. App orientation remains stable.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(16.dp))
        }
      }

      items(AppLanguage.entries) { language ->
        val isSelected = language == selectedLanguage
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = if (isSelected) sakinahColors.activePrayerBackground else MaterialTheme.colorScheme.surface,
          border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) sakinahColors.activePrayerBorder else MaterialTheme.colorScheme.outlineVariant
          ),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onLanguageSelected(language) }
            .testTag("language_option_${language.code}")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = language.displayName,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = language.nativeName,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            // Radio / Check Indicator
            Box(
              modifier = Modifier
                .size(24.dp)
                .background(
                  if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                  CircleShape
                ),
              contentAlignment = Alignment.Center
            ) {
              if (isSelected) {
                Icon(
                  imageVector = Icons.Outlined.Check,
                  contentDescription = "Selected",
                  tint = MaterialTheme.colorScheme.onPrimary,
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}
