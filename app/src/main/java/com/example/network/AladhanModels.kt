package com.example.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AladhanResponse(
  val code: Int = 200,
  val status: String = "OK",
  val data: AladhanData? = null
)

@JsonClass(generateAdapter = true)
data class AladhanData(
  val timings: AladhanTimings,
  val date: AladhanDateMetadata,
  val meta: AladhanMeta? = null
)

@JsonClass(generateAdapter = true)
data class AladhanTimings(
  @Json(name = "Fajr") val fajr: String = "",
  @Json(name = "Sunrise") val sunrise: String = "",
  @Json(name = "Dhuhr") val dhuhr: String = "",
  @Json(name = "Asr") val asr: String = "",
  @Json(name = "Sunset") val sunset: String = "",
  @Json(name = "Maghrib") val maghrib: String = "",
  @Json(name = "Isha") val isha: String = "",
  @Json(name = "Imsak") val imsak: String = "",
  @Json(name = "Midnight") val midnight: String = ""
)

@JsonClass(generateAdapter = true)
data class AladhanDateMetadata(
  val readable: String = "",
  val timestamp: String = "",
  val hijri: AladhanHijri? = null,
  val gregorian: AladhanGregorian? = null
)

@JsonClass(generateAdapter = true)
data class AladhanHijri(
  val date: String = "",
  val day: String = "",
  val month: AladhanHijriMonth? = null,
  val year: String = ""
)

@JsonClass(generateAdapter = true)
data class AladhanHijriMonth(
  val number: Int = 1,
  val en: String = "",
  val ar: String = ""
)

@JsonClass(generateAdapter = true)
data class AladhanGregorian(
  val date: String = "",
  val day: String = "",
  val weekday: AladhanWeekday? = null,
  val year: String = ""
)

@JsonClass(generateAdapter = true)
data class AladhanWeekday(
  val en: String = ""
)

@JsonClass(generateAdapter = true)
data class AladhanMeta(
  val latitude: Double? = null,
  val longitude: Double? = null,
  val timezone: String? = null
)
