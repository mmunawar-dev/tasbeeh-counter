package com.example.model

import androidx.compose.ui.unit.LayoutDirection

enum class AppLanguage(
  val displayName: String,
  val nativeName: String,
  val code: String,
  val layoutDirection: LayoutDirection
) {
  ENGLISH("English", "English", "en", LayoutDirection.Ltr),
  URDU("Urdu", "اردو", "ur", LayoutDirection.Rtl),
  ARABIC("Arabic", "العربية", "ar", LayoutDirection.Rtl)
}
