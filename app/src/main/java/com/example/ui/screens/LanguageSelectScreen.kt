package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
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
    topBar = {
      // Top Header: "Languages" with ~10dp top margin, and clickable "✓ Apply" text action
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          if (isSettingsMode && onBack != null) {
            IconButton(
              onClick = onBack,
              modifier = Modifier
                .testTag("back_button")
                .size(36.dp)
            ) {
              Text(
                text = "←",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
          }
          Text(
            text = "Languages",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 24.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
          )
        }

        // Apply action with tick icon
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onContinue)
            .testTag("language_apply_button")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.Check,
              contentDescription = "Apply",
              tint = MaterialTheme.colorScheme.onPrimary,
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "Apply",
              style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              ),
              color = MaterialTheme.colorScheme.onPrimary
            )
          }
        }
      }
    }
  ) { padding ->
    // 2-Column Grid of Language Cards (respecting RTL per language where appropriate)
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(AppLanguage.entries) { language ->
        val isSelected = language == selectedLanguage

        Surface(
          shape = RoundedCornerShape(16.dp),
          color = if (isSelected) sakinahColors.activePrayerBackground else MaterialTheme.colorScheme.surface,
          border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) sakinahColors.activePrayerBorder else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
          ),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onLanguageSelected(language) }
            .testTag("language_option_${language.code}")
        ) {
          CompositionLocalProvider(LocalLayoutDirection provides language.layoutDirection) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
              ) {
                Text(
                  text = language.displayName,
                  style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    fontSize = 15.sp
                  ),
                  color = MaterialTheme.colorScheme.onSurface,
                  maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = language.nativeName,
                  style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                  ),
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 1
                )
              }

              // Trailing side: Country flag + selection indicator
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = language.flagEmoji,
                  fontSize = 20.sp
                )

                if (isSelected) {
                  Box(
                    modifier = Modifier
                      .size(20.dp)
                      .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Outlined.Check,
                      contentDescription = "Selected",
                      tint = MaterialTheme.colorScheme.onPrimary,
                      modifier = Modifier.size(12.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
