package com.example.model

enum class CompassStyle(
  val title: String,
  val subtitle: String
) {
  CLASSIC_GOLD(
    title = "Classic Gold",
    subtitle = "Traditional brass dial with gilded Kaaba needle"
  ),
  EMERALD_MINIMAL(
    title = "Emerald Modern",
    subtitle = "Sleek minimalist ring with luminous green needle"
  ),
  ASTROLABE(
    title = "Islamic Astrolabe",
    subtitle = "Ornamental celestial rose with antique arrow"
  ),
  KAABA_ARROW(
    title = "Kaaba Falcon Arrow",
    subtitle = "High-visibility 3D directional arrow with Kaaba emblem"
  ),
  NIGHT_CELESTIAL(
    title = "Midnight Celestial",
    subtitle = "Deep obsidian dial with glowing celestial markers"
  )
}
