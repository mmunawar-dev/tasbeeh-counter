package com.example.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculation.QiblaCalculator
import com.example.model.CityLocation
import com.example.model.CompassStyle
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun QiblaScreen(
  city: CityLocation,
  qiblaBearingDegrees: Float,
  selectedStyle: CompassStyle,
  onSelectStyle: (CompassStyle) -> Unit,
  onBack: () -> Unit
) {
  val context = LocalContext.current
  var currentHeading by remember { mutableStateOf(0f) }

  // Listen to device compass sensor
  DisposableEffect(Unit) {
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    val rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
      ?: sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION)

    val listener = object : SensorEventListener {
      override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
          val rotationMatrix = FloatArray(9)
          SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
          val orientation = FloatArray(3)
          SensorManager.getOrientation(rotationMatrix, orientation)
          val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
          currentHeading = (azimuth + 360f) % 360f
        } else if (event.sensor.type == Sensor.TYPE_ORIENTATION) {
          currentHeading = event.values[0]
        }
      }
      override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    rotationSensor?.let {
      sensorManager?.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI)
    }

    onDispose {
      sensorManager?.unregisterListener(listener)
    }
  }

  val smoothHeading by animateFloatAsState(
    targetValue = currentHeading,
    animationSpec = spring(dampingRatio = 0.75f, stiffness = 300f),
    label = "compass_rotation"
  )

  // Relative angle to the Kaaba from current phone orientation
  val relativeQiblaAngle = (qiblaBearingDegrees - smoothHeading + 360f) % 360f
  val isAligned = relativeQiblaAngle in 356f..360f || relativeQiblaAngle in 0f..4f

  val distanceKm = remember(city) {
    QiblaCalculator.calculateDistanceKm(city.latitude, city.longitude)
  }

  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    topBar = {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("qibla_back_button")
        ) {
          Text(
            text = "←",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
          )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Column {
          Text(
            text = "Qibla Direction",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "القبلة • Direction to Holy Kaaba",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(horizontal = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // 1. Top Info Pill: City & Distance
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.LocationOn,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(18.dp)
            )
            Column {
              Text(
                text = city.displayName,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Bearing: ${qiblaBearingDegrees.toInt()}° from North",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.secondaryContainer
          ) {
            Text(
              text = "$distanceKm km",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              ),
              color = MaterialTheme.colorScheme.onSecondaryContainer,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }

      // 2. Compass Style Selector Carousel
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
      ) {
        Text(
          text = "COMPASS & ARROW THEMES",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            fontSize = 10.5.sp
          ),
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(CompassStyle.values()) { style ->
            val isSelected = style == selectedStyle
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
              border = if (isSelected) {
                androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
              } else {
                androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
              },
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .clickable { onSelectStyle(style) }
                .testTag("compass_style_${style.name.lowercase()}")
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Text(
                  text = when (style) {
                    CompassStyle.CLASSIC_GOLD -> "✨"
                    CompassStyle.EMERALD_MINIMAL -> "🧭"
                    CompassStyle.ASTROLABE -> "📜"
                    CompassStyle.KAABA_ARROW -> "🎯"
                    CompassStyle.NIGHT_CELESTIAL -> "🌙"
                  },
                  fontSize = 13.sp
                )
                Text(
                  text = style.title,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 11.sp
                  ),
                  color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }

      // 3. Compass Visual Dial Area
      Box(
        modifier = Modifier
          .size(290.dp)
          .testTag("qibla_compass_dial"),
        contentAlignment = Alignment.Center
      ) {
        // Draw the specific compass dial ring based on selected style
        CompassDialCanvas(
          style = selectedStyle,
          isAligned = isAligned,
          modifier = Modifier.fillMaxSize()
        )

        // Rotating Compass Rose (rotates with device heading)
        Box(
          modifier = Modifier
            .fillMaxSize()
            .rotate(-smoothHeading)
        ) {
          // Cardinal Labels: N, S, E, W
          Text(
            text = "N",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              fontSize = 13.sp
            ),
            color = if (selectedStyle == CompassStyle.NIGHT_CELESTIAL) Color(0xFF55E6B7) else Color(0xFFE53935),
            modifier = Modifier
              .align(Alignment.TopCenter)
              .padding(top = 10.dp)
          )
          Text(
            text = "S",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = if (selectedStyle == CompassStyle.NIGHT_CELESTIAL) Color(0xFF88A096) else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .padding(bottom = 10.dp)
          )
          Text(
            text = "E",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = if (selectedStyle == CompassStyle.NIGHT_CELESTIAL) Color(0xFF88A096) else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
              .align(Alignment.CenterEnd)
              .padding(end = 12.dp)
          )
          Text(
            text = "W",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = if (selectedStyle == CompassStyle.NIGHT_CELESTIAL) Color(0xFF88A096) else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
              .align(Alignment.CenterStart)
              .padding(start = 12.dp)
          )

          // Custom Needle & Arrow pointing at qiblaBearingDegrees on the rotating rose
          Box(
            modifier = Modifier
              .fillMaxSize()
              .rotate(qiblaBearingDegrees)
          ) {
            CompassNeedleView(
              style = selectedStyle,
              isAligned = isAligned,
              modifier = Modifier.fillMaxSize()
            )
          }
        }

        // Fixed Phone Forward Indicator at top (pointing straight up)
        PhoneForwardIndicator(
          style = selectedStyle,
          isAligned = isAligned,
          modifier = Modifier.align(Alignment.TopCenter)
        )

        // Center Degree readout & alignment indicator
        Surface(
          shape = CircleShape,
          color = when (selectedStyle) {
            CompassStyle.NIGHT_CELESTIAL -> Color(0xFF14241C)
            CompassStyle.CLASSIC_GOLD -> Color(0xFFFFFDF8)
            CompassStyle.ASTROLABE -> Color(0xFFFAF6EE)
            else -> MaterialTheme.colorScheme.surface
          },
          border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isAligned) Color(0xFF1B8A5A) else MaterialTheme.colorScheme.outlineVariant
          ),
          shadowElevation = 4.dp,
          modifier = Modifier.size(92.dp)
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(4.dp)
          ) {
            Text(
              text = "${smoothHeading.toInt()}°",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
              ),
              color = if (selectedStyle == CompassStyle.NIGHT_CELESTIAL) Color(0xFF55E6B7) else MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = if (isAligned) "FACING" else "QIBLA",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                fontSize = 8.5.sp
              ),
              color = if (isAligned) Color(0xFF1B8A5A) else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = "${qiblaBearingDegrees.toInt()}°",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 9.sp
              ),
              color = if (isAligned) Color(0xFF1B8A5A) else MaterialTheme.colorScheme.primary
            )
          }
        }
      }

      // 4. Bottom Status & Calibration Prompt
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (isAligned) {
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFE8F7F0),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFF249D6A)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF167B51),
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "ALIGNED WITH HOLY KAABA (مكة المكرمة)",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp
                ),
                color = Color(0xFF167B51)
              )
            }
          }
        } else {
          val turnDifference = (relativeQiblaAngle + 360f) % 360f
          val turnDirection = if (turnDifference in 1f..180f) "right ➔" else "left ⬅"
          val degreesRemaining = if (turnDifference <= 180f) turnDifference.toInt() else (360 - turnDifference).toInt()

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Outlined.Explore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Rotate phone $turnDirection ($degreesRemaining° to align)",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "Calibrate: Wave phone in a figure-8 motion away from magnets",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
          textAlign = TextAlign.Center
        )
      }
    }
  }
}

/**
 * Draws the custom compass bezel, background gradient, and tick marks for each style
 */
@Composable
private fun CompassDialCanvas(
  style: CompassStyle,
  isAligned: Boolean,
  modifier: Modifier = Modifier
) {
  Canvas(modifier = modifier) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension / 2f - 4.dp.toPx()

    when (style) {
      CompassStyle.CLASSIC_GOLD -> {
        // Gilded brass background & double gold rings
        drawCircle(
          color = Color(0xFFFBF8F0),
          radius = radius,
          center = center
        )
        drawCircle(
          color = if (isAligned) Color(0xFF1B8A5A) else Color(0xFFD4AF37),
          radius = radius,
          center = center,
          style = Stroke(width = if (isAligned) 4.dp.toPx() else 3.dp.toPx())
        )
        drawCircle(
          color = Color(0xFFE8D49B),
          radius = radius - 8.dp.toPx(),
          center = center,
          style = Stroke(width = 1.dp.toPx())
        )

        // Degree ticks every 15 degrees
        for (deg in 0 until 360 step 15) {
          val rad = Math.toRadians(deg.toDouble())
          val isMajor = deg % 90 == 0
          val isMedium = deg % 30 == 0
          val tickLen = if (isMajor) 12.dp.toPx() else if (isMedium) 8.dp.toPx() else 4.dp.toPx()
          val startR = radius - 8.dp.toPx()
          val endR = startR - tickLen

          val startX = center.x + (startR * sin(rad)).toFloat()
          val startY = center.y - (startR * cos(rad)).toFloat()
          val endX = center.x + (endR * sin(rad)).toFloat()
          val endY = center.y - (endR * cos(rad)).toFloat()

          drawLine(
            color = if (isMajor) Color(0xFF996515) else Color(0xFFD4B37A),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = if (isMajor) 2.dp.toPx() else 1.dp.toPx()
          )
        }
      }

      CompassStyle.EMERALD_MINIMAL -> {
        // High-tech modern emerald ring with mint ticks
        drawCircle(
          color = Color(0xFFF4F9F6),
          radius = radius,
          center = center
        )
        drawCircle(
          color = if (isAligned) Color(0xFF1B8A5A) else Color(0xFF2C6B55),
          radius = radius,
          center = center,
          style = Stroke(width = 2.5.dp.toPx())
        )

        for (deg in 0 until 360 step 30) {
          val rad = Math.toRadians(deg.toDouble())
          val tickLen = if (deg % 90 == 0) 10.dp.toPx() else 5.dp.toPx()
          val startR = radius - 4.dp.toPx()
          val endR = startR - tickLen

          val startX = center.x + (startR * sin(rad)).toFloat()
          val startY = center.y - (startR * cos(rad)).toFloat()
          val endX = center.x + (endR * sin(rad)).toFloat()
          val endY = center.y - (endR * cos(rad)).toFloat()

          drawLine(
            color = if (deg % 90 == 0) Color(0xFF16523F) else Color(0xFF6BB599),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
          )
        }
      }

      CompassStyle.ASTROLABE -> {
        // Antique Islamic brass astrolabe ring with quadrant arcs
        drawCircle(
          color = Color(0xFFF5EFE1),
          radius = radius,
          center = center
        )
        drawCircle(
          color = Color(0xFF8B6B38),
          radius = radius,
          center = center,
          style = Stroke(width = 3.dp.toPx())
        )
        drawCircle(
          color = Color(0xFFB89B60),
          radius = radius - 12.dp.toPx(),
          center = center,
          style = Stroke(width = 1.2.dp.toPx())
        )

        // Astrolabe geometric arcs
        for (deg in 0 until 360 step 10) {
          val rad = Math.toRadians(deg.toDouble())
          val isMajor = deg % 30 == 0
          val tickLen = if (isMajor) 10.dp.toPx() else 4.dp.toPx()
          val startR = radius - 2.dp.toPx()
          val endR = startR - tickLen

          val startX = center.x + (startR * sin(rad)).toFloat()
          val startY = center.y - (startR * cos(rad)).toFloat()
          val endX = center.x + (endR * sin(rad)).toFloat()
          val endY = center.y - (endR * cos(rad)).toFloat()

          drawLine(
            color = if (isMajor) Color(0xFF6E4D1B) else Color(0xFFA6854F),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 1.dp.toPx()
          )
        }
      }

      CompassStyle.KAABA_ARROW -> {
        // High visibility bold dial
        drawCircle(
          color = Color(0xFFFAFAFA),
          radius = radius,
          center = center
        )
        drawCircle(
          color = if (isAligned) Color(0xFF1B8A5A) else Color(0xFF424242),
          radius = radius,
          center = center,
          style = Stroke(width = 3.dp.toPx())
        )
        drawCircle(
          color = if (isAligned) Color(0xFF55E6B7).copy(alpha = 0.3f) else Color(0xFFE0E0E0),
          radius = radius - 6.dp.toPx(),
          center = center,
          style = Stroke(width = 2.dp.toPx())
        )

        for (deg in 0 until 360 step 30) {
          val rad = Math.toRadians(deg.toDouble())
          val tickLen = 8.dp.toPx()
          val startR = radius - 6.dp.toPx()
          val endR = startR - tickLen

          val startX = center.x + (startR * sin(rad)).toFloat()
          val startY = center.y - (startR * cos(rad)).toFloat()
          val endX = center.x + (endR * sin(rad)).toFloat()
          val endY = center.y - (endR * cos(rad)).toFloat()

          drawLine(
            color = Color(0xFF616161),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 1.5.dp.toPx()
          )
        }
      }

      CompassStyle.NIGHT_CELESTIAL -> {
        // Deep midnight obsidian celestial ring with starlight
        drawCircle(
          color = Color(0xFF0F1A15),
          radius = radius,
          center = center
        )
        drawCircle(
          color = if (isAligned) Color(0xFF55E6B7) else Color(0xFF264C3B),
          radius = radius,
          center = center,
          style = Stroke(width = 2.5.dp.toPx())
        )
        drawCircle(
          color = Color(0xFF1A382B),
          radius = radius - 10.dp.toPx(),
          center = center,
          style = Stroke(width = 1.dp.toPx())
        )

        // Celestial stars on ring
        for (deg in 0 until 360 step 20) {
          val rad = Math.toRadians(deg.toDouble())
          val starR = radius - 6.dp.toPx()
          val sx = center.x + (starR * sin(rad)).toFloat()
          val sy = center.y - (starR * cos(rad)).toFloat()
          drawCircle(
            color = if (deg % 60 == 0) Color(0xFF72DFBA) else Color(0xFF386151),
            radius = if (deg % 60 == 0) 2.dp.toPx() else 1.2.dp.toPx(),
            center = Offset(sx, sy)
          )
        }
      }
    }
  }
}

/**
 * Draws the customized Needle & Arrow pointing at the Kaaba on the compass rose
 */
@Composable
private fun CompassNeedleView(
  style: CompassStyle,
  isAligned: Boolean,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier) {
    when (style) {
      CompassStyle.CLASSIC_GOLD -> {
        // Ornate dual-tone golden needle with Kaaba emblem
        Column(
          modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 28.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Gilded arrow head
          Canvas(modifier = Modifier.size(24.dp, 28.dp)) {
            val path = Path().apply {
              moveTo(size.width / 2f, 0f)
              lineTo(size.width, size.height)
              lineTo(size.width / 2f, size.height * 0.7f)
              lineTo(0f, size.height)
              close()
            }
            drawPath(
              path = path,
              color = if (isAligned) Color(0xFF1B8A5A) else Color(0xFFD4AF37)
            )
          }

          Spacer(modifier = Modifier.height(2.dp))

          // Kaaba Emblem Box
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFF191D1A),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFD4B37A)),
            shadowElevation = 3.dp,
            modifier = Modifier.size(26.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(text = "🕋", fontSize = 14.sp)
            }
          }

          Text(
            text = "QIBLA",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.ExtraBold,
              fontSize = 8.5.sp,
              letterSpacing = 0.5.sp
            ),
            color = if (isAligned) Color(0xFF1B8A5A) else Color(0xFF996515)
          )
        }
      }

      CompassStyle.EMERALD_MINIMAL -> {
        // Sleek aerodynamic arrow needle with emerald gradient
        Column(
          modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 26.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Canvas(modifier = Modifier.size(26.dp, 32.dp)) {
            val path = Path().apply {
              moveTo(size.width / 2f, 0f)
              lineTo(size.width, size.height)
              lineTo(size.width / 2f, size.height * 0.75f)
              lineTo(0f, size.height)
              close()
            }
            drawPath(
              path = path,
              color = if (isAligned) Color(0xFF249D6A) else Color(0xFF185D46)
            )
          }

          Spacer(modifier = Modifier.height(3.dp))

          Surface(
            shape = CircleShape,
            color = Color(0xFF0F3B2E),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF55E6B7)),
            modifier = Modifier.size(24.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(text = "🕋", fontSize = 13.sp)
            }
          }
        }
      }

      CompassStyle.ASTROLABE -> {
        // Antique filigree Islamic Astrolabe pointer
        Column(
          modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 28.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Canvas(modifier = Modifier.size(22.dp, 30.dp)) {
            val path = Path().apply {
              moveTo(size.width / 2f, 0f)
              lineTo(size.width, size.height * 0.8f)
              lineTo(size.width * 0.6f, size.height)
              lineTo(size.width / 2f, size.height * 0.7f)
              lineTo(size.width * 0.4f, size.height)
              lineTo(0f, size.height * 0.8f)
              close()
            }
            drawPath(
              path = path,
              color = if (isAligned) Color(0xFF1B8A5A) else Color(0xFF78541D)
            )
          }

          Spacer(modifier = Modifier.height(2.dp))

          Surface(
            shape = RoundedCornerShape(5.dp),
            color = Color(0xFF2C2417),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBB178)),
            modifier = Modifier.size(26.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(text = "🕋", fontSize = 14.sp)
            }
          }

          Text(
            text = "مكة",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp
            ),
            color = Color(0xFF78541D)
          )
        }
      }

      CompassStyle.KAABA_ARROW -> {
        // High visibility bold 3D directional arrow with Kaaba symbol at arrowhead
        Column(
          modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 22.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Large 3D Arrowhead
          Canvas(modifier = Modifier.size(32.dp, 36.dp)) {
            val path = Path().apply {
              moveTo(size.width / 2f, 0f)
              lineTo(size.width, size.height)
              lineTo(size.width / 2f, size.height * 0.7f)
              lineTo(0f, size.height)
              close()
            }
            drawPath(
              path = path,
              color = if (isAligned) Color(0xFF1B8A5A) else Color(0xFF2E7D32)
            )
          }

          Spacer(modifier = Modifier.height(2.dp))

          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFF1E211E),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFFD700)),
            shadowElevation = 4.dp,
            modifier = Modifier.size(28.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(text = "🕋", fontSize = 15.sp)
            }
          }

          Text(
            text = "KAABA",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.ExtraBold,
              fontSize = 8.5.sp,
              letterSpacing = 0.5.sp
            ),
            color = if (isAligned) Color(0xFF1B8A5A) else Color(0xFF2E7D32)
          )
        }
      }

      CompassStyle.NIGHT_CELESTIAL -> {
        // Glowing celestial arrow needle
        Column(
          modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 26.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Canvas(modifier = Modifier.size(24.dp, 32.dp)) {
            val path = Path().apply {
              moveTo(size.width / 2f, 0f)
              lineTo(size.width, size.height)
              lineTo(size.width / 2f, size.height * 0.75f)
              lineTo(0f, size.height)
              close()
            }
            drawPath(
              path = path,
              color = if (isAligned) Color(0xFF55E6B7) else Color(0xFF388E6E)
            )
          }

          Spacer(modifier = Modifier.height(2.dp))

          Surface(
            shape = CircleShape,
            color = Color(0xFF0F1E17),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFF72DFBA)),
            shadowElevation = 6.dp,
            modifier = Modifier.size(26.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(text = "🕋", fontSize = 14.sp)
            }
          }

          Text(
            text = "★ QIBLA ★",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 8.sp,
              letterSpacing = 0.8.sp
            ),
            color = Color(0xFF55E6B7)
          )
        }
      }
    }
  }
}

/**
 * Fixed indicator at the top of the dial representing the phone's current direction
 */
@Composable
private fun PhoneForwardIndicator(
  style: CompassStyle,
  isAligned: Boolean,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .padding(top = 2.dp)
      .width(if (style == CompassStyle.KAABA_ARROW) 4.dp else 3.dp)
      .height(if (style == CompassStyle.KAABA_ARROW) 36.dp else 32.dp)
      .background(
        color = if (isAligned) {
          Color(0xFF1B8A5A)
        } else when (style) {
          CompassStyle.CLASSIC_GOLD -> Color(0xFFD4AF37)
          CompassStyle.NIGHT_CELESTIAL -> Color(0xFF55E6B7)
          else -> MaterialTheme.colorScheme.primary
        },
        shape = RoundedCornerShape(2.dp)
      )
  )
}
