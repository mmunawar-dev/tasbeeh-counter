package com.example.model

data class CityLocation(
  val id: String,
  val name: String,
  val country: String,
  val latitude: Double,
  val longitude: Double,
  val timeZoneOffsetHours: Double,
  val isGpsDetected: Boolean = false,
  val recommendedMethod: CalculationMethod = CalculationMethod.KARACHI,
  val recommendedMadhab: Madhab = Madhab.HANAFI
) {
  val displayName: String get() = "$name, $country"

  companion object {
    val DEFAULT_CITY = CityLocation(
      id = "rawalpindi_pk",
      name = "Rawalpindi",
      country = "Pakistan",
      latitude = 33.5651,
      longitude = 73.0169,
      timeZoneOffsetHours = 5.0,
      recommendedMethod = CalculationMethod.KARACHI,
      recommendedMadhab = Madhab.HANAFI
    )

    val PRESET_CITIES = listOf(
      DEFAULT_CITY,
      CityLocation("islamabad_pk", "Islamabad", "Pakistan", 33.6844, 73.0479, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("lahore_pk", "Lahore", "Pakistan", 31.5204, 74.3587, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("karachi_pk", "Karachi", "Pakistan", 24.8607, 67.0011, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("makkah_sa", "Makkah", "Saudi Arabia", 21.3891, 39.8579, 3.0, false, CalculationMethod.UMM_AL_QURA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("madinah_sa", "Madinah", "Saudi Arabia", 24.5247, 39.5692, 3.0, false, CalculationMethod.UMM_AL_QURA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("istanbul_tr", "Istanbul", "Turkey", 41.0082, 28.9784, 3.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("cairo_eg", "Cairo", "Egypt", 30.0444, 31.2357, 2.0, false, CalculationMethod.EGYPT, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("dubai_ae", "Dubai", "United Arab Emirates", 25.2048, 55.2708, 4.0, false, CalculationMethod.DUBAI, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("doha_qa", "Doha", "Qatar", 25.2854, 51.5310, 3.0, false, CalculationMethod.UMM_AL_QURA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("london_uk", "London", "United Kingdom", 51.5074, -0.1278, 1.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("newyork_us", "New York", "United States", 40.7128, -74.0060, -4.0, false, CalculationMethod.ISNA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("jakarta_id", "Jakarta", "Indonesia", -6.2088, 106.8456, 7.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("kualalumpur_my", "Kuala Lumpur", "Malaysia", 3.1390, 101.6869, 8.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("toronto_ca", "Toronto", "Canada", 43.6532, -79.3832, -4.0, false, CalculationMethod.ISNA, Madhab.HANAFI),
      CityLocation("paris_fr", "Paris", "France", 48.8566, 2.3522, 2.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI)
    )
  }
}
