package com.example.model

data class DhikrPreset(
  val id: String,
  val arabicText: String,
  val transliteration: String,
  val translation: String,
  val targetCount: Int = 33
) {
  companion object {
    val PRESETS = listOf(
      DhikrPreset(
        id = "subhanallah",
        arabicText = "سُبْحَانَ اللَّهِ",
        transliteration = "SubhanAllah",
        translation = "Glory be to Allah",
        targetCount = 33
      ),
      DhikrPreset(
        id = "alhamdulillah",
        arabicText = "الْحَمْدُ لِلَّهِ",
        transliteration = "Alhamdulillah",
        translation = "Praise be to Allah",
        targetCount = 33
      ),
      DhikrPreset(
        id = "allahuakbar",
        arabicText = "اللَّهُ أَكْبَرُ",
        transliteration = "Allahu Akbar",
        translation = "Allah is the Greatest",
        targetCount = 34
      ),
      DhikrPreset(
        id = "astaghfirullah",
        arabicText = "أَسْتَغْفِرُ اللَّهَ",
        transliteration = "Astaghfirullah",
        translation = "I seek forgiveness from Allah",
        targetCount = 100
      ),
      DhikrPreset(
        id = "lailahaillallah",
        arabicText = "لَا إِلٰهَ إِلَّا اللَّهُ",
        transliteration = "La ilaha illallah",
        translation = "There is no deity worthy of worship except Allah",
        targetCount = 100
      ),
      DhikrPreset(
        id = "salawat",
        arabicText = "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ",
        transliteration = "Allahumma Salli 'ala Muhammad",
        translation = "O Allah, send blessings upon Muhammad",
        targetCount = 100
      ),
      DhikrPreset(
        id = "hawqala",
        arabicText = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
        transliteration = "La hawla wa la quwwata illa billah",
        translation = "There is no power nor strength except by Allah",
        targetCount = 33
      )
    )
  }
}
