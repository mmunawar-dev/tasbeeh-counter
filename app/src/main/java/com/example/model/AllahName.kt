package com.example.model

data class AllahName(
  val number: Int,
  val arabic: String,
  val transliteration: String,
  val englishMeaning: String,
  val explanation: String
) {
  companion object {
    val NAMES_LIST = listOf(
      AllahName(1, "الرَّحْمَنُ", "Ar-Rahman", "The Entirely Merciful", "The One who has plenty of mercy for all creation in this world."),
      AllahName(2, "الرَّحِيمُ", "Ar-Raheem", "The Especially Merciful", "The One who bestows abundant and ongoing mercy upon believers."),
      AllahName(3, "الْمَلِكُ", "Al-Malik", "The King", "The Sovereign Owner and Absolute Ruler of all creation and dominion."),
      AllahName(4, "الْقُدُّوسُ", "Al-Quddus", "The Most Holy", "The Pure One who is completely free from any imperfection or deficiency."),
      AllahName(5, "السَّلَامُ", "As-Salam", "The Source of Peace", "The Giver of peace and safety who is free from all flaws."),
      AllahName(6, "الْمُؤْمِنُ", "Al-Mu'min", "The Inspirer of Faith", "The Guarantor of security who fulfills all promises to believers."),
      AllahName(7, "الْمُهَيْمِنُ", "Al-Muhaymin", "The Guardian", "The Overseer and Protector who witnesses and preserves all things."),
      AllahName(8, "الْعَزِيزُ", "Al-Aziz", "The All-Mighty", "The Invincible One whose might and power cannot be overcome."),
      AllahName(9, "الْجَبَّارُ", "Al-Jabbar", "The Compeller", "The Restorer who repairs all broken things and executes His absolute will."),
      AllahName(10, "الْمُتَكَبِّرُ", "Al-Mutakabbir", "The Supreme", "The One who is truly great and exalted above all creation."),
      AllahName(11, "الْخَالِقُ", "Al-Khaliq", "The Creator", "The One who brings everything from non-existence into existence."),
      AllahName(12, "الْبَارِئُ", "Al-Bari'", "The Originator", "The Maker who creates with perfect proportion without prior model."),
      AllahName(13, "الْمُصَوِّرُ", "Al-Musawwir", "The Fashioner", "The Designer who gives distinct shape and beauty to every created entity."),
      AllahName(14, "الْغَفَّارُ", "Al-Ghaffar", "The All-Forgiving", "The One who continually forgives sins and conceals shortcomings."),
      AllahName(15, "الْقَهَّارُ", "Al-Qahhar", "The Subduer", "The Master who has complete dominance over all creation."),
      AllahName(16, "الْوَهَّابُ", "Al-Wahhab", "The Bestower", "The Giver who continually provides gifts without seeking any return."),
      AllahName(17, "الرَّزَّاقُ", "Ar-Razzaq", "The Provider", "The One who provides sustenance for mind, body, and soul."),
      AllahName(18, "الْفَتَّاحُ", "Al-Fattah", "The Opener", "The Opener of the gates of mercy, victory, and knowledge."),
      AllahName(19, "الْعَلِيمُ", "Al-Alim", "The All-Knowing", "The Omniscient whose knowledge encompasses the apparent and hidden."),
      AllahName(20, "الْقَابِضُ", "Al-Qabid", "The Withholder", "The One who restricts sustenance according to divine wisdom."),
      AllahName(21, "الْبَاسِطُ", "Al-Basit", "The Expander", "The One who enlarges and expands hearts and sustenance generously."),
      AllahName(22, "الْخَافِضُ", "Al-Khafid", "The Abaser", "The One who humbles oppressors and lowers whoever He wills."),
      AllahName(23, "الرَّافِعُ", "Ar-Rafi'", "The Exalter", "The One who elevates believers in dignity, faith, and status."),
      AllahName(24, "الْمُعِزُّ", "Al-Mu'izz", "The Bestower of Honour", "The Giver of honor and esteem to those who follow righteousness."),
      AllahName(25, "الْمُذِلُّ", "Al-Muzill", "The Humiliator", "The One who brings disgrace to arrogance and transgression."),
      AllahName(26, "السَّمِيعُ", "As-Sami'", "The All-Hearing", "The One who hears every sound, prayer, and whispered thought."),
      AllahName(27, "الْبَصِيرُ", "Al-Baseer", "The All-Seeing", "The One who perceives every hidden movement and secret act."),
      AllahName(28, "الْحَكَمُ", "Al-Hakam", "The Impartial Judge", "The Ultimate Arbiter whose decree cannot be overruled."),
      AllahName(29, "الْعَدْلُ", "Al-Adl", "The Utterly Just", "The Embodiment of absolute justice who wrongs no one."),
      AllahName(30, "اللَّطِيفُ", "Al-Lateef", "The Subtle, The Kind", "The Knower of subtle depths who grants kindness in unseen ways."),
      AllahName(31, "الْخَبِيرُ", "Al-Khabeer", "The All-Aware", "The One who knows the inner reality and secrets of everything."),
      AllahName(32, "الْحَلِيمُ", "Al-Haleem", "The Most Forbearing", "The One who does not rush to punish and grants time to repent."),
      AllahName(33, "الْعَظِيمُ", "Al-Azeem", "The Magnificent", "The Supreme whose greatness is beyond human comprehension."),
      AllahName(34, "الْغَفُورُ", "Al-Ghafoor", "The Forgiving", "The One whose forgiveness covers the greatest sins of believers."),
      AllahName(35, "الشَّكُورُ", "Ash-Shakoor", "The Most Appreciative", "The One who rewards abundantly for even the smallest good deeds."),
      AllahName(36, "الْعَلِيُّ", "Al-Aliyy", "The Most High", "The Exalted above all creation in rank, authority, and status."),
      AllahName(37, "الْكَبِيرُ", "Al-Kabeer", "The Greatest", "The Infinitely Great One whose majesty surpasses all imagination."),
      AllahName(38, "الْحَفِيظُ", "Al-Hafeez", "The Preserver", "The Guardian who protects the universe and records all actions."),
      AllahName(39, "الْمُقِيتُ", "Al-Muqeet", "The Sustainer", "The One who provides nourishment and strength to all life."),
      AllahName(40, "الْحَسِيبُ", "Al-Haseeb", "The Reckoner", "The One who suffices all who rely on Him and takes full account.")
    )
  }
}
