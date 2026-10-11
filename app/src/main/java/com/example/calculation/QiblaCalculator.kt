package com.example.calculation

import kotlin.math.*

object QiblaCalculator {
  // Coordinates of the Kaaba in Makkah
  private const val KAABA_LAT = 21.422487
  private const val KAABA_LNG = 39.826206

  /**
   * Calculates the bearing in degrees (0..360) from current location to the Kaaba.
   */
  fun calculateQiblaBearing(lat: Double, lng: Double): Float {
    val phi1 = Math.toRadians(lat)
    val phi2 = Math.toRadians(KAABA_LAT)
    val deltaLambda = Math.toRadians(KAABA_LNG - lng)

    val y = sin(deltaLambda) * cos(phi2)
    val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(deltaLambda)

    var bearing = Math.toDegrees(atan2(y, x))
    bearing = (bearing + 360.0) % 360.0
    return bearing.toFloat()
  }

  /**
   * Calculates approximate distance to Makkah in kilometers.
   */
  fun calculateDistanceKm(lat: Double, lng: Double): Int {
    val earthRadiusKm = 6371.0
    val dLat = Math.toRadians(KAABA_LAT - lat)
    val dLng = Math.toRadians(KAABA_LNG - lng)

    val a = sin(dLat / 2) * sin(dLat / 2) +
      cos(Math.toRadians(lat)) * cos(Math.toRadians(KAABA_LAT)) *
      sin(dLng / 2) * sin(dLng / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return (earthRadiusKm * c).toInt()
  }
}
