package com.example.model

import androidx.compose.ui.unit.LayoutDirection

enum class AppLanguage(
  val displayName: String,
  val nativeName: String,
  val code: String,
  val flagEmoji: String,
  val layoutDirection: LayoutDirection = LayoutDirection.Ltr
) {
  ENGLISH("English", "English", "en", "🇬🇧", LayoutDirection.Ltr),
  ARABIC("Arabic", "العربية", "ar", "🇸🇦", LayoutDirection.Rtl),
  URDU("Urdu", "اردو", "ur", "🇵🇰", LayoutDirection.Rtl),
  INDONESIAN("Indonesian", "Bahasa Indonesia", "id", "🇮🇩", LayoutDirection.Ltr),
  TURKISH("Turkish", "Türkçe", "tr", "🇹🇷", LayoutDirection.Ltr),
  FRENCH("French", "Français", "fr", "🇫🇷", LayoutDirection.Ltr),
  SPANISH("Spanish", "Español", "es", "🇪🇸", LayoutDirection.Ltr),
  MALAY("Malay", "Bahasa Melayu", "ms", "🇲🇾", LayoutDirection.Ltr),
  BENGALI("Bengali", "বাংলা", "bn", "🇧🇩", LayoutDirection.Ltr),
  RUSSIAN("Russian", "Русский", "ru", "🇷🇺", LayoutDirection.Ltr),
  PERSIAN("Persian", "فارسی", "fa", "🇮🇷", LayoutDirection.Rtl),
  HINDI("Hindi", "हिन्दी", "hi", "🇮🇳", LayoutDirection.Ltr),
  GERMAN("German", "Deutsch", "de", "🇩🇪", LayoutDirection.Ltr),
  UZBEK("Uzbek", "Oʻzbekcha", "uz", "🇺🇿", LayoutDirection.Ltr),
  HAUSA("Hausa", "Hausa", "ha", "🇳🇬", LayoutDirection.Ltr),
  SOMALI("Somali", "Soomaali", "so", "🇸🇴", LayoutDirection.Ltr);
}
