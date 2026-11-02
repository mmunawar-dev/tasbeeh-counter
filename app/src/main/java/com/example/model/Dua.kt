package com.example.model

data class DuaCategory(
  val id: String,
  val title: String,
  val arabicTitle: String,
  val iconName: String,
  val description: String
) {
  val count: Int
    get() = DuaItem.DUA_ITEMS.count { it.categoryId == id }

  constructor(
    id: String,
    title: String,
    arabicTitle: String,
    iconName: String,
    @Suppress("UNUSED_PARAMETER") count: Int,
    description: String
  ) : this(id, title, arabicTitle, iconName, description)

  companion object {
    val CATEGORIES = listOf(
      DuaCategory(
        id = "morning_evening",
        title = "Morning & Evening",
        arabicTitle = "أَذْكَارُ الصَّبَاحِ وَالْمَسَاءِ",
        iconName = "wb_sunny",
        description = "Divine protection, spiritual light, and gratitude for dawn and twilight."
      ),
      DuaCategory(
        id = "prayer",
        title = "Prayer & After Salah",
        arabicTitle = "أَدْعِيَةُ الصَّلَاةِ وَمَا بَعْدَهَا",
        iconName = "mosque",
        description = "Supplications in Sujood, Ruku, Tashahhud, and Sunnah dhikr after Fard prayers."
      ),
      DuaCategory(
        id = "protection",
        title = "Protection & Refuge",
        arabicTitle = "أَدْعِيَةُ الْحِفْظِ وَالِاسْتِعَاذَةِ",
        iconName = "shield",
        description = "Refuge from evil eye, envy, shaytan, calamities, and unseen harm."
      ),
      DuaCategory(
        id = "forgiveness",
        title = "Forgiveness & Repentance",
        arabicTitle = "الِاسْتِغْفَارُ وَالتَّوْبَةُ",
        iconName = "favorite",
        description = "Sayyid al-Istighfar and heartfelt pleas for divine mercy and pardon."
      ),
      DuaCategory(
        id = "anxiety",
        title = "Anxiety & Hardship",
        arabicTitle = "تَفْرِيجُ الْهَمِّ وَالْكَرْبِ",
        iconName = "spa",
        description = "Peace of mind, resilience against grief, distress, debt, and sorrow."
      ),
      DuaCategory(
        id = "sleep",
        title = "Sleep & Night",
        arabicTitle = "أَذْكَارُ النَّوْمِ وَالِاسْتِيقَاظِ",
        iconName = "bedtime",
        description = "Waking up with praise, retiring to bed with peace, and night protection."
      ),
      DuaCategory(
        id = "travel",
        title = "Travel & Journey",
        arabicTitle = "دُعَاءُ السَّفَرِ وَالرُّكُوبِ",
        iconName = "flight",
        description = "Blessings on the road, embarking on transit, and returning safely home."
      ),
      DuaCategory(
        id = "daily_life",
        title = "Daily Life & Food",
        arabicTitle = "الْحَيَاةُ الْيَوْمِيَّةُ وَالطَّعَامُ",
        iconName = "restaurant",
        description = "Entering home, before and after meals, and everyday habits."
      ),
      DuaCategory(
        id = "quranic_duas",
        title = "Quranic Rabbana Duas",
        arabicTitle = "أَدْعِيَةٌ مِنَ الْقُرْآنِ الْكَرِيمِ",
        iconName = "book",
        description = "Timeless supplications revealed by Allah in the Noble Quran."
      ),
      DuaCategory(
        id = "health_illness",
        title = "Health & Sickness",
        arabicTitle = "أَدْعِيَةُ الشِّفَاءِ وَالْعِيَادَةِ",
        iconName = "healing",
        description = "Authentic Prophetic Ruqyah, cure from bodily illness, and visiting the sick."
      ),
      DuaCategory(
        id = "parents_family",
        title = "Parents & Family",
        arabicTitle = "أَدْعِيَةُ الْوَالِدَيْنِ وَالْأُسْرَةِ",
        iconName = "family",
        description = "Compassion for parents, righteous spouses, and blessed offspring."
      ),
      DuaCategory(
        id = "guidance_istikhara",
        title = "Guidance & Istikharah",
        arabicTitle = "الِاسْتِخَارَةُ وَطَلَبُ الْهِدَايَةِ",
        iconName = "compass",
        description = "Divine consultation for pivotal decisions and firmness of faith."
      )
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
      // ══════════════════════════════════════════════════════════════════════
      // 1. MORNING & EVENING (أَذْكَارُ الصَّبَاحِ وَالْمَسَاءِ)
      // ══════════════════════════════════════════════════════════════════════
      DuaItem(
        id = "morning_praise",
        categoryId = "morning_evening",
        title = "Morning Praise & Sovereignty",
        arabic = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ، رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَذَا الْيَوْمِ وَخَيْرَ مَا بَعْدَهُ، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هَذَا الْيَوْمِ وَشَرِّ مَا بَعْدَهُ، رَبِّ أَعُوذُ بِكَ مِنَ الْكَسَلِ وَسُوءِ الْكِبَرِ، رَبِّ أَعُوذُ بِكَ مِنْ عَذَابٍ فِي النَّارِ وَعَذَابٍ فِي الْقَبْرِ",
        transliteration = "Asbahna wa-asbahal-mulku lillah, wal-hamdu lillah, la ilaha illallahu wahdahu la sharika lah, lahul-mulku wa lahul-hamdu wa Huwa 'ala kulli shay'in Qadeer. Rabbi as'aluka khayra ma fi hadhal-yawmi wa khayra ma ba'dahu, wa a'udhu bika min sharri ma fi hadhal-yawmi wa sharri ma ba'dahu, Rabbi a'udhu bika minal-kasali wa soo'il-kibar, Rabbi a'udhu bika min 'adhabin fin-nari wa 'adhabin fil-qabr.",
        translation = "We have entered the morning and the kingdom belongs to Allah, and all praise is for Allah. None has the right to be worshipped except Allah alone, without partner. To Him belongs the sovereignty and praise, and He has power over all things. My Lord, I ask You for the good of this day and what comes after it, and I seek refuge in You from the evil of this day and what comes after it. My Lord, I seek refuge in You from laziness and the hardships of old age. My Lord, I seek refuge in You from punishment in the Fire and punishment in the grave.",
        reference = "Sahih Muslim 2723",
        benefit = "Grounds the heart in gratitude, divine sovereignty, and complete spiritual refuge at the break of dawn."
      ),
      DuaItem(
        id = "morning_bika_asbahna",
        categoryId = "morning_evening",
        title = "Affirmation of Life and Resurrection",
        arabic = "اللَّهُمَّ بِكَ أَصْبَحْنَا، وَبِكَ أَمْسَيْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ النُّشُورُ",
        transliteration = "Allahumma bika asbahna, wa bika amsayna, wa bika nahya, wa bika namootu, wa ilaykan-nushoor.",
        translation = "O Allah, by You we enter the morning, and by You we enter the evening, by You we live, and by You we die, and unto You is the final resurrection.",
        reference = "Jami` at-Tirmidhi 3391, Sunan Abi Dawood 5068",
        benefit = "Recited every morning to renew conscious awareness that existence, sustenance, and revival depend wholly on Allah."
      ),
      DuaItem(
        id = "morning_afiyah_protection",
        categoryId = "morning_evening",
        title = "Supplication for Well-Being & Protection from All Sides",
        arabic = "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَفْوَ وَالْعَافِيَةَ فِي الدُّنْيَا وَالْآخِرَةِ، اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَفْوَ وَالْعَافِيَةَ فِي دِينِي وَدُنْيَايَ وَأَهْلِي وَمَالِي، اللَّهُمَّ اسْتُرْ عَوْرَاتِي وَآمِنْ رَوْعَاتِي، اللَّهُمَّ احْفَظْنِي مِنْ بَيْنِ يَدَيَّ، وَمِنْ خَلْفِي، وَعَنْ يَمِينِي، وَعَنْ شِمَالِي، وَمِنْ فَوْقِي، وَأَعُوذُ بِعَظَمَتِكَ أَنْ أُغْتَالَ مِنْ تَحْتِي",
        transliteration = "Allahumma inni as'alukal-'afwa wal-'afiyata fid-dunya wal-akhirah. Allahumma inni as'alukal-'afwa wal-'afiyata fi deeni wa dunyaya wa ahli wa mali. Allahummastur 'awrati wa amin raw'ati. Allahummah-fazni min bayni yadayya, wa min khalfi, wa 'an yameeni, wa 'an shimali, wa min fawqi, wa a'udhu bi-'azamatika an ughtala min tahti.",
        translation = "O Allah, I ask You for pardon and well-being in this world and the Hereafter. O Allah, I ask You for pardon and well-being in my religion, my worldly affairs, my family, and my wealth. O Allah, conceal my faults and calm my fears. O Allah, guard me from before me, from behind me, on my right, on my left, and from above me; and I seek refuge in Your Greatness from being swallowed up from beneath me.",
        reference = "Sunan Abi Dawood 5074, Sunan Ibn Majah 3871",
        benefit = "The Messenger of Allah (pbuh) never neglected this comprehensive six-direction shield every morning and evening."
      ),
      DuaItem(
        id = "morning_contentment",
        categoryId = "morning_evening",
        title = "Contentment with Allah, Islam, and the Prophet",
        arabic = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا",
        transliteration = "Radeetu billahi Rabba, wa bil-Islami deena, wa bi-Muhammadin sallallahu 'alayhi wa sallama Nabiyya.",
        translation = "I am pleased with Allah as my Lord, with Islam as my religion, and with Muhammad (peace and blessings of Allah be upon him) as my Prophet.",
        reference = "Sunan Abi Dawood 5072, Jami` at-Tirmidhi 3389",
        benefit = "Whoever recites this thrice every morning and evening, it is a duty upon Allah to please him on the Day of Resurrection."
      ),
      DuaItem(
        id = "morning_ya_hayyu",
        categoryId = "morning_evening",
        title = "Plea to the Ever-Living for Total Guidance",
        arabic = "يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ، أَصْلِحْ لِي شَأْنِي كُلَّهُ، وَلَا تَكِلْنِي إِلَى نَفْسِي طَرْفَةَ عَيْنٍ",
        transliteration = "Ya Hayyu Ya Qayyumu bi-rahmatika astagheeth, aslih li sha'ni kullahu, wa la takilni ila nafsi tarfata 'ayn.",
        translation = "O Ever-Living, O Self-Subsisting, by Your mercy I seek assistance. Rectify for me all of my affairs, and do not leave me in charge of myself even for the blink of an eye.",
        reference = "Sunan an-Nasa'i (Al-Kubra 10405), Al-Hakim 1/545",
        benefit = "Relieves helplessness and guarantees divine custody over one's psychological and spiritual state."
      ),
      DuaItem(
        id = "morning_hasbiyallah",
        categoryId = "morning_evening",
        title = "Sufficiency of Allah (Hasbiyallahu)",
        arabic = "حَسْبِيَ اللَّهُ لَا إِلَهَ إِلَّا هُوَ عَلَيْهِ تَوَكَّلْتُ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ",
        transliteration = "Hasbiyallahu la ilaha illa Huwa, 'alayhi tawakkaltu wa Huwa Rabbul-'Arshil-'Azeem.",
        translation = "Allah is sufficient for me; none has the right to be worshipped but He. In Him I put my trust, and He is the Lord of the Mighty Throne.",
        reference = "Surah At-Tawbah 9:129, Sunan Abi Dawood 5081",
        benefit = "Whoever recites this seven times in the morning and evening, Allah will suffice him for all that concerns him."
      ),
      DuaItem(
        id = "morning_bismillahil_ladhi",
        categoryId = "morning_evening",
        title = "Absolute Shield Against Calamity",
        arabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
        transliteration = "Bismillahil-ladhi la yadurru ma'as-mihi shay'un fil-ardi wa la fis-sama'i wa Huwas-Sami'ul-'Aleem.",
        translation = "In the Name of Allah, with Whose Name nothing can cause harm in the earth nor in the heavens, and He is the All-Hearing, the All-Knowing.",
        reference = "Sunan Abi Dawood 5088, Jami` at-Tirmidhi 3388",
        benefit = "Recited three times morning and evening; no sudden affliction or venomous harm will touch the servant."
      ),
      DuaItem(
        id = "morning_sayyid_istighfar_entry",
        categoryId = "morning_evening",
        title = "Sayyid al-Istighfar in the Morning",
        arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
        transliteration = "Allahumma Anta Rabbi la ilaha illa Anta, khalaqtani wa ana 'abduka, wa ana 'ala 'ahdika wa wa'dika mastata'tu, a'udhu bika min sharri ma sana'tu, abu'u laka bi-ni'matika 'alayya, wa abu'u laka bi-dhanbi faghfir li fa-innahu la yaghfirudh-dhunooba illa Anta.",
        translation = "O Allah, You are my Lord, none has the right to be worshipped but You. You created me and I am Your servant, and I abide by Your covenant and promise as best I can. I seek refuge in You from the evil of what I have done. I acknowledge Your favor upon me, and I acknowledge my sin, so forgive me, for none forgives sins except You.",
        reference = "Sahih al-Bukhari 6306",
        benefit = "If recited with conviction in the morning and one passes away that day, he enters Paradise."
      ),

      // ══════════════════════════════════════════════════════════════════════
      // 2. PRAYER & AFTER SALAH (أَدْعِيَةُ الصَّلَاةِ وَمَا بَعْدَهَا)
      // ══════════════════════════════════════════════════════════════════════
      DuaItem(
        id = "prayer_istiftah",
        categoryId = "prayer",
        title = "Opening Dua of Salah (Du'a al-Istiftah)",
        arabic = "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ، وَتَبَارَكَ اسْمُكَ، وَتَعَالَى جَدُّكَ، وَلَا إِلَهَ غَيْرُكَ",
        transliteration = "Subhanaka Allahumma wa bihamdika, wa tabarakasmuka, wa ta'ala jadduka, wa la ilaha ghayruk.",
        translation = "Glory is to You, O Allah, and praise. Blessed is Your Name and exalted is Your Majesty, and there is none worthy of worship other than You.",
        reference = "Sunan Abi Dawood 776, Jami` at-Tirmidhi 243",
        benefit = "Recited after the opening Takbeer and before reciting Surah Al-Fatihah in obligatory and voluntary prayers."
      ),
      DuaItem(
        id = "prayer_ruku",
        categoryId = "prayer",
        title = "Supplication in Ruku (Bowing)",
        arabic = "سُبْحَانَ رَبِّيَ الْعَظِيمِ",
        transliteration = "Subhana Rabbiyal-'Azeem.",
        translation = "Glory be to my Lord, the Almighty.",
        reference = "Sahih Muslim 772",
        benefit = "Recited three times (or more) while bowing in profound reverence to Allah's Grandeur."
      ),
      DuaItem(
        id = "prayer_rising_ruku",
        categoryId = "prayer",
        title = "Praise Upon Rising from Bowing",
        arabic = "رَبَّنَا وَلَكَ الْحَمْدُ حَمْدًا كَثِيرًا طَيِّبًا مُبَارَكًا فِيهِ",
        transliteration = "Rabbana wa lakal-hamdu hamdan katheeran tayyiban mubarakan feeh.",
        translation = "Our Lord, to You belongs all praise, an abundant, beautiful, and blessed praise.",
        reference = "Sahih al-Bukhari 799",
        benefit = "The Prophet (pbuh) saw over thirty angels racing to be the first to write down this magnificent praise."
      ),
      DuaItem(
        id = "prayer_sujood",
        categoryId = "prayer",
        title = "Supplication in Sujood (Prostration)",
        arabic = "سُبْحَانَ رَبِّيَ الْأَعْلَى",
        transliteration = "Subhana Rabbiyal-A'la.",
        translation = "Glory be to my Lord, the Most High.",
        reference = "Sahih Muslim 772",
        benefit = "Recited three times (or more) in prostration, the closest a servant can ever be to his Creator."
      ),
      DuaItem(
        id = "prayer_sujood_forgiveness",
        categoryId = "prayer",
        title = "Supplication for Total Forgiveness in Sujood",
        arabic = "اللَّهُمَّ اغْفِرْ لِي ذَنْبِي كُلَّهُ، دِقَّهُ وَجِلَّهُ، وَأَوَّلَهُ وَآخِرَهُ، وَعَلَانِيَتَهُ وَسِرَّهُ",
        transliteration = "Allahummagh-fir li dhanbi kullahu, diqqahu wa jillahu, wa awwalahu wa akhirahu, wa 'alaniyatahu wa sirrahu.",
        translation = "O Allah, forgive all my sins, the small and the great, the first and the last, the open and the secret.",
        reference = "Sahih Muslim 483",
        benefit = "A comprehensive Prophetic supplication in prostration cleansing every category of transgression."
      ),
      DuaItem(
        id = "prayer_between_sujood",
        categoryId = "prayer",
        title = "Dua Between the Two Prostrations (Jalsah)",
        arabic = "رَبِّ اغْفِرْ لِي، رَبِّ اغْفِرْ لِي، اللَّهُمَّ اغْفِرْ لِي وَارْحَمْنِي وَعَافِنِي وَاهْدِنِي وَارْزُقْنِي",
        transliteration = "Rabbigh-fir li, Rabbigh-fir li. Allahummagh-fir li warhamni wa 'afini wahdini warzuqni.",
        translation = "My Lord forgive me, my Lord forgive me. O Allah, forgive me, have mercy on me, grant me well-being, guide me, and provide for me.",
        reference = "Sunan Abi Dawood 850, Sunan Ibn Majah 898",
        benefit = "Encompasses spiritual forgiveness, bodily well-being, divine guidance, and lawful provision."
      ),
      DuaItem(
        id = "prayer_before_tasleem",
        categoryId = "prayer",
        title = "Protection from Four Evils Before Tasleem",
        arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ عَذَابِ جَهَنَّمَ، وَمِنْ عَذَابِ الْقَبْرِ، وَمِنْ فِتْنَةِ الْمَحْيَا وَالْمَمَاتِ، وَمِنْ شَرِّ فِتْنَةِ الْمَسِيحِ الدَّجَّالِ",
        transliteration = "Allahumma inni a'udhu bika min 'adhabi jahannam, wa min 'adhabil-qabr, wa min fitnatil-mahya wal-mamat, wa min sharri fitnatil-maseehid-dajjal.",
        translation = "O Allah, I seek refuge in You from the punishment of Hellfire, from the punishment of the grave, from the trials of life and death, and from the evil trial of the False Messiah (Dajjal).",
        reference = "Sahih al-Bukhari 1377, Sahih Muslim 588",
        benefit = "The Prophet (pbuh) strongly enjoined reciting this after the final Tashahhud before concluding prayer."
      ),
      DuaItem(
        id = "prayer_after_tasleem",
        categoryId = "prayer",
        title = "Supplication Immediately After Tasleem",
        arabic = "أَسْتَغْفِرُ اللَّهَ، أَسْتَغْفِرُ اللَّهَ، أَسْتَغْفِرُ اللَّهَ، اللَّهُمَّ أَنْتَ السَّلَامُ وَمِنْكَ السَّلَامُ، تَبَارَكْتَ يَا ذَا الْجَلَالِ وَالْإِكْرَامِ",
        transliteration = "Astaghfirullah, Astaghfirullah, Astaghfirullah. Allahumma Antas-Salamu wa minkas-salam, tabarakta ya Dhal-Jalali wal-Ikram.",
        translation = "I ask Allah for forgiveness (three times). O Allah, You are Peace and from You comes peace. Blessed are You, O Owner of Majesty and Honor.",
        reference = "Sahih Muslim 591",
        benefit = "The first Sunnah Dhikr uttered upon completing every obligatory prayer."
      ),
      DuaItem(
        id = "prayer_muadh_dhikr",
        categoryId = "prayer",
        title = "Supplication for Assistance in Worship (Mu'adh)",
        arabic = "اللَّهُمَّ أَعِنِّي عَلَى ذِكْرِكَ وَشُكْرِكَ وَحُسْنِ عِبَادَتِكَ",
        transliteration = "Allahumma a'inni 'ala dhikrika wa shukrika wa husni 'ibadatik.",
        translation = "O Allah, assist me in remembering You, expressing gratitude to You, and worshipping You in the best manner.",
        reference = "Sunan Abi Dawood 1522, Sunan an-Nasa'i 1303",
        benefit = "The Prophet (pbuh) held Mu'adh ibn Jabal's hand with love and advised him never to omit this after any prayer."
      ),

      // ══════════════════════════════════════════════════════════════════════
      // 3. PROTECTION & REFUGE (أَدْعِيَةُ الْحِفْظِ وَالِاسْتِعَاذَةِ)
      // ══════════════════════════════════════════════════════════════════════
      DuaItem(
        id = "protection_ayat_kursi",
        categoryId = "protection",
        title = "Ayat al-Kursi (The Throne Verse)",
        arabic = "اللَّهُ لَا إِلَهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ مَنْ ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلَّا بِإِذْنِهِ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ وَلَا يُحِيطُونَ بِشَيْءٍ مِنْ عِلْمِهِ إِلَّا بِمَا شَاءَ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ وَلَا يَئُودُهُ حِفْظُهُمَا وَهُوَ الْعَلِيُّ الْعَظِيمُ",
        transliteration = "Allahu la ilaha illa Huwal-Hayyul-Qayyum, la ta'khudhuhu sinatun wa la nawm, lahu ma fis-samawati wa ma fil-ard, man dhal-ladhi yashfa'u 'indahu illa bi-idhnih, ya'lamu ma bayna aydihim wa ma khalfahum, wa la yuheetoona bi-shay'im-min 'ilmihi illa bima sha'a, wasi'a kursiyyuhus-samawati wal-ard, wa la ya'ooduhu hifzuhuma, wa Huwal-'Aliyyul-'Azeem.",
        translation = "Allah! None has the right to be worshipped but He, the Ever-Living, the All-Sustaining. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth. Who is it that can intercede with Him except by His permission? He knows what is before them and what will be after them, and they encompass not a thing of His knowledge except for what He wills. His Kursi extends over the heavens and the earth, and their preservation tires Him not. And He is the Most High, the Most Great.",
        reference = "Surah Al-Baqarah 2:255, Sahih al-Bukhari 2311",
        benefit = "The greatest verse in the Quran. Whoever recites it after each prayer, nothing stands between him and Paradise except death."
      ),
      DuaItem(
        id = "protection_evil_eye_children",
        categoryId = "protection",
        title = "Seeking Refuge from the Evil Eye and Devils",
        arabic = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّةِ مِنْ كُلِّ شَيْطَانٍ وَهَامَّةٍ، وَمِنْ كُلِّ عَيْنٍ لَامَّةٍ",
        transliteration = "A'udhu bi-kalimatil-lahit-tammati min kulli shaytanin wa hammah, wa min kulli 'aynin lammah.",
        translation = "I seek refuge in the perfect words of Allah from every devil and poisonous reptile, and from every envious, harmful eye.",
        reference = "Sahih al-Bukhari 3371",
        benefit = "The supplication Prophet Ibrahim recited over Ishaaq and Isma'il, and Prophet Muhammad (pbuh) recited over Al-Hasan and Al-Husain."
      ),
      DuaItem(
        id = "protection_three_times",
        categoryId = "protection",
        title = "Protection in the Name of Allah",
        arabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
        transliteration = "Bismillahil-ladhi la yadurru ma'as-mihi shay'un fil-ardi wa la fis-sama'i wa Huwas-Sami'ul-'Aleem.",
        translation = "In the Name of Allah, with Whose Name nothing can cause harm in the earth nor in the heavens, and He is the All-Hearing, the All-Knowing.",
        reference = "Sunan Abi Dawood 5088, Jami` at-Tirmidhi 3388",
        benefit = "Shields the believer completely from harm, toxins, and unexpected calamity when recited thrice."
      ),
      DuaItem(
        id = "protection_words_of_allah",
        categoryId = "protection",
        title = "Seeking Refuge in the Perfect Words of Allah",
        arabic = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
        transliteration = "A'udhu bi-kalimatil-lahit-tammati min sharri ma khalaq.",
        translation = "I seek refuge in the perfect words of Allah from the evil of what He has created.",
        reference = "Sahih Muslim 2709",
        benefit = "Whoever stops at any dwelling and recites this, nothing will harm him until he departs from that place."
      ),
      DuaItem(
        id = "protection_leaving_home",
        categoryId = "protection",
        title = "Dua for Stepping Out of the Home",
        arabic = "بِسْمِ اللَّهِ تَوَكَّلْتُ عَلَى اللَّهِ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
        transliteration = "Bismillahi tawakkaltu 'alallahi, wa la hawla wa la quwwata illa billah.",
        translation = "In the Name of Allah, I place my trust in Allah; there is no might nor power except with Allah.",
        reference = "Sunan Abi Dawood 5095, Jami` at-Tirmidhi 3426",
        benefit = "The angels say to the believer: 'You have been guided, defended, and protected,' and Satan moves away from his path."
      ),
      DuaItem(
        id = "protection_evil_companions",
        categoryId = "protection",
        title = "Refuge from Destructive Neighbors and Treacherous Friends",
        arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ جَارِ السُّوءِ، وَمِنْ زَوْجٍ تُشَيِّبُنِي قَبْلَ الْمَشِيبِ، وَمِنْ وَلَدٍ يَكُونُ عَلَيَّ رَبًّا، وَمِنْ مَالٍ يَكُونُ عَلَيَّ عَذَابًا، وَمِنْ خَلِيلٍ مَاكِرٍ عَيْنُهُ تَرَانِي وَقَلْبُهُ يَرْعَانِي، إِنْ رَأَى حَسَنَةً دَفَنَهَا، وَإِنْ رَأَى سَيِّئَةً أَذَاعَهَا",
        transliteration = "Allahumma inni a'udhu bika min jaris-soo', wa min zawjin tushayyibuni qablal-masheeb, wa min waladin yakoonu 'alayya rabba, wa min malin yakoonu 'alayya 'adhaba, wa min khaleelin makin 'aynuhu tarani wa qalbuhu yar'ani, in ra'a hasanatan dafanaha, wa in ra'a sayyi'atan adha'aha.",
        translation = "O Allah, I seek refuge in You from an evil neighbor, from a companion who turns my hair grey before old age, from a child who becomes a master over me, from wealth that becomes a torment for me, and from a deceitful friend whose eyes watch me and whose heart observes me: if he sees something good he conceals it, and if he sees something bad he broadcasts it.",
        reference = "At-Tabarani (Al-Kabeer 1438), Sahih al-Jami' 1304",
        benefit = "Guards against toxic social relationships, slander, domestic torment, and treacherous acquaintances."
      ),

      // ══════════════════════════════════════════════════════════════════════
      // 4. FORGIVENESS & REPENTANCE (الِاسْتِغْفَارُ وَالتَّوْبَةُ)
      // ══════════════════════════════════════════════════════════════════════
      DuaItem(
        id = "sayyid_istighfar",
        categoryId = "forgiveness",
        title = "Sayyid al-Istighfar (The Master of Forgiveness)",
        arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
        transliteration = "Allahumma Anta Rabbi la ilaha illa Anta, khalaqtani wa ana 'abduka, wa ana 'ala 'ahdika wa wa'dika mastata'tu, a'udhu bika min sharri ma sana'tu, abu'u laka bi-ni'matika 'alayya, wa abu'u laka bi-dhanbi faghfir li fa-innahu la yaghfirudh-dhunooba illa Anta.",
        translation = "O Allah, You are my Lord, none has the right to be worshipped but You. You created me and I am Your servant, and I abide by Your covenant and promise as best I can. I seek refuge in You from the evil of what I have done. I acknowledge Your favor upon me, and I acknowledge my sin, so forgive me, for none forgives sins except You.",
        reference = "Sahih al-Bukhari 6306",
        benefit = "The most exalted formula of repentance. Guarantees Paradise for whoever recites it with sincere conviction."
      ),
      DuaItem(
        id = "forgiveness_yunus",
        categoryId = "forgiveness",
        title = "Dua of Prophet Yunus (Dhun-Noon) in the Whale",
        arabic = "لَا إِلَهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ",
        transliteration = "La ilaha illa Anta subhanaka inni kuntu minaz-zalimeen.",
        translation = "There is none worthy of worship except You, glory be to You! Truly, I have been among the wrongdoers.",
        reference = "Surah Al-Anbiya 21:87, Jami` at-Tirmidhi 3505",
        benefit = "No believer ever calls upon Allah with this supplication during any hardship or affliction except that Allah relieves him."
      ),
      DuaItem(
        id = "forgiveness_adam_hawwa",
        categoryId = "forgiveness",
        title = "Dua of Adam & Hawwa for Sincere Repentance",
        arabic = "رَبَّنَا ظَلَمْنَا أَنْفُسَنَا وَإِنْ لَمْ تَغْفِرْ لَنَا وَتَرْحَمْنَا لَنَكُونَنَّ مِنَ الْخَاسِرِينَ",
        transliteration = "Rabbana zalamna anfusana wa il-lam taghfir lana wa tarhamna lanakoonanna minal-khasireen.",
        translation = "Our Lord! We have wronged ourselves. If You do not forgive us and have mercy upon us, we shall certainly be of the losers.",
        reference = "Surah Al-A'raf 7:23",
        benefit = "The primary words of contrition taught by Allah to Adam and Hawwa when seeking His forgiveness."
      ),
      DuaItem(
        id = "forgiveness_abu_bakr",
        categoryId = "forgiveness",
        title = "Supplication Taught to Abu Bakr as-Siddiq",
        arabic = "اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا، وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ، فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي، إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ",
        transliteration = "Allahumma inni zalamtu nafsi zulman katheeran, wa la yaghfirudh-dhunooba illa Anta, faghfir li maghfiratan min 'indika warhamni, innaka Antal-Ghafoorur-Raheem.",
        translation = "O Allah, I have wronged my soul greatly, and none can forgive sins except You. So grant me forgiveness from Your Presence, and have mercy upon me; indeed You are the Oft-Forgiving, the Most Merciful.",
        reference = "Sahih al-Bukhari 834, Sahih Muslim 2705",
        benefit = "Taught directly by the Messenger of Allah (pbuh) to Abu Bakr to be recited in his prayers."
      ),
      DuaItem(
        id = "forgiveness_general_cleansing",
        categoryId = "forgiveness",
        title = "Istighfar for Complete Erasure of Past Sins",
        arabic = "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ الَّذِي لَا إِلَهَ إِلَّا هُوَ الْحَيَّ الْقَيُّومَ وَأَتُوبُ إِلَيْهِ",
        transliteration = "Astaghfirullahal-'Azeemal-ladhi la ilaha illa Huwal-Hayyul-Qayyumu wa atoobu ilayh.",
        translation = "I seek forgiveness from Allah the Almighty, besides Whom there is no deity, the Ever-Living, the Sustainer, and I repent unto Him.",
        reference = "Sunan Abi Dawood 1517, Jami` at-Tirmidhi 3577",
        benefit = "Whoever recites this, his sins will be forgiven even if he had fled from the advance of an army."
      ),
      DuaItem(
        id = "forgiveness_kaffaratul_majlis",
        categoryId = "forgiveness",
        title = "Expiation for Sins of Assemblies (Kaffaratul-Majlis)",
        arabic = "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ، أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا أَنْتَ، أَسْتَغْفِرُكَ وَأَتُوبُ إِلَيْكَ",
        transliteration = "Subhanaka Allahumma wa bihamdika, ash-hadu al-la ilaha illa Anta, astaghfiruka wa atoobu ilayk.",
        translation = "Glory is to You, O Allah, and praise. I bear witness that there is no deity worthy of worship except You. I seek Your forgiveness and repent unto You.",
        reference = "Jami` at-Tirmidhi 3433, Sunan Abi Dawood 4859",
        benefit = "Expiates every idle word, oversight, or idle chatter that occurred during any sitting or gathering."
      ),

      // ══════════════════════════════════════════════════════════════════════
      // 5. ANXIETY & HARDSHIP (تَفْرِيجُ الْهَمِّ وَالْكَرْبِ)
      // ══════════════════════════════════════════════════════════════════════
      DuaItem(
        id = "anxiety_relief",
        categoryId = "anxiety",
        title = "Dua for Relief from Distress, Laziness, & Debt",
        arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ وَغَلَبَةِ الرِّجَالِ",
        transliteration = "Allahumma inni a'udhu bika minal-hammi wal-hazani, wal-'ajzi wal-kasali, wal-bukhli wal-jubni, wa dala'id-dayni wa ghalabatir-rijal.",
        translation = "O Allah, I seek refuge in You from anxiety and grief, from incapacity and laziness, from cowardice and miserliness, from the burden of debt, and from being overpowered by men.",
        reference = "Sahih al-Bukhari 2893",
        benefit = "Comprehensive supplication for emotional tranquility, productivity, financial freedom, and dignity."
      ),
      DuaItem(
        id = "anxiety_karb",
        categoryId = "anxiety",
        title = "Dua of Severe Calamity (Du'a al-Karb)",
        arabic = "لَا إِلَهَ إِلَّا اللَّهُ الْعَظِيمُ الْحَلِيمُ، لَا إِلَهَ إِلَّا اللَّهُ رَبُّ الْعَرْشِ الْعَظِيمِ، لَا إِلَهَ إِلَّا اللَّهُ رَبُّ السَّمَاوَاتِ وَرَبُّ الْأَرْضِ وَرَبُّ الْعَرْشِ الْكَرِيمِ",
        transliteration = "La ilaha illallahul-'Azeemul-Haleem, la ilaha illallahu Rabbul-'Arshil-'Azeem, la ilaha illallahu Rabbus-samawati wa Rabbul-ardi wa Rabbul-'Arshil-Kareem.",
        translation = "There is no deity worthy of worship but Allah, the Tremendous, the Forbearing. None has the right to be worshipped but Allah, Lord of the Magnificent Throne. None has the right to be worshipped but Allah, Lord of the heavens and Lord of the earth, and Lord of the Noble Throne.",
        reference = "Sahih al-Bukhari 6346, Sahih Muslim 2730",
        benefit = "Recited by the Prophet (pbuh) whenever intense psychological distress or crisis befell him."
      ),
      DuaItem(
        id = "anxiety_making_hard_easy",
        categoryId = "anxiety",
        title = "Supplication When Something Becomes Difficult",
        arabic = "اللَّهُمَّ لَا سَهْلَ إِلَّا مَا جَعَلْتَهُ سَهْلًا، وَأَنْتَ تَجْعَلُ الْحَزْنَ إِذَا شِئْتَ سَهْلًا",
        transliteration = "Allahumma la sahla illa ma ja'altahu sahla, wa Anta taj'alul-hazna idha shi'ta sahla.",
        translation = "O Allah, nothing is easy except that which You make easy, and You make hardship easy if You will.",
        reference = "Sahih Ibn Hibban 974, Al-Bayhaqi",
        benefit = "Recited before challenging tests, complicated tasks, tough interviews, and complex hurdles."
      ),
      DuaItem(
        id = "anxiety_debt_settlement",
        categoryId = "anxiety",
        title = "Supplication for Freedom from Debt",
        arabic = "اللَّهُمَّ اكْفِنِي بِحَلَالِكَ عَنْ حَرَامِكَ، وَأَغْنِنِي بِفَضْلِكَ عَمَّنْ سِوَاكَ",
        transliteration = "Allahummak-fini bi-halalika 'an haramik, wa aghnini bi-fadlika 'amman siwak.",
        translation = "O Allah, suffice me with what is lawful against what is unlawful, and enrich me by Your grace from needing anyone besides You.",
        reference = "Jami` at-Tirmidhi 3563",
        benefit = "Ali (ra) said: 'Even if your debt were like the mountain of Seer, Allah would pay it off on your behalf.'"
      ),
      DuaItem(
        id = "anxiety_rahmataka_arju",
        categoryId = "anxiety",
        title = "Plea for Divine Mercy in All Affairs",
        arabic = "اللَّهُمَّ رَحْمَتَكَ أَرْجُو فَلَا تَكِلْنِي إِلَى نَفْسِي طَرْفَةَ عَيْنٍ، وَأَصْلِحْ لِي شَأْنِي كُلَّهُ، لَا إِلَهَ إِلَّا أَنْتَ",
        transliteration = "Allahumma rahmataka arjoo fala takilni ila nafsi tarfata 'ayn, wa aslih li sha'ni kullahu, la ilaha illa Anta.",
        translation = "O Allah, Your mercy I hope for, so do not leave me to myself even for the blink of an eye. Rectify for me all of my affairs; there is no deity worthy of worship except You.",
        reference = "Sunan Abi Dawood 5090, Musnad Ahmad 5/42",
        benefit = "The supplication of the distressed; shields the heart from helplessness, despair, and depression."
      ),
      DuaItem(
        id = "anxiety_in_affliction",
        categoryId = "anxiety",
        title = "Supplication Upon Hearing Troubling News or Loss",
        arabic = "إِنَّا لِلَّهِ وَإِنَّا إِلَيْهِ رَاجِعُونَ، اللَّهُمَّ أْجُرْنِي فِي مُصِيبَتِي وَأَخْلِفْ لِي خَيْرًا مِنْهَا",
        transliteration = "Inna lillahi wa inna ilayhi raji'oon, Allahumma'-jurni fi museebati wa akhlif li khayran minha.",
        translation = "Truly to Allah we belong and truly to Him we shall return. O Allah, reward me in my affliction and grant me in return something better than it.",
        reference = "Sahih Muslim 918",
        benefit = "Umm Salamah recited this upon the death of Abu Salamah, and Allah granted her the Prophet Muhammad (pbuh) in marriage."
      ),

      // ══════════════════════════════════════════════════════════════════════
      // 6. SLEEP & NIGHT (أَذْكَارُ النَّوْمِ وَالِاسْتِيقَاظِ)
      // ══════════════════════════════════════════════════════════════════════
      DuaItem(
        id = "sleep_dua",
        categoryId = "sleep",
        title = "Dua When Retiring to Bed",
        arabic = "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
        transliteration = "Bismika Allahumma amootu wa ahya.",
        translation = "In Your Name, O Allah, I die and I live.",
        reference = "Sahih al-Bukhari 6324",
        benefit = "Entrusts the soul to Allah before falling into slumber, which is the minor death."
      ),
      DuaItem(
        id = "sleep_side_dusting",
        categoryId = "sleep",
        title = "Dua of Laying on the Right Side",
        arabic = "بِاسْمِكَ رَبِّي وَضَعْتُ جَنْبِي وَبِكَ أَرْفَعُهُ، إِنْ أَمْسَكْتَ نَفْسِي فَارْحَمْهَا، وَإِنْ أَرْسَلْتَهَا فَاحْفَظْهَا بِمَا تَحْفَظُ بِهِ عِبَادَكَ الصَّالِحِينَ",
        transliteration = "Bismika Rabbi wada'tu janbi wa bika arfa'uh, in amsakta nafsi farhamha, wa in arsaltaha fahfazha bima tahfazu bihi 'ibadakas-saliheen.",
        translation = "In Your Name, my Lord, I lay down my side and by Your Name I raise it. If You take my soul, have mercy on it, and if You send it back, guard it as You guard Your righteous servants.",
        reference = "Sahih al-Bukhari 6320, Sahih Muslim 2714",
        benefit = "The Sunnah prayer upon dusting the mattress three times before sleeping on the right side."
      ),
      DuaItem(
        id = "sleep_fitrah_surrender",
        categoryId = "sleep",
        title = "Complete Surrender of the Soul (Dua on Fitrah)",
        arabic = "اللَّهُمَّ أَسْلَمْتُ نَفْسِي إِلَيْكَ، وَفَوَّضْتُ أَمْرِي إِلَيْكَ، وَوَجَّهْتُ وَجْهِي إِلَيْكَ، وَأَلْجَأْتُ ظَهْرِي إِلَيْكَ، رَغْبَةً وَرَهْبَةً إِلَيْكَ، لَا مَلْجَأَ وَلَا مَنْجَا مِنْكَ إِلَّا إِلَيْكَ، آمَنْتُ بِكِتَابِكَ الَّذِي أَنْزَلْتَ، وَبِنَبِيِّكَ الَّذِي أَرْسَلْتَ",
        transliteration = "Allahumma aslamtu nafsi ilayk, wa fawwadtu amri ilayk, wa wajjahtu wajhi ilayk, wa alja'tu zahri ilayk, raghbatan wa rahbatan ilayk, la malja'a wa la manja minka illa ilayk, amantu bi-kitabikal-ladhi anzalt, wa bi-nabiyyikal-ladhi arsalt.",
        translation = "O Allah, I have surrendered my soul to You, entrusted my affairs to You, turned my face to You, and backed myself to You out of longing and awe of You. There is no refuge nor escape from You except to You. I believe in Your Book which You revealed, and in Your Prophet whom You sent.",
        reference = "Sahih al-Bukhari 6313, Sahih Muslim 2710",
        benefit = "The Prophet (pbuh) instructed that these should be one's very last words before sleep; whoever dies that night dies upon pure faith."
      ),
      DuaItem(
        id = "sleep_waking_up",
        categoryId = "sleep",
        title = "Supplication Upon Awakening",
        arabic = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
        transliteration = "Alhamdu lillahil-ladhi ahyana ba'da ma amatana wa ilayhin-nushoor.",
        translation = "All praise is for Allah, Who brought us to life after causing us to die, and unto Him is the final resurrection.",
        reference = "Sahih al-Bukhari 6312, Sahih Muslim 2711",
        benefit = "First conscious breath of gratitude welcoming a new day of life and opportunity for good deeds."
      ),
      DuaItem(
        id = "sleep_turning_restless",
        categoryId = "sleep",
        title = "Supplication When Turning Over or Waking Restless",
        arabic = "لَا إِلَهَ إِلَّا اللَّهُ الْوَاحِدُ الْقَهَّارُ، رَبُّ السَّمَاوَاتِ وَالْأَرْضِ وَمَا بَيْنَهُمَا الْعَزِيزُ الْغَفَّارُ",
        transliteration = "La ilaha illallahul-Wahidul-Qahhar, Rabbus-samawati wal-ardi wa ma baynahumal-'Azeezul-Ghaffar.",
        translation = "There is no deity worthy of worship except Allah, the One, the All-Compelling, Lord of the heavens and the earth and whatever is between them, the Exalted in Might, the Perpetual Forgiver.",
        reference = "Sunan an-Nasa'i (Al-Kubra 10630), Al-Hakim 1/540",
        benefit = "Recited by the Prophet (pbuh) whenever he woke up and turned during the night."
      ),

      // ══════════════════════════════════════════════════════════════════════
      // 7. TRAVEL & JOURNEY (دُعَاءُ السَّفَرِ وَالرُّكُوبِ)
      // ══════════════════════════════════════════════════════════════════════
      DuaItem(
        id = "travel_dua",
        categoryId = "travel",
        title = "Dua for Undertaking a Journey (Mounting Vehicle)",
        arabic = "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ، وَإِنَّا إِلَى رَبِّنَا لَمُنْقَلِبُونَ",
        transliteration = "Subhanal-ladhi sakh-khara lana hadha wa ma kunna lahu muqrinin, wa inna ila Rabbina lamunqaliboon.",
        translation = "Glory to Him who has brought this under our control though we were unable to subdue it by ourselves, and to our Lord surely we will return.",
        reference = "Surah Az-Zukhruf 43:13-14",
        benefit = "Recited when boarding any vehicle, car, plane, train, or vessel for a journey."
      ),
      DuaItem(
        id = "travel_complete_journey",
        categoryId = "travel",
        title = "Complete Travel Supplication for Protection",
        arabic = "اللَّهُمَّ إِنَّا نَسْأَلُكَ فِي سَفَرِنَا هَذَا الْبِرَّ وَالتَّقْوَى، وَمِنَ الْعَمَلِ مَا تَرْضَى، اللَّهُمَّ هَوِّنْ عَلَيْنَا سَفَرَنَا هَذَا وَاطْوِ عَنَّا بُعْدَهُ، اللَّهُمَّ أَنْتَ الصَّاحِبُ فِي السَّفَرِ، وَالْخَلِيفَةُ فِي الْأَهْلِ، اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ وَعْثَاءِ السَّفَرِ، وَكَآبَةِ الْمَنْظَرِ، وَسُوءِ الْمُنْقَلَبِ فِي الْمَالِ وَالْأَهْلِ",
        transliteration = "Allahumma inna nas'aluka fi safarina hadhal-birra wat-taqwa, wa minal-'amali ma tarda. Allahumma hawwin 'alayna safarana hadha watwi 'anna bu'dah. Allahumma Antas-sahibu fis-safar, wal-khaleefatu fil-ahl. Allahumma inni a'udhu bika min wa'tha'is-safar, wa ka'abatil-manzar, wa soo'il-munqalabi fil-mali wal-ahl.",
        translation = "O Allah, we ask You on this journey for righteousness and piety, and deeds that please You. O Allah, make this journey easy for us and roll up its distance. O Allah, You are the Companion in travel and the Guardian of the family. O Allah, I seek refuge in You from the hardships of travel, sorrowful sights, and an evil return regarding wealth and family.",
        reference = "Sahih Muslim 1342",
        benefit = "Envelopes the traveler, vehicle, luggage, and loved ones left behind in divine protection."
      ),
      DuaItem(
        id = "travel_entrusting_resident",
        categoryId = "travel",
        title = "Dua of Traveler Entrusting Those Staying Behind",
        arabic = "أَسْتَوْدِعُكُمُ اللَّهَ الَّذِي لَا تَضِيعُ وَدَائِعُهُ",
        transliteration = "Astawdi'ukumullahal-ladhi la tadee'u wada'i'uh.",
        translation = "I entrust you to Allah, Whose deposits are never lost.",
        reference = "Sunan Ibn Majah 2825, Musnad Ahmad 8668",
        benefit = "Whatever is entrusted to the custody of the Almighty is preserved from destruction and harm."
      ),
      DuaItem(
        id = "travel_return_home",
        categoryId = "travel",
        title = "Supplication Upon Safe Return from Journey",
        arabic = "آيِبُونَ تَائِبُونَ عَابِدُونَ لِرَبِّنَا حَامِدُونَ",
        transliteration = "Ayiboona ta'iboona 'abidoona li-Rabbina hamidoon.",
        translation = "We return, repentant, worshipping, and praising our Lord.",
        reference = "Sahih al-Bukhari 1797, Sahih Muslim 1342",
        benefit = "Recited upon arriving back safely at one's home city or residence after travel."
      ),
      DuaItem(
        id = "travel_entering_town",
        categoryId = "travel",
        title = "Dua When Entering a New Town or Destination",
        arabic = "اللَّهُمَّ رَبَّ السَّمَاوَاتِ السَّبْعِ وَمَا أَظْلَلْنَ، وَرَبَّ الْأَرَضِينَ السَّبْعِ وَمَا أَقْلَلْنَ، أَسْأَلُكَ خَيْرَ هَذِهِ الْقَرْيَةِ وَخَيْرَ أَهْلِهَا وَخَيْرَ مَا فِيهَا، وَأَعُوذُ بِكَ مِنْ شَرِّهَا وَشَرِّ أَهْلِهَا وَشَرِّ مَا فِيهَا",
        transliteration = "Allahumma Rabbas-samawatis-sab'i wa ma azlalna, wa Rabbal-aradeenas-sab'i wa ma aqlalna, as'aluka khayra hadhihil-qaryati wa khayra ahliha wa khayra ma feeha, wa a'udhu bika min sharriha wa sharri ahliha wa sharri ma feeha.",
        translation = "O Allah, Lord of the seven heavens and what they overshadow, Lord of the seven earths and what they carry, I ask You for the good of this town, the good of its people, and the good of what is in it; and I seek refuge in You from its evil, the evil of its people, and the evil of what is in it.",
        reference = "Sunan an-Nasa'i (Al-Kubra 8770), Al-Hakim 2/100",
        benefit = "Safeguards the traveler from foreign illnesses, dishonest inhabitants, and misfortunes in the new destination."
      ),

      // ══════════════════════════════════════════════════════════════════════
      // 8. DAILY LIFE & FOOD (الْحَيَاةُ الْيَوْمِيَّةُ وَالطَّعَامُ)
      // ══════════════════════════════════════════════════════════════════════
      DuaItem(
        id = "food_before_eating",
        categoryId = "daily_life",
        title = "Dua Before Eating & Drinking",
        arabic = "بِسْمِ اللَّهِ، اللَّهُمَّ بَارِكْ لَنَا فِيمَا رَزَقْتَنَا وَقِنَا عَذَابَ النَّارِ",
        transliteration = "Bismillah, Allahumma barik lana feema razaqtana wa qina 'adhaban-nar.",
        translation = "In the Name of Allah. O Allah, bless for us that which You have provided us, and save us from the punishment of the Fire.",
        reference = "Sahih al-Bukhari 5376, Ibn As-Sunni 457",
        benefit = "Invokes barakah (blessings) into food, nourishes the body, and wards off the devil from participating in the meal."
      ),
      DuaItem(
        id = "food_forgot_bismillah",
        categoryId = "daily_life",
        title = "If One Forgot Bismillah at the Beginning of the Meal",
        arabic = "بِسْمِ اللَّهِ أَوَّلَهُ وَآخِرَهُ",
        transliteration = "Bismillahi awwalahu wa akhirahu.",
        translation = "In the Name of Allah, at its beginning and at its end.",
        reference = "Sunan Abi Dawood 3767, Jami` at-Tirmidhi 1858",
        benefit = "Restores the divine blessing of the meal even if remembered halfway through eating."
      ),
      DuaItem(
        id = "food_after_eating",
        categoryId = "daily_life",
        title = "Dua After Finishing a Meal (Past Sins Forgiven)",
        arabic = "الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنِي هَذَا وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍ",
        transliteration = "Alhamdu lillahil-ladhi at'amani hadha wa razaqaneehi min ghayri hawlin minni wa la quwwah.",
        translation = "All praise is for Allah Who fed me this and provided it for me without any power or strength on my part.",
        reference = "Jami` at-Tirmidhi 3458, Sunan Abi Dawood 4023",
        benefit = "The Prophet (pbuh) said whoever recites this after eating, his past minor sins are forgiven."
      ),
      DuaItem(
        id = "daily_washroom_enter",
        categoryId = "daily_life",
        title = "Dua Before Entering the Washroom",
        arabic = "بِسْمِ اللَّهِ، اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْخُبُثِ وَالْخَبَائِثِ",
        transliteration = "Bismillah, Allahumma inni a'udhu bika minal-khubuthi wal-khaba'ith.",
        translation = "In the Name of Allah. O Allah, I seek refuge in You from impure male and female devils.",
        reference = "Sahih al-Bukhari 142, Sahih Muslim 375",
        benefit = "Establishes a spiritual veil between human modesty and the gaze of unseen jinns."
      ),
      DuaItem(
        id = "daily_washroom_exit",
        categoryId = "daily_life",
        title = "Dua After Leaving the Washroom",
        arabic = "غُفْرَانَكَ، الْحَمْدُ لِلَّهِ الَّذِي أَذْهَبَ عَنِّي الْأَذَى وَعَافَانِي",
        transliteration = "Ghufranaka. Alhamdu lillahil-ladhi adhhaba 'annil-adha wa 'afani.",
        translation = "I seek Your forgiveness. All praise is for Allah Who removed the harmful waste from me and granted me relief and health.",
        reference = "Sunan Abi Dawood 30, Jami` at-Tirmidhi 7, Sunan Ibn Majah 301",
        benefit = "Expresses deep gratitude for the miracle of digestive elimination and bodily health."
      ),
      DuaItem(
        id = "daily_enter_home",
        categoryId = "daily_life",
        title = "Dua When Entering the Home",
        arabic = "بِسْمِ اللَّهِ وَلَجْنَا، وَبِسْمِ اللَّهِ خَرَجْنَا، وَعَلَى رَبِّنَا تَوَكَّلْنَا",
        transliteration = "Bismillahi walajna, wa bismillahi kharajna, wa 'ala Rabbina tawakkalna.",
        translation = "In the Name of Allah we enter, and in the Name of Allah we leave, and upon our Lord we rely.",
        reference = "Sunan Abi Dawood 5096",
        benefit = "Prevents Satan from entering the house or staying for the night with the family."
      ),
      DuaItem(
        id = "daily_wearing_clothes",
        categoryId = "daily_life",
        title = "Dua When Wearing New Clothes",
        arabic = "الْحَمْدُ لِلَّهِ الَّذِي كَسَانِي هَذَا الثَّوْبَ وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍ",
        transliteration = "Alhamdu lillahil-ladhi kasanee hadhath-thawba wa razaqaneehi min ghayri hawlin minni wa la quwwah.",
        translation = "All praise is for Allah Who has clothed me with this garment and provided it for me without any power or strength on my part.",
        reference = "Sunan Abi Dawood 4023, Jami` at-Tirmidhi 3458",
        benefit = "Cultivates humility, gratitude for modesty, and expiates past shortcomings."
      ),

      // ══════════════════════════════════════════════════════════════════════
      // 9. QURANIC RABBANA DUAS (أَدْعِيَةٌ مِنَ الْقُرْآنِ الْكَرِيمِ)
      // ══════════════════════════════════════════════════════════════════════
      DuaItem(
        id = "quranic_rabbana_atina",
        categoryId = "quranic_duas",
        title = "Goodness in Both Worlds (Rabbana Atina)",
        arabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
        transliteration = "Rabbana atina fid-dunya hasanatan wa fil-akhirati hasanatan wa qina 'adhaban-nar.",
        translation = "Our Lord, give us in this world that which is good and in the Hereafter that which is good, and save us from the punishment of the Fire.",
        reference = "Surah Al-Baqarah 2:201",
        benefit = "The most frequent supplication of the Prophet Muhammad (pbuh), uniting all goodness of this life and the next."
      ),
      DuaItem(
        id = "quranic_la_tuzigh",
        categoryId = "quranic_duas",
        title = "Steadfastness of the Heart from Deviation",
        arabic = "رَبَّنَا لَا تُزِغْ قُلُوبَنَا بَعْدَ إِذْ هَدَيْتَنَا وَهَبْ لَنَا مِنْ لَدُنْكَ رَحْمَةً إِنَّكَ أَنْتَ الْوَهَّابُ",
        transliteration = "Rabbana la tuzigh quloobana ba'da idh hadaytana wa hab lana min ladunka rahmah, innaka Antal-Wahhab.",
        translation = "Our Lord! Let not our hearts deviate now after You have guided us, but grant us mercy from Your presence; for You are the Bestower.",
        reference = "Surah Ali 'Imran 3:8",
        benefit = "Preserves the heart from doubts, hypocrisy, confusion, and spiritual decline."
      ),
      DuaItem(
        id = "quranic_zidni_ilma",
        categoryId = "quranic_duas",
        title = "Supplication for Increase in Knowledge",
        arabic = "رَبِّ زِدْنِي عِلْمًا",
        transliteration = "Rabbi zidnee 'ilma.",
        translation = "My Lord! Increase me in beneficial knowledge.",
        reference = "Surah Ta-Ha 20:114",
        benefit = "The only worldly matter in which Allah directly instructed His Prophet (pbuh) to pray for an increase."
      ),
      DuaItem(
        id = "quranic_patience_firmness",
        categoryId = "quranic_duas",
        title = "Patience and Victory Over Adversity",
        arabic = "رَبَّنَا أَفْرِغْ عَلَيْنَا صَبْرًا وَثَبِّتْ أَقْدَامَنَا وَانْصُرْنَا عَلَى الْقَوْمِ الْكَافِرِينَ",
        transliteration = "Rabbana afrigh 'alayna sabran wa thabbit aqdamana wansurna 'alal-qawmil-kafireen.",
        translation = "Our Lord! Pour out upon us patience, make our steps firm, and grant us victory over the disbelieving people.",
        reference = "Surah Al-Baqarah 2:250",
        benefit = "Supplicated by the righteous army of Talut against Goliath; inspires courage and unwavering faith."
      ),
      DuaItem(
        id = "quranic_perfect_light",
        categoryId = "quranic_duas",
        title = "Completion of Light on the Day of Judgment",
        arabic = "رَبَّنَا أَتْمِمْ لَنَا نُورَنَا وَاغْفِرْ لَنَا إِنَّكَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
        transliteration = "Rabbana atmim lana noorana waghfir lana innaka 'ala kulli shay'in Qadeer.",
        translation = "Our Lord, perfect for us our light and forgive us; indeed You are over all things Capable.",
        reference = "Surah At-Tahrim 66:8",
        benefit = "The plea of the believers traversing the Bridge (Sirat) when the light of the hypocrites is extinguished."
      ),
      DuaItem(
        id = "quranic_musa_speech",
        categoryId = "quranic_duas",
        title = "Ease in Tasks and Eloquence of Speech (Prophet Musa)",
        arabic = "رَبِّ اشْرَحْ لِي صَدْرِي ۝ وَيَسِّرْ لِي أَمْرِي ۝ وَاحْلُلْ عُقْدَةً مِنْ لِسَانِي ۝ يَفْقَهُوا قَوْلِي",
        transliteration = "Rabbish-rah li sadri, wa yassir li amri, wahlul 'uqdatan min lisani, yafqahoo qawli.",
        translation = "My Lord! Expand for me my chest, and make easy for me my task, and untie the knot from my tongue, that they may understand my speech.",
        reference = "Surah Ta-Ha 20:25-28",
        benefit = "Removes anxiety before presentations, speeches, exams, and daunting responsibilities."
      ),

      // ══════════════════════════════════════════════════════════════════════
      // 10. HEALTH & SICKNESS (أَدْعِيَةُ الشِّفَاءِ وَالْعِيَادَةِ)
      // ══════════════════════════════════════════════════════════════════════
      DuaItem(
        id = "health_ayyoob_healing",
        categoryId = "health_illness",
        title = "Prophet Ayyub's Supplication for Cure",
        arabic = "رَبِّ أَنِّي مَسَّنِيَ الضُّرُّ وَأَنْتَ أَرْحَمُ الرَّاحِمِينَ",
        transliteration = "Rabbi annee massaniyad-durru wa Anta Arhamur-Rahimeen.",
        translation = "Truly distress has seized me, and You are the Most Merciful of those who show mercy.",
        reference = "Surah Al-Anbiya 21:83",
        benefit = "The humble supplication through which Allah cured Prophet Ayyub from years of severe chronic illness."
      ),
      DuaItem(
        id = "health_prophetic_ruqyah",
        categoryId = "health_illness",
        title = "Prophetic Ruqyah for Sickness (Adh-hibil-Ba's)",
        arabic = "اللَّهُمَّ رَبَّ النَّاسِ، أَذْهِبِ الْبَأْسَ، اشْفِ أَنْتَ الشَّافِي، لَا شِفَاءَ إِلَّا شِفَاؤُكَ، شِفَاءً لَا يُغَادِرُ سَقَمًا",
        transliteration = "Allahumma Rabban-nas, adh-hibil-ba's, ishfi Antash-Shafi, la shifa'a illa shifa'uk, shifa'an la yughadiru saqama.",
        translation = "O Allah, Lord of mankind, remove the affliction and cure him; You are the Healer, there is no healing but Yours, a cure that leaves behind no disease.",
        reference = "Sahih al-Bukhari 5743, Sahih Muslim 2191",
        benefit = "Recited by the Messenger of Allah (pbuh) while gently wiping his right hand over the sick person."
      ),
      DuaItem(
        id = "health_hand_on_pain",
        categoryId = "health_illness",
        title = "Placing Hand on the Area of Bodily Pain",
        arabic = "بِسْمِ اللَّهِ (ثَلَاثًا)، أَعُوذُ بِاللَّهِ وَقُدْرَتِهِ مِنْ شَرِّ مَا أَجِدُ وَأُحَاذِرُ (سَبْعَ مَرَّاتٍ)",
        transliteration = "Bismillah (3 times), A'udhu billahi wa qudratihi min sharri ma ajidu wa uhadhir (7 times).",
        translation = "In the Name of Allah (three times). I seek refuge in Allah and His Power from the evil of what I feel and fear (seven times).",
        reference = "Sahih Muslim 2202",
        benefit = "Taught by the Prophet (pbuh) to Uthman ibn Abi al-Aas for severe physical pain; he was cured immediately."
      ),
      DuaItem(
        id = "health_visiting_sick",
        categoryId = "health_illness",
        title = "Supplication When Visiting a Sick Person",
        arabic = "لَا بَأْسَ طَهُورٌ إِنْ شَاءَ اللَّهُ، أَسْأَلُ اللَّهَ الْعَظِيمَ رَبَّ الْعَرْشِ الْعَظِيمِ أَنْ يَشْفِيَكَ",
        transliteration = "La ba'sa tahoorun in sha' Allah. As'alullahal-'Azeema Rabbal-'Arshil-'Azeemi an yashfiyak.",
        translation = "No harm, may it be a purification if Allah wills. I ask Allah the Tremendous, Lord of the Magnificent Throne, to cure you.",
        reference = "Sahih al-Bukhari 3616, Sunan Abi Dawood 3106",
        benefit = "Whoever visits a sick person and recites this seven times, Allah will grant him healing unless it is his appointed time."
      ),
      DuaItem(
        id = "health_seeing_afflicted",
        categoryId = "health_illness",
        title = "Dua When Seeing Someone Afflicted with Trial",
        arabic = "الْحَمْدُ لِلَّهِ الَّذِي عَافَانِي مِمَّا ابْتَلَاكَ بِهِ وَفَضَّلَنِي عَلَى كَثِيرٍ مِمَّنْ خَلَقَ تَفْضِيلًا",
        transliteration = "Alhamdu lillahil-ladhi 'afani mimmabtalaaka bihi wa faddalani 'ala katheerin mimman khalaqa tafdeela.",
        translation = "All praise is for Allah Who saved me from that with which He afflicted you, and favored me over many of those whom He has created.",
        reference = "Jami` at-Tirmidhi 3432, Sunan Ibn Majah 3892",
        benefit = "Whoever sees an afflicted person and recites this quietly to himself will be protected from that trial for life."
      ),

      // ══════════════════════════════════════════════════════════════════════
      // 11. PARENTS & FAMILY (أَدْعِيَةُ الْوَالِدَيْنِ وَالْأُسْرَةِ)
      // ══════════════════════════════════════════════════════════════════════
      DuaItem(
        id = "family_parents_mercy",
        categoryId = "parents_family",
        title = "Supplication for Mercy upon Parents",
        arabic = "رَبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
        transliteration = "Rabbir-hamhuma kama rabbayani sagheera.",
        translation = "My Lord! Have mercy upon them both as they raised and cherished me when I was small.",
        reference = "Surah Al-Isra 17:24",
        benefit = "The divine Quranic injunction of filial devotion and continuous charity for living and departed parents."
      ),
      DuaItem(
        id = "family_parents_forgiveness",
        categoryId = "parents_family",
        title = "Forgiveness for Parents and All Believers",
        arabic = "رَبَّنَا اغْفِرْ لِي وَلِوَالِدَيَّ وَلِلْمُؤْمِنِينَ يَوْمَ يَقُومُ الْحِسَابُ",
        transliteration = "Rabbanagh-fir li wa li-walidayya wa lil-mu'mineena yawma yaqoomul-hisab.",
        translation = "Our Lord! Forgive me and my parents, and the believers on the Day when the account is established.",
        reference = "Surah Ibrahim 14:41",
        benefit = "The heartfelt supplication of Prophet Ibrahim uniting the whole family in eternal forgiveness."
      ),
      DuaItem(
        id = "family_righteous_offspring",
        categoryId = "parents_family",
        title = "Dua for Righteous Children (Prophet Zakariyya)",
        arabic = "رَبِّ هَبْ لِي مِنْ لَدُنْكَ ذُرِّيَّةً طَيِّبَةً إِنَّكَ سَمِيعُ الدُّعَاءِ",
        transliteration = "Rabbi hab li min ladunka dhurriyyatan tayyibah, innaka Samee'ud-du'a.",
        translation = "My Lord! Grant me from Yourself a good offspring; indeed You are the All-Hearer of invocation.",
        reference = "Surah Ali 'Imran 3:38",
        benefit = "Answered by the miraculous birth of Prophet Yahya; brings pious and honorable descendants."
      ),
      DuaItem(
        id = "family_comfort_of_eyes",
        categoryId = "parents_family",
        title = "Spouses and Children as Comfort of the Eyes",
        arabic = "رَبَّنَا هَبْ لَنَا مِنْ أَزْوَاجِنَا وَذُرِّيَّاتِنَا قُرَّةَ أَعْيُنٍ وَاجْعَلْنَا لِلْمُتَّقِينَ إِمَامًا",
        transliteration = "Rabbana hab lana min azwajina wa dhurriyyatina qurrata a'yunin waj'alna lil-muttaqeena imama.",
        translation = "Our Lord! Bestow on us from our spouses and our offspring the comfort of our eyes, and make us leaders of the righteous.",
        reference = "Surah Al-Furqan 25:74",
        benefit = "The distinguished hallmark of the true servants of the Most Merciful (Ibadur-Rahman)."
      ),
      DuaItem(
        id = "family_gratitude_parents",
        categoryId = "parents_family",
        title = "Gratitude for Parents and Upright Generations",
        arabic = "رَبِّ أَوْزِعْنِي أَنْ أَشْكُرَ نِعْمَتَكَ الَّتِي أَنْعَمْتَ عَلَيَّ وَعَلَى وَالِدَيَّ وَأَنْ أَعْمَلَ صَالِحًا تَرْضَاهُ وَأَصْلِحْ لِي فِي ذُرِّيَّتِي إِنِّي تُبْتُ إِلَيْكَ وَإِنِّي مِنَ الْمُسْلِمِينَ",
        transliteration = "Rabbi awzi'ni an ashkura ni'matakal-latee an'amta 'alayya wa 'ala walidayya wa an a'mala salihan tardahu wa aslih li fee dhurriyyatee, innee tubtu ilayka wa innee minal-muslimeen.",
        translation = "My Lord, enable me to be grateful for Your favor which You have bestowed upon me and upon my parents, and to do righteousness of which You approve, and make righteous for me my offspring. Indeed, I have repented to You, and indeed, I am of the Muslims.",
        reference = "Surah Al-Ahqaf 46:15",
        benefit = "Brings deep peace, barakah across generations, and divine acceptance of lifelong deeds."
      ),

      // ══════════════════════════════════════════════════════════════════════
      // 12. GUIDANCE & ISTIKHARAH (الِاسْتِخَارَةُ وَطَلَبُ الْهِدَايَةِ)
      // ══════════════════════════════════════════════════════════════════════
      DuaItem(
        id = "guidance_istikharah_complete",
        categoryId = "guidance_istikhara",
        title = "The Complete Dua of Istikharah (Seeking Guidance in Decisions)",
        arabic = "اللَّهُمَّ إِنِّي أَسْتَخِيرُكَ بِعِلْمِكَ، وَأَسْتَقْدِرُكَ بِقُدْرَتِكَ، وَأَسْأَلُكَ مِنْ فَضْلِكَ الْعَظِيمِ، فَإِنَّكَ تَقْدِرُ وَلَا أَقْدِرُ، وَتَعْلَمُ وَلَا أَعْلَمُ، وَأَنْتَ عَلَّامُ الْغُيُوبِ، اللَّهُمَّ إِنْ كُنْتَ تَعْلَمُ أَنَّ هَذَا الْأَمْرَ خَيْرٌ لِي فِي دِينِي وَمَعَاشِي وَعَاقِبَةِ أَمْرِي فَاقْدُرْهُ لِي وَيَسِّرْهُ لِي ثُمَّ بَارِكْ لِي فِيهِ، وَإِنْ كُنْتَ تَعْلَمُ أَنَّ هَذَا الْأَمْرَ شَرٌّ لِي فِي دِينِي وَمَعَاشِي وَعَاقِبَةِ أَمْرِي فَاصْرِفْهُ عَنِّي وَاصْرِفْنِي عَنْهُ، وَاقْدُرْ لِيَ الْخَيْرَ حَيْثُ كَانَ ثُمَّ أَرْضِنِي بِهِ",
        transliteration = "Allahumma inni astakheeruka bi-'ilmika, wa astaqdiruka bi-qudratika, wa as'aluka min fadlikal-'azeem, fa-innaka taqdiru wa la aqdir, wa ta'lamu wa la a'lam, wa Anta 'Allamul-ghuyoob. Allahumma in kunta ta'lamu anna hadhal-amra khayrun li fi deeni wa ma'ashi wa 'aqibati amri faqdurhu li wa yassirhu li thumma barik li feeh, wa in kunta ta'lamu anna hadhal-amra sharrun li fi deeni wa ma'ashi wa 'aqibati amri fasrifhu 'anni wasrifni 'anhu, waqdur liyal-khayra haythu kana thumma ardini bih.",
        translation = "O Allah, I consult You through Your Knowledge, and seek ability through Your Power, and ask You of Your Great Bounty. For You have power and I have none, You know and I do not know, and You are the Knower of the unseen. O Allah, if You know that this matter is good for me in my religion, my livelihood, and the end of my affairs, then decree it for me, make it easy for me, and then bless me in it. And if You know that this matter is bad for me in my religion, my livelihood, and the end of my affairs, then turn it away from me and turn me away from it, and decree for me the good wherever it may be, and make me pleased with it.",
        reference = "Sahih al-Bukhari 1162",
        benefit = "The Prophet (pbuh) taught this supplication to the Sahabah as he would teach a Surah from the Quran for all decisions."
      ),
      DuaItem(
        id = "guidance_piety_chastity",
        categoryId = "guidance_istikhara",
        title = "Supplication for Guidance, Piety, & Self-Sufficiency",
        arabic = "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْهُدَى وَالتُّقَى وَالْعَفَافَ وَالْغِنَى",
        transliteration = "Allahumma inni as'alukal-huda wat-tuqa wal-'afafa wal-ghina.",
        translation = "O Allah, I ask You for guidance, piety, chastity, and self-sufficiency.",
        reference = "Sahih Muslim 2721",
        benefit = "Combines all foundational pillars of spiritual elevation, upright character, and independence from people."
      ),
      DuaItem(
        id = "guidance_turner_of_hearts",
        categoryId = "guidance_istikhara",
        title = "Steadfastness of the Heart (Ya Muqallibal-Quloob)",
        arabic = "يَا مُقَلِّبَ الْقُلُوبِ ثَبِّتْ قَلْبِي عَلَى دِينِكَ",
        transliteration = "Ya Muqallibal-quloobi thabbit qalbi 'ala deenik.",
        translation = "O Turner of the hearts, make my heart steadfast upon Your religion.",
        reference = "Jami` at-Tirmidhi 3522, Sunan Ibn Majah 3834",
        benefit = "The most frequent supplication uttered by the Prophet (pbuh) throughout his life."
      ),
      DuaItem(
        id = "guidance_director_of_hearts",
        categoryId = "guidance_istikhara",
        title = "Directing Hearts Toward Divine Obedience",
        arabic = "اللَّهُمَّ مُصَرِّفَ الْقُلُوبِ صَرِّفْ قُلُوبَنَا عَلَى طَاعَتِكَ",
        transliteration = "Allahumma Musarrifal-quloobi sarrif quloobana 'ala ta'atik.",
        translation = "O Allah, Director of hearts, direct our hearts to Your obedience.",
        reference = "Sahih Muslim 2654",
        benefit = "The Prophet (pbuh) explained that all hearts are between two fingers of the Most Merciful, turning them as He wills."
      ),
      DuaItem(
        id = "guidance_straight_path",
        categoryId = "guidance_istikhara",
        title = "Supplication for Straightness and Rectitude",
        arabic = "اللَّهُمَّ اهْدِنِي وَسَدِّدْنِي، وَاذْكُرْ بِالْهُدَى هِدَايَتَكَ الطَّرِيقَ، وَبِالسَّدَادِ سَدَادَ السَّهْمِ",
        transliteration = "Allahummah-dini wa saddidni, wadhkur bil-huda hidayatakat-tareeq, wa bis-sadadi sadadas-sahm.",
        translation = "O Allah, guide me and keep me straight; and let Your guidance remind you of the straight path, and keeping straight like the straightness of an arrow.",
        reference = "Sahih Muslim 2725",
        benefit = "Taught by the Prophet (pbuh) to Ali ibn Abi Talib for unwavering clarity in thought and action."
      )
    )
  }
}
