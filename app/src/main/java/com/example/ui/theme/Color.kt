package com.example.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Sakinah Color System
 *
 * A sophisticated, serene, and neutral palette designed specifically for a modern
 * Muslim prayer and worship companion.
 *
 * Philosophy:
 * - Neutral Warm Alabaster foundation in Light mode (reminiscent of handmade paper/parchment)
 * - Deep Slate Obsidian foundation in Dark mode (soothing to the eye during night/Tahajjud prayers)
 * - Restrained Deep Emerald & Sand Ochre accents (spiritual, mature, non-garish)
 * - Dedicated multi-tier surface and text hierarchies for maximum clarity
 */

// ==========================================
// LIGHT PALETTE (Daytime Serenity)
// ==========================================

// Primary Brand — Deep Heritage Emerald
val EmeraldPrimaryLight = Color(0xFF133E31)
val EmeraldOnPrimaryLight = Color(0xFFFFFFFF)
val EmeraldPrimaryContainerLight = Color(0xFFE2ECE7)
val EmeraldOnPrimaryContainerLight = Color(0xFF092920)

// Secondary Accent — Restrained Desert Ochre / Warm Brass
val OchreSecondaryLight = Color(0xFF8A6B38)
val OchreOnSecondaryLight = Color(0xFFFFFFFF)
val OchreSecondaryContainerLight = Color(0xFFF7EFE2)
val OchreOnSecondaryContainerLight = Color(0xFF2C200C)

// Tertiary Accent — Soft Sage Mist
val SageTertiaryLight = Color(0xFF3F6355)
val SageOnTertiaryLight = Color(0xFFFFFFFF)
val SageTertiaryContainerLight = Color(0xFFE5EEE9)
val SageOnTertiaryContainerLight = Color(0xFF132A21)

// Surfaces & Background (Warm Alabaster Neutral Hierarchy)
val BackgroundLight = Color(0xFFF9F9F6)           // Base canvas: warm off-white parchment
val SurfaceLight = Color(0xFFFFFFFF)              // Primary elevated card surface
val SurfaceContainerLowLight = Color(0xFFF3F4EF)  // Subtle recessed grouping
val SurfaceContainerLight = Color(0xFFECEDE7)     // Mid container
val SurfaceContainerHighLight = Color(0xFFE5E7E0) // Higher emphasis container
val SurfaceVariantLight = Color(0xFFEDF0EA)       // Outlined cards & pill chips

// Outlines & Borders
val OutlineLight = Color(0xFFD3DCD4)
val OutlineVariantLight = Color(0xFFE2E7E2)

// Text & Content Hierarchy (Contrast ratio compliant)
val TextPrimaryLight = Color(0xFF18211D)   // High emphasis (prayer times, headers)
val TextSecondaryLight = Color(0xFF4E5D55) // Medium emphasis (subtitles, secondary info)
val TextTertiaryLight = Color(0xFF7B8B82)  // Low emphasis (captions, timestamps)
val TextQuaternaryLight = Color(0xFFA5B2AA)// Disabled / placeholder

// Active Prayer State (Light)
val ActivePrayerBackgroundLight = Color(0xFFEDF5F1)
val ActivePrayerBorderLight = Color(0xFF246B54)
val ActivePrayerAccentLight = Color(0xFF133E31)

// ==========================================
// DARK PALETTE (Nocturnal Serenity)
// ==========================================

// Primary Brand — Luminous Soft Jade
val EmeraldPrimaryDark = Color(0xFF52A88D)
val EmeraldOnPrimaryDark = Color(0xFF06241A)
val EmeraldPrimaryContainerDark = Color(0xFF1B3B30)
val EmeraldOnPrimaryContainerDark = Color(0xFFBCE7D6)

// Secondary Accent — Warm Gilded Sand
val OchreSecondaryDark = Color(0xFFD6B377)
val OchreOnSecondaryDark = Color(0xFF38290E)
val OchreSecondaryContainerDark = Color(0xFF3A301C)
val OchreOnSecondaryContainerDark = Color(0xFFF4E4C6)

// Tertiary Accent — Soft Ethereal Celadon
val SageTertiaryDark = Color(0xFF86B09E)
val SageOnTertiaryDark = Color(0xFF0D251C)
val SageTertiaryContainerDark = Color(0xFF223E33)
val SageOnTertiaryContainerDark = Color(0xFFC7E2D7)

// Surfaces & Background (Deep Obsidian Slate Neutral Hierarchy)
val BackgroundDark = Color(0xFF111714)           // Base canvas: deep slate black (not harsh OLED black)
val SurfaceDark = Color(0xFF17201C)              // Primary elevated card surface
val SurfaceContainerLowDark = Color(0xFF141C18)  // Subtle recessed section
val SurfaceContainerDark = Color(0xFF1D2722)     // Mid container
val SurfaceContainerHighDark = Color(0xFF23302A) // Higher emphasis container
val SurfaceVariantDark = Color(0xFF1E2B25)       // Outlined cards & chips

// Outlines & Borders
val OutlineDark = Color(0xFF2D3C35)
val OutlineVariantDark = Color(0xFF24322C)

// Text & Content Hierarchy (Dark)
val TextPrimaryDark = Color(0xFFE7EFEA)   // High emphasis
val TextSecondaryDark = Color(0xFF9CAEA5) // Medium emphasis
val TextTertiaryDark = Color(0xFF6B7D74)  // Low emphasis
val TextQuaternaryDark = Color(0xFF4A5951)// Disabled / placeholder

// Active Prayer State (Dark)
val ActivePrayerBackgroundDark = Color(0xFF1A2E25)
val ActivePrayerBorderDark = Color(0xFF3E8A6E)
val ActivePrayerAccentDark = Color(0xFF52A88D)

// ==========================================
// SEMANTIC ACCENTS
// ==========================================
val NotificationActiveColor = Color(0xFF22825E)
val NotificationMutedColor = Color(0xFF84958C)
val GoldHighlightColor = Color(0xFFC49A45)
