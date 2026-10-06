package com.example.model

data class DuaCategory(
  val id: String,
  val title: String,
  val arabicTitle: String,
  val iconName: String,
  val count: Int,
  val description: String
) {
  companion object {
    val CATEGORIES = listOf(
      DuaCategory("morning_evening", "Morning & Evening", "أذكار الصباح والمساء", "wb_sunny", 8, "Protection, light and gratitude for dawn and twilight."),
      DuaCategory("prayer", "Prayer & After Salah", "أدعية الصلاة", "mosque", 6, "Supplications in Sujood, Tashahhud and after Fard prayers."),
      DuaCategory("protection", "Protection & Refuge", "أدعية الحفظ", "shield", 5, "Refuge from evil eye, envy, shaytan and harm."),
      DuaCategory("forgiveness", "Forgiveness & Repentance", "الاستغفار والتوبة", "favorite", 5, "Sayyid al-Istighfar and sincere pleas for mercy."),
      DuaCategory("anxiety", "Anxiety & Hardship", "الهم والحزن والفرج", "spa", 4, "Calmness, relief from distress, debt and sorrow."),
      DuaCategory("sleep", "Sleep & Night", "أذكار النوم", "bedtime", 4, "Waking up, retiring to bed, and protection in sleep."),
      DuaCategory("travel", "Travel & Journey", "دعاء السفر", "flight", 3, "Blessings on the road, departures and safe returns."),
      DuaCategory("daily_life", "Daily Life & Food", "الحياة اليومية", "restaurant", 5, "Entering home, before eating, and daily actions.")
    )
  }
}

data class DuaItem(
  val id: String,
  val categoryId: String,
  val title: String,
  val arabic: String,
  val transliteration: String,
  val translation: String,
  val reference: String,
  val benefit: String
) {
  companion object {
    val DUA_ITEMS = listOf(
      DuaItem(
        id = "sayyid_istighfar",
        categoryId = "forgiveness",
        title = "Sayyid al-Istighfar (The Master of Forgiveness)",
        arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
        transliteration = "Allahumma anta Rabbi la ilaha illa anta, khalaqtani wa ana 'abduka, wa ana 'ala 'ahdika wa wa'dika mastata'tu, a'udhu bika min sharri ma sana'tu, abu'u laka bini'matika 'alayya, wa abu'u laka bidhanbi faghfir li fa-innahu la yaghfirudh-dhunuba illa anta.",
        translation = "O Allah, You are my Lord, none has the right to be worshipped but You. You created me and I am Your servant, and I abide by Your covenant and promise as best I can. I seek refuge in You from the evil of what I have done. I acknowledge Your favor upon me, and I acknowledge my sin, so forgive me, for none forgives sins except You.",
        reference = "Sahih al-Bukhari 6306",
        benefit = "Whoever recites this with firm faith in the morning and dies before evening will enter Paradise, and vice-versa."
      ),
      DuaItem(
        id = "morning_praise",
        categoryId = "morning_evening",
        title = "Morning Gratitude & Sovereignty",
        arabic = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
        transliteration = "Asbahna wa-asbahal-mulku lillah, wal-hamdu lillah, la ilaha illallahu wahdahu la sharika lah, lahul-mulku wa lahul-hamdu wa huwa 'ala kulli shay'in qadeer.",
        translation = "We have entered the morning and the kingdom belongs to Allah, and all praise is for Allah. None has the right to be worshipped except Allah alone, without partner. To Him belongs the sovereignty and praise, and He has power over all things.",
        reference = "Sahih Muslim 2723",
        benefit = "Grounds the heart in gratitude and divine sovereignty at the break of dawn."
      ),
      DuaItem(
        id = "protection_three_times",
        categoryId = "protection",
        title = "Protection in the Name of Allah",
        arabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
        transliteration = "Bismillahil-ladhi la yadurru ma'as-mihi shay'un fil-ardi wa la fis-sama'i wa huwas-Sami'ul-'Aleem.",
        translation = "In the Name of Allah, with Whose Name nothing can cause harm in the earth nor in the heavens, and He is the All-Hearing, the All-Knowing.",
        reference = "Abu Dawood 5088, At-Tirmidhi 3388",
        benefit = "Recited 3 times morning and evening; no sudden harm or affliction will touch the servant."
      ),
      DuaItem(
        id = "anxiety_relief",
        categoryId = "anxiety",
        title = "Dua for Relief from Distress & Grief",
        arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ وَغَلَبَةِ الرِّجَالِ",
        transliteration = "Allahumma inni a'udhu bika minal-hammi wal-hazani, wal-'ajzi wal-kasali, wal-bukhli wal-jubni, wa dala'id-dayni wa ghalabatir-rijal.",
        translation = "O Allah, I seek refuge in You from anxiety and grief, from incapacity and laziness, from cowardice and miserliness, from the burden of debt, and from being overpowered by men.",
        reference = "Sahih al-Bukhari 2893",
        benefit = "Comprehensive supplication for psychological tranquility, productivity, and freedom from oppression."
      ),
      DuaItem(
        id = "travel_dua",
        categoryId = "travel",
        title = "Dua for Undertaking a Journey",
        arabic = "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ، وَإِنَّا إِلَى رَبِّنَا لَمُنْقَلِبُونَ",
        transliteration = "Subhanal-ladhi sakh-khara lana hadha wa ma kunna lahu muqrinin, wa inna ila Rabbina lamunqaliboon.",
        translation = "Glory to Him who has brought this under our control though we were unable to subdue it by ourselves, and to our Lord surely we will return.",
        reference = "Surah Az-Zukhruf 43:13-14",
        benefit = "Recited when boarding any vehicle or commencing a travel route."
      ),
      DuaItem(
        id = "sleep_dua",
        categoryId = "sleep",
        title = "Dua When Retiring to Bed",
        arabic = "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
        transliteration = "Bismika Allahumma amutu wa ahya.",
        translation = "In Your Name, O Allah, I die and I live.",
        reference = "Sahih al-Bukhari 6324",
        benefit = "Entrusts the soul to Allah before slumber."
      )
    )
  }
}
