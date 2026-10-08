package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

data class OnboardingPageData(
  val imageRes: Int,
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
      imageRes = R.drawable.img_prayer_real_1791484687970,
      title = "Know Prayer Times",
      description = "Accurate daily prayer timetable calculated precisely for your location, sun position, and fiqh preferences.",
      subtitlePill = "ACCURATE & LOCALIZED"
    ),
    OnboardingPageData(
      imageRes = R.drawable.img_tasbeeh_real_1791484707858,
      title = "Remember Allah / Tasbeeh",
      description = "Perform your daily dhikr with a digital Tasbeeh counter featuring authentic presets, bead tactile modes, and goal tracking.",
      subtitlePill = "DIGITAL TASBEEH & DHIKR"
    ),
    OnboardingPageData(
      imageRes = R.drawable.img_qibla_real_1791484726025,
      title = "Qibla & Guidance",
      description = "Find the exact direction of the Holy Kaaba with a precise compass, plus authentic daily Duas and Islamic calendar.",
      subtitlePill = "QIBLA COMPASS & DUAS"
    )
  )

  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    contentWindowInsets = WindowInsets(0.dp), // 0dp system top padding: image starts directly from top
    bottomBar = {
      // Single horizontal row: Left: dot/page indicators | Right: compact Continue button ("Next" or "Get Started")
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Page Dots on the LEFT
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          pages.indices.forEach { index ->
            val isCurrent = index == currentPage
            Box(
              modifier = Modifier
                .height(6.dp)
                .width(if (isCurrent) 22.dp else 6.dp)
                .clip(CircleShape)
                .background(
                  if (isCurrent) MaterialTheme.colorScheme.primary
                  else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                )
            )
          }
        }

        // Compact Action Button on the RIGHT
        Button(
          onClick = {
            if (currentPage < pages.lastIndex) {
              currentPage++
            } else {
              onComplete()
            }
          },
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
          ),
          contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
          modifier = Modifier
            .height(44.dp)
            .testTag("onboarding_next_button")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = if (currentPage == pages.lastIndex) "Get Started" else "Next",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            )
            Icon(
              imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }
  ) { padding ->
    // Onboarding top/system padding is 0dp. Image starts directly from parent top.
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(bottom = padding.calculateBottomPadding())
    ) {
      AnimatedContent(
        targetState = currentPage,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "onboarding_content"
      ) { targetPage ->
        val page = pages[targetPage]

        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Large Hero Image:
          // Full width (match_parent), height dynamically filling the top area up to the heading/text section (weight(1f) = height 0dp constrained from parent top to top of heading section)
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f)
              .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
          ) {
            Image(
              painter = painterResource(id = page.imageRes),
              contentDescription = page.title,
              contentScale = ContentScale.Crop, // CenterCrop without distortion
              modifier = Modifier.fillMaxSize()
            )
          }

          // Heading & Text Section: neatly arranged below the image
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 24.dp)
              .padding(top = 20.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            // Subtitle Tag Pill
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
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title
            Text(
              text = page.title,
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
              ),
              color = MaterialTheme.colorScheme.onBackground,
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            Text(
              text = page.description,
              style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 22.sp,
                fontSize = 15.sp
              ),
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(horizontal = 6.dp)
            )
          }
        }
      }
    }
  }
}
