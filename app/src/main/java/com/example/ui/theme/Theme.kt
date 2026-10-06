package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Extended semantic tokens for surface layers, text emphasis, and worship states.
 */
@Immutable
data class SakinahCustomColors(
  val textPrimary: Color,
  val textSecondary: Color,
  val textTertiary: Color,
  val textQuaternary: Color,
  val surfaceContainerLow: Color,
  val surfaceContainer: Color,
  val surfaceContainerHigh: Color,
  val activePrayerBackground: Color,
  val activePrayerBorder: Color,
  val activePrayerAccent: Color,
  val notificationActive: Color,
  val notificationMuted: Color,
  val goldHighlight: Color
)

val LocalSakinahColors = staticCompositionLocalOf {
  SakinahCustomColors(
    textPrimary = TextPrimaryLight,
    textSecondary = TextSecondaryLight,
    textTertiary = TextTertiaryLight,
    textQuaternary = TextQuaternaryLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    activePrayerBackground = ActivePrayerBackgroundLight,
    activePrayerBorder = ActivePrayerBorderLight,
    activePrayerAccent = ActivePrayerAccentLight,
    notificationActive = NotificationActiveColor,
    notificationMuted = NotificationMutedColor,
    goldHighlight = GoldHighlightColor
  )
}

object SakinahTheme {
  val colors: SakinahCustomColors
    @Composable
    @ReadOnlyComposable
    get() = LocalSakinahColors.current
}

private val LightColorScheme: ColorScheme = lightColorScheme(
  primary = EmeraldPrimaryLight,
  onPrimary = EmeraldOnPrimaryLight,
  primaryContainer = EmeraldPrimaryContainerLight,
  onPrimaryContainer = EmeraldOnPrimaryContainerLight,
  secondary = OchreSecondaryLight,
  onSecondary = OchreOnSecondaryLight,
  secondaryContainer = OchreSecondaryContainerLight,
  onSecondaryContainer = OchreOnSecondaryContainerLight,
  tertiary = SageTertiaryLight,
  onTertiary = SageOnTertiaryLight,
  tertiaryContainer = SageTertiaryContainerLight,
  onTertiaryContainer = SageOnTertiaryContainerLight,
  background = BackgroundLight,
  onBackground = TextPrimaryLight,
  surface = SurfaceLight,
  onSurface = TextPrimaryLight,
  surfaceVariant = SurfaceVariantLight,
  onSurfaceVariant = TextSecondaryLight,
  outline = OutlineLight,
  outlineVariant = OutlineVariantLight
)

private val DarkColorScheme: ColorScheme = darkColorScheme(
  primary = EmeraldPrimaryDark,
  onPrimary = EmeraldOnPrimaryDark,
  primaryContainer = EmeraldPrimaryContainerDark,
  onPrimaryContainer = EmeraldOnPrimaryContainerDark,
  secondary = OchreSecondaryDark,
  onSecondary = OchreOnSecondaryDark,
  secondaryContainer = OchreSecondaryContainerDark,
  onSecondaryContainer = OchreOnSecondaryContainerDark,
  tertiary = SageTertiaryDark,
  onTertiary = SageOnTertiaryDark,
  tertiaryContainer = SageTertiaryContainerDark,
  onTertiaryContainer = SageOnTertiaryContainerDark,
  background = BackgroundDark,
  onBackground = TextPrimaryDark,
  surface = SurfaceDark,
  onSurface = TextPrimaryDark,
  surfaceVariant = SurfaceVariantDark,
  onSurfaceVariant = TextSecondaryDark,
  outline = OutlineDark,
  outlineVariant = OutlineVariantDark
)

@Composable
fun SakinahTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  val customColors = if (darkTheme) {
    SakinahCustomColors(
      textPrimary = TextPrimaryDark,
      textSecondary = TextSecondaryDark,
      textTertiary = TextTertiaryDark,
      textQuaternary = TextQuaternaryDark,
      surfaceContainerLow = SurfaceContainerLowDark,
      surfaceContainer = SurfaceContainerDark,
      surfaceContainerHigh = SurfaceContainerHighDark,
      activePrayerBackground = ActivePrayerBackgroundDark,
      activePrayerBorder = ActivePrayerBorderDark,
      activePrayerAccent = ActivePrayerAccentDark,
      notificationActive = EmeraldPrimaryDark,
      notificationMuted = NotificationMutedColor,
      goldHighlight = OchreSecondaryDark
    )
  } else {
    SakinahCustomColors(
      textPrimary = TextPrimaryLight,
      textSecondary = TextSecondaryLight,
      textTertiary = TextTertiaryLight,
      textQuaternary = TextQuaternaryLight,
      surfaceContainerLow = SurfaceContainerLowLight,
      surfaceContainer = SurfaceContainerLight,
      surfaceContainerHigh = SurfaceContainerHighLight,
      activePrayerBackground = ActivePrayerBackgroundLight,
      activePrayerBorder = ActivePrayerBorderLight,
      activePrayerAccent = ActivePrayerAccentLight,
      notificationActive = NotificationActiveColor,
      notificationMuted = NotificationMutedColor,
      goldHighlight = GoldHighlightColor
    )
  }

  CompositionLocalProvider(LocalSakinahColors provides customColors) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
  }
}
