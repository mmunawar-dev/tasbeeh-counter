package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class OnboardingPageData(
  val icon: ImageVector,
  val title: String,
  val description: String,
  val subtitlePill: String
)

@Composable
fun OnboardingScreen(
  onComplete: () -> Unit
) {
  var currentPage by remember { mutableStateOf(0) }

  val pages = listOf(
    OnboardingPageData(
      icon = Icons.Outlined.AccessTime,
      title = "Know your prayer times",
      description = "Accurate daily prayer schedule calculated precisely for your location, sun position, and fiqh preferences.",
      subtitlePill = "ACCURATE & LOCALIZED"
    ),
    OnboardingPageData(
      icon = Icons.Outlined.NotificationsActive,
      title = "Stay connected throughout the day",
      description = "Get gentle, dependable reminders for each prayer with customizable adhan, subtle vibration, or quiet alerts.",
      subtitlePill = "PEACEFUL NOTIFICATIONS"
    ),
    OnboardingPageData(
      icon = Icons.Outlined.Tune,
      title = "Your worship, your settings",
      description = "Easily select your preferred school of thought (Hanafi, Shafi'i, Maliki, Hanbali), calculation authority, and tasbeeh counter.",
      subtitlePill = "TAILORED TO YOUR PRACTICE"
    )
  )

  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    topBar = {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.End
      ) {
        if (currentPage < pages.lastIndex) {
          TextButton(
            onClick = onComplete,
            modifier = Modifier.testTag("onboarding_skip_button")
          ) {
            Text(
              text = "Skip",
              style = MaterialTheme.typography.labelLarge,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        } else {
          Spacer(modifier = Modifier.height(36.dp))
        }
      }
    },
    bottomBar = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Dot Indicators
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          pages.indices.forEach { index ->
            val isCurrent = index == currentPage
            Box(
              modifier = Modifier
                .height(6.dp)
                .width(if (isCurrent) 24.dp else 6.dp)
                .clip(CircleShape)
                .background(
                  if (isCurrent) MaterialTheme.colorScheme.primary
                  else MaterialTheme.colorScheme.outlineVariant
                )
            )
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
          onClick = {
            if (currentPage < pages.lastIndex) {
              currentPage++
            } else {
              onComplete()
            }
          },
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
          ),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("onboarding_next_button")
        ) {
          Text(
            text = if (currentPage == pages.lastIndex) "Get Started" else "Continue",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
          )
        }
      }
    }
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(horizontal = 24.dp),
      contentAlignment = Alignment.Center
    ) {
      AnimatedContent(
        targetState = currentPage,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "onboarding_content"
      ) { targetPage ->
        val page = pages[targetPage]
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center,
          modifier = Modifier.fillMaxWidth()
        ) {
          // Editorial Geometric Graphic
          Box(
            modifier = Modifier
              .size(130.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Box(
              modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = page.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(46.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(28.dp))

          // Subtitle Tag
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.secondaryContainer
          ) {
            Text(
              text = page.subtitlePill,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 0.8.sp
              ),
              color = MaterialTheme.colorScheme.onSecondaryContainer,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 24.sp
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge.copy(
              lineHeight = 22.sp,
              fontSize = 15.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
          )
        }
      }
    }
  }
}
