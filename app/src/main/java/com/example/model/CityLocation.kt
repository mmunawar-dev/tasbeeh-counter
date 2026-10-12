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

    private var cachedCities: List<CityLocation>? = null

    fun getAllCities(context: android.content.Context? = null): List<CityLocation> {
      cachedCities?.let { return it }
      if (context != null) {
        val loaded = loadFromAssets(context)
        if (loaded.isNotEmpty()) {
          cachedCities = loaded
          return loaded
        }
      }
      cachedCities = PRESET_CITIES
      return PRESET_CITIES
    }

    private fun loadFromAssets(context: android.content.Context): List<CityLocation> {
      return try {
        val jsonString = context.assets.open("cities.json").bufferedReader().use { it.readText() }
        val array = org.json.JSONArray(jsonString)
        val list = mutableListOf<CityLocation>()
        for (i in 0 until array.length()) {
          val obj = array.getJSONObject(i)
          val methodStr = obj.optString("recommendedMethod", "KARACHI")
          val method = try { CalculationMethod.valueOf(methodStr) } catch (_: Exception) { CalculationMethod.KARACHI }
          val madhabStr = obj.optString("recommendedMadhab", "HANAFI")
          val madhab = try { Madhab.valueOf(madhabStr) } catch (_: Exception) { Madhab.HANAFI }
          list.add(
            CityLocation(
              id = obj.getString("id"),
              name = obj.getString("name"),
              country = obj.getString("country"),
              latitude = obj.getDouble("latitude"),
              longitude = obj.getDouble("longitude"),
              timeZoneOffsetHours = obj.getDouble("timeZoneOffsetHours"),
              isGpsDetected = false,
              recommendedMethod = method,
              recommendedMadhab = madhab
            )
          )
        }
        list
      } catch (e: Exception) {
        PRESET_CITIES
      }
    }

    val PRESET_CITIES = listOf(
      DEFAULT_CITY,
      CityLocation("islamabad_pk", "Islamabad", "Pakistan", 33.6844, 73.0479, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("lahore_pk", "Lahore", "Pakistan", 31.5204, 74.3587, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("karachi_pk", "Karachi", "Pakistan", 24.8607, 67.0011, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("faisalabad_pk", "Faisalabad", "Pakistan", 31.4504, 73.1350, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("multan_pk", "Multan", "Pakistan", 30.1575, 71.5249, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("peshawar_pk", "Peshawar", "Pakistan", 34.0151, 71.5249, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("quetta_pk", "Quetta", "Pakistan", 30.1798, 66.9750, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("sialkot_pk", "Sialkot", "Pakistan", 32.4945, 74.5229, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("gujranwala_pk", "Gujranwala", "Pakistan", 32.1877, 74.1945, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("hyderabad_pk", "Hyderabad", "Pakistan", 25.3960, 68.3578, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("makkah_sa", "Makkah", "Saudi Arabia", 21.3891, 39.8579, 3.0, false, CalculationMethod.UMM_AL_QURA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("madinah_sa", "Madinah", "Saudi Arabia", 24.5247, 39.5692, 3.0, false, CalculationMethod.UMM_AL_QURA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("riyadh_sa", "Riyadh", "Saudi Arabia", 24.7136, 46.6753, 3.0, false, CalculationMethod.UMM_AL_QURA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("jeddah_sa", "Jeddah", "Saudi Arabia", 21.5433, 39.1728, 3.0, false, CalculationMethod.UMM_AL_QURA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("dammam_sa", "Dammam", "Saudi Arabia", 26.4207, 50.0888, 3.0, false, CalculationMethod.UMM_AL_QURA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("taif_sa", "Taif", "Saudi Arabia", 21.2854, 40.4222, 3.0, false, CalculationMethod.UMM_AL_QURA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("dubai_ae", "Dubai", "United Arab Emirates", 25.2048, 55.2708, 4.0, false, CalculationMethod.DUBAI, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("abudhabi_ae", "Abu Dhabi", "United Arab Emirates", 24.4539, 54.3773, 4.0, false, CalculationMethod.DUBAI, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("sharjah_ae", "Sharjah", "United Arab Emirates", 25.3463, 55.4209, 4.0, false, CalculationMethod.DUBAI, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("doha_qa", "Doha", "Qatar", 25.2854, 51.5310, 3.0, false, CalculationMethod.UMM_AL_QURA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("kuwaitcity_kw", "Kuwait City", "Kuwait", 29.3759, 47.9774, 3.0, false, CalculationMethod.UMM_AL_QURA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("manama_bh", "Manama", "Bahrain", 26.2285, 50.5860, 3.0, false, CalculationMethod.UMM_AL_QURA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("muscat_om", "Muscat", "Oman", 23.5880, 58.3829, 4.0, false, CalculationMethod.DUBAI, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("amman_jo", "Amman", "Jordan", 31.9454, 35.9284, 3.0, false, CalculationMethod.EGYPT, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("jerusalem_ps", "Jerusalem", "Palestine", 31.7683, 35.2137, 3.0, false, CalculationMethod.EGYPT, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("beirut_lb", "Beirut", "Lebanon", 33.8938, 35.5018, 3.0, false, CalculationMethod.EGYPT, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("damascus_sy", "Damascus", "Syria", 33.5138, 36.2765, 3.0, false, CalculationMethod.EGYPT, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("baghdad_iq", "Baghdad", "Iraq", 33.3152, 44.3661, 3.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("erbil_iq", "Erbil", "Iraq", 36.1901, 43.9930, 3.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("basra_iq", "Basra", "Iraq", 30.5081, 47.7835, 3.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("sanaa_ye", "Sana'a", "Yemen", 15.3694, 44.1910, 3.0, false, CalculationMethod.UMM_AL_QURA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("aden_ye", "Aden", "Yemen", 12.7855, 45.0187, 3.0, false, CalculationMethod.UMM_AL_QURA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("tehran_ir", "Tehran", "Iran", 35.6892, 51.3890, 3.5, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("isfahan_ir", "Isfahan", "Iran", 32.6546, 51.6680, 3.5, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("istanbul_tr", "Istanbul", "Turkey", 41.0082, 28.9784, 3.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("ankara_tr", "Ankara", "Turkey", 39.9334, 32.8597, 3.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("izmir_tr", "Izmir", "Turkey", 38.4237, 27.1428, 3.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("bursa_tr", "Bursa", "Turkey", 40.1885, 29.0610, 3.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("antalya_tr", "Antalya", "Turkey", 36.8969, 30.7133, 3.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("konya_tr", "Konya", "Turkey", 37.8746, 32.4932, 3.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("cairo_eg", "Cairo", "Egypt", 30.0444, 31.2357, 2.0, false, CalculationMethod.EGYPT, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("alexandria_eg", "Alexandria", "Egypt", 31.2001, 29.9187, 3.0, false, CalculationMethod.EGYPT, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("giza_eg", "Giza", "Egypt", 30.0131, 31.2089, 3.0, false, CalculationMethod.EGYPT, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("tripoli_ly", "Tripoli", "Libya", 32.8872, 13.1913, 2.0, false, CalculationMethod.EGYPT, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("tunis_tn", "Tunis", "Tunisia", 36.8065, 10.1815, 1.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("algiers_dz", "Algiers", "Algeria", 36.7538, 3.0588, 1.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("oran_dz", "Oran", "Algeria", 35.6987, -0.6349, 1.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("casablanca_ma", "Casablanca", "Morocco", 33.5731, -7.5898, 1.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("rabat_ma", "Rabat", "Morocco", 34.0209, -6.8416, 1.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("marrakech_ma", "Marrakech", "Morocco", 31.6295, -7.9811, 1.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("khartoum_sd", "Khartoum", "Sudan", 15.5007, 32.5599, 2.0, false, CalculationMethod.EGYPT, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("mogadishu_so", "Mogadishu", "Somalia", 2.0469, 45.3182, 3.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("hargeisa_so", "Hargeisa", "Somalia", 9.5600, 44.0650, 3.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("djibouti_dj", "Djibouti City", "Djibouti", 11.8251, 42.5903, 3.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("addisababa_et", "Addis Ababa", "Ethiopia", 9.0300, 38.7400, 3.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("nairobi_ke", "Nairobi", "Kenya", -1.2921, 36.8219, 3.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("mombasa_ke", "Mombasa", "Kenya", -4.0435, 39.6682, 3.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("zanzibar_tz", "Zanzibar", "Tanzania", -6.1659, 39.2026, 3.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("daressalaam_tz", "Dar es Salaam", "Tanzania", -6.7924, 39.2083, 3.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("kampala_ug", "Kampala", "Uganda", 0.3476, 32.5825, 3.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("lagos_ng", "Lagos", "Nigeria", 6.5244, 3.3792, 1.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("abuja_ng", "Abuja", "Nigeria", 9.0765, 7.3986, 1.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("kano_ng", "Kano", "Nigeria", 12.0022, 8.5920, 1.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("dakar_sn", "Dakar", "Senegal", 14.7167, -17.4677, 0.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("niamey_ne", "Niamey", "Niger", 13.5116, 2.1254, 1.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("bamako_ml", "Bamako", "Mali", 12.6392, -8.0029, 0.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("johannesburg_za", "Johannesburg", "South Africa", -26.2041, 28.0473, 2.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("capetown_za", "Cape Town", "South Africa", -33.9249, 18.4241, 2.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("durban_za", "Durban", "South Africa", -29.8587, 31.0218, 2.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("delhi_in", "Delhi", "India", 28.6139, 77.2090, 5.5, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("mumbai_in", "Mumbai", "India", 19.0760, 72.8777, 5.5, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("hyderabad_in", "Hyderabad", "India", 17.3850, 78.4867, 5.5, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("bengaluru_in", "Bengaluru", "India", 12.9716, 77.5946, 5.5, false, CalculationMethod.KARACHI, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("chennai_in", "Chennai", "India", 13.0827, 80.2707, 5.5, false, CalculationMethod.KARACHI, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("kolkata_in", "Kolkata", "India", 22.5726, 88.3639, 5.5, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("ahmedabad_in", "Ahmedabad", "India", 23.0225, 72.5714, 5.5, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("srinagar_in", "Srinagar", "India", 34.0837, 74.7973, 5.5, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("dhaka_bd", "Dhaka", "Bangladesh", 23.8103, 90.4125, 6.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("chittagong_bd", "Chittagong", "Bangladesh", 22.3569, 91.7832, 6.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("sylhet_bd", "Sylhet", "Bangladesh", 24.8949, 91.8687, 6.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("colombo_lk", "Colombo", "Sri Lanka", 6.9271, 79.8612, 5.5, false, CalculationMethod.KARACHI, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("male_mv", "Male", "Maldives", 4.1755, 73.5093, 5.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("kabul_af", "Kabul", "Afghanistan", 34.5553, 69.2075, 4.5, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("kandahar_af", "Kandahar", "Afghanistan", 31.6289, 65.7372, 4.5, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("herat_af", "Herat", "Afghanistan", 34.3529, 62.2040, 4.5, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("tashkent_uz", "Tashkent", "Uzbekistan", 41.2995, 69.2401, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("samarkand_uz", "Samarkand", "Uzbekistan", 39.6270, 66.9750, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("bukhara_uz", "Bukhara", "Uzbekistan", 39.7681, 64.4556, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("almaty_kz", "Almaty", "Kazakhstan", 43.2220, 76.8512, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("astana_kz", "Astana", "Kazakhstan", 51.1694, 71.4491, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("bishkek_kg", "Bishkek", "Kyrgyzstan", 42.8746, 74.5698, 6.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("dushanbe_tj", "Dushanbe", "Tajikistan", 38.5598, 68.7870, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("ashgabat_tm", "Ashgabat", "Turkmenistan", 37.9601, 58.3261, 5.0, false, CalculationMethod.KARACHI, Madhab.HANAFI),
      CityLocation("baku_az", "Baku", "Azerbaijan", 40.4093, 49.8671, 4.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("jakarta_id", "Jakarta", "Indonesia", -6.2088, 106.8456, 7.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("surabaya_id", "Surabaya", "Indonesia", -7.2575, 112.7521, 7.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("bandung_id", "Bandung", "Indonesia", -6.9175, 107.6191, 7.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("medan_id", "Medan", "Indonesia", 3.5952, 98.6722, 7.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("makassar_id", "Makassar", "Indonesia", -5.1477, 119.4327, 8.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("bandaaceh_id", "Banda Aceh", "Indonesia", 5.5483, 95.3238, 7.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("kualalumpur_my", "Kuala Lumpur", "Malaysia", 3.1390, 101.6869, 8.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("penang_my", "Penang", "Malaysia", 5.4141, 100.3288, 8.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("johorbahru_my", "Johor Bahru", "Malaysia", 1.4927, 103.7414, 8.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("kotakinabalu_my", "Kota Kinabalu", "Malaysia", 5.9804, 116.0735, 8.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("singapore_sg", "Singapore", "Singapore", 1.3521, 103.8198, 8.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("bandarseribegawan_bn", "Bandar Seri Begawan", "Brunei", 4.9031, 114.9398, 8.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("bangkok_th", "Bangkok", "Thailand", 13.7563, 100.5018, 7.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("manila_ph", "Manila", "Philippines", 14.5995, 120.9842, 8.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("tokyo_jp", "Tokyo", "Japan", 35.6762, 139.6503, 9.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("seoul_kr", "Seoul", "South Korea", 37.5665, 126.9780, 9.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("beijing_cn", "Beijing", "China", 39.9042, 116.4074, 8.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("shanghai_cn", "Shanghai", "China", 31.2304, 121.4737, 8.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("urumqi_cn", "Urumqi", "China", 43.8256, 87.6168, 8.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("london_uk", "London", "United Kingdom", 51.5074, -0.1278, 1.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("birmingham_uk", "Birmingham", "United Kingdom", 52.4862, -1.8904, 1.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("manchester_uk", "Manchester", "United Kingdom", 53.4808, -2.2426, 1.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("glasgow_uk", "Glasgow", "United Kingdom", 55.8642, -4.2518, 1.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("paris_fr", "Paris", "France", 48.8566, 2.3522, 2.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("marseille_fr", "Marseille", "France", 43.2965, 5.3698, 2.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("berlin_de", "Berlin", "Germany", 52.5200, 13.4050, 2.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("frankfurt_de", "Frankfurt", "Germany", 50.1109, 8.6821, 2.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("munich_de", "Munich", "Germany", 48.1351, 11.5820, 2.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("amsterdam_nl", "Amsterdam", "Netherlands", 52.3676, 4.9041, 2.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("rotterdam_nl", "Rotterdam", "Netherlands", 51.9244, 4.4777, 2.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("brussels_be", "Brussels", "Belgium", 50.8503, 4.3517, 2.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("vienna_at", "Vienna", "Austria", 48.2082, 16.3738, 2.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("zurich_ch", "Zurich", "Switzerland", 47.3769, 8.5417, 2.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("rome_it", "Rome", "Italy", 41.9028, 12.4964, 2.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("milan_it", "Milan", "Italy", 45.4642, 9.1900, 2.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("madrid_es", "Madrid", "Spain", 40.4168, -3.7038, 2.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("barcelona_es", "Barcelona", "Spain", 41.3851, 2.1734, 2.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("dublin_ie", "Dublin", "Ireland", 53.3498, -6.2603, 1.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("stockholm_se", "Stockholm", "Sweden", 59.3293, 18.0686, 2.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("oslo_no", "Oslo", "Norway", 59.9139, 10.7522, 2.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("copenhagen_dk", "Copenhagen", "Denmark", 55.6761, 12.5683, 2.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("warsaw_pl", "Warsaw", "Poland", 52.2297, 21.0122, 2.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("athens_gr", "Athens", "Greece", 37.9838, 23.7275, 3.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("sarajevo_ba", "Sarajevo", "Bosnia and Herzegovina", 43.8563, 18.4131, 2.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("moscow_ru", "Moscow", "Russia", 55.7558, 37.6173, 3.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("kazan_ru", "Kazan", "Russia", 55.8304, 49.0661, 3.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("grozny_ru", "Grozny", "Russia", 43.3169, 45.6885, 3.0, false, CalculationMethod.MWL, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("newyork_us", "New York", "United States", 40.7128, -74.0060, -4.0, false, CalculationMethod.ISNA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("chicago_us", "Chicago", "United States", 41.8781, -87.6298, -5.0, false, CalculationMethod.ISNA, Madhab.HANAFI),
      CityLocation("houston_us", "Houston", "United States", 29.7604, -95.3698, -5.0, false, CalculationMethod.ISNA, Madhab.HANAFI),
      CityLocation("losangeles_us", "Los Angeles", "United States", 34.0522, -118.2437, -7.0, false, CalculationMethod.ISNA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("dallas_us", "Dallas", "United States", 32.7767, -96.7970, -5.0, false, CalculationMethod.ISNA, Madhab.HANAFI),
      CityLocation("washington_us", "Washington D.C.", "United States", 38.9072, -77.0369, -4.0, false, CalculationMethod.ISNA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("philadelphia_us", "Philadelphia", "United States", 39.9526, -75.1652, -4.0, false, CalculationMethod.ISNA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("atlanta_us", "Atlanta", "United States", 33.7490, -84.3880, -4.0, false, CalculationMethod.ISNA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("detroit_us", "Detroit", "United States", 42.3314, -83.0458, -4.0, false, CalculationMethod.ISNA, Madhab.HANAFI),
      CityLocation("sanfrancisco_us", "San Francisco", "United States", 37.7749, -122.4194, -7.0, false, CalculationMethod.ISNA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("miami_us", "Miami", "United States", 25.7617, -80.1918, -4.0, false, CalculationMethod.ISNA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("toronto_ca", "Toronto", "Canada", 43.6532, -79.3832, -4.0, false, CalculationMethod.ISNA, Madhab.HANAFI),
      CityLocation("montreal_ca", "Montreal", "Canada", 45.5017, -73.5673, -4.0, false, CalculationMethod.ISNA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("vancouver_ca", "Vancouver", "Canada", 49.2827, -123.1207, -7.0, false, CalculationMethod.ISNA, Madhab.HANAFI),
      CityLocation("calgary_ca", "Calgary", "Canada", 51.0447, -114.0719, -6.0, false, CalculationMethod.ISNA, Madhab.HANAFI),
      CityLocation("ottawa_ca", "Ottawa", "Canada", 45.4215, -75.6972, -4.0, false, CalculationMethod.ISNA, Madhab.HANAFI),
      CityLocation("mexicocity_mx", "Mexico City", "Mexico", 19.4326, -99.1332, -6.0, false, CalculationMethod.ISNA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("saopaulo_br", "Sao Paulo", "Brazil", -23.5505, -46.6333, -3.0, false, CalculationMethod.ISNA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("buenosaires_ar", "Buenos Aires", "Argentina", -34.6037, -58.3816, -3.0, false, CalculationMethod.ISNA, Madhab.SHAFI_MALIKI_HANBALI),
      CityLocation("sydney_au", "Sydney", "Australia", -33.8688, 151.2093, 10.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("melbourne_au", "Melbourne", "Australia", -37.8136, 144.9631, 10.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("brisbane_au", "Brisbane", "Australia", -27.4698, 153.0251, 10.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("perth_au", "Perth", "Australia", -31.9505, 115.8605, 8.0, false, CalculationMethod.MWL, Madhab.HANAFI),
      CityLocation("auckland_nz", "Auckland", "New Zealand", -36.8485, 174.7633, 12.0, false, CalculationMethod.MWL, Madhab.HANAFI)
    )
  }
}
