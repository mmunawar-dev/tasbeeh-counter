package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  onProceed: () -> Unit
) {
  val alphaAnim = remember { Animatable(0f) }
  val scaleAnim = remember { Animatable(0.92f) }

  LaunchedEffect(Unit) {
    alphaAnim.animateTo(1f, animationSpec = tween(700))
    scaleAnim.animateTo(1f, animationSpec = tween(700))
    delay(1300)
    onProceed()
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .testTag("splash_screen"),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier
        .alpha(alphaAnim.value)
        .scale(scaleAnim.value)
    ) {
      // Emblem Icon
      Box(
        modifier = Modifier
          .size(96.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = R.drawable.ic_app_logo_1791310765262),
          contentDescription = "Sakinah Emblem",
          modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      Text(
        text = "Sakinah",
        style = MaterialTheme.typography.headlineLarge.copy(
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 1.sp,
          fontSize = 32.sp
        ),
        color = MaterialTheme.colorScheme.onBackground
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "سكينة • Prayer & Worship Companion",
        style = MaterialTheme.typography.bodyMedium.copy(
          letterSpacing = 0.5.sp,
          fontSize = 14.sp
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
