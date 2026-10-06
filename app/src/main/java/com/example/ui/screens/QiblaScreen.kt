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
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculation.QiblaCalculator
import com.example.model.CityLocation
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun QiblaScreen(
  city: CityLocation,
  qiblaBearingDegrees: Float,
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
        .padding(horizontal = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Top Info Pill: City & Distance
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
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

      // Compass Visual Area
      Box(
        modifier = Modifier
          .size(280.dp)
          .testTag("qibla_compass_dial"),
        contentAlignment = Alignment.Center
      ) {
        // Outer Compass Ring
        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.surface,
          border = androidx.compose.foundation.BorderStroke(
            width = if (isAligned) 3.dp else 1.5.dp,
            color = if (isAligned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
          ),
          shadowElevation = if (isAligned) 6.dp else 1.dp,
          modifier = Modifier.fillMaxSize()
        ) {
          Box(contentAlignment = Alignment.Center) {
            // Rotating Compass Rose (rotates opposite to phone heading)
            Box(
              modifier = Modifier
                .fillMaxSize()
                .rotate(-smoothHeading)
            ) {
              // Cardinal Labels: N, E, S, W
              Text(
                text = "N",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                  .align(Alignment.TopCenter)
                  .padding(top = 10.dp)
              )
              Text(
                text = "S",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                  .align(Alignment.BottomCenter)
                  .padding(bottom = 10.dp)
              )
              Text(
                text = "E",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                  .align(Alignment.CenterEnd)
                  .padding(end = 12.dp)
              )
              Text(
                text = "W",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                  .align(Alignment.CenterStart)
                  .padding(start = 12.dp)
              )

              // Kaaba Marker Needle (pointing at qiblaBearingDegrees on the rose)
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .rotate(qiblaBearingDegrees)
              ) {
                Column(
                  modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 34.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  // Kaaba Cube Symbol
                  Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF1E211E),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD4B37A)),
                    modifier = Modifier.size(24.dp)
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Text(
                        text = "🕋",
                        fontSize = 13.sp
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(2.dp))

                  Text(
                    text = "QIBLA",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 8.sp,
                      letterSpacing = 0.5.sp
                    ),
                    color = if (isAligned) MaterialTheme.colorScheme.primary else Color(0xFFD4B37A)
                  )
                }
              }
            }

            // Fixed Phone Direction Needle (pointing straight ahead up)
            Box(
              modifier = Modifier
                .width(3.dp)
                .height(36.dp)
                .align(Alignment.TopCenter)
                .padding(top = 2.dp)
                .background(
                  if (isAligned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                  RoundedCornerShape(2.dp)
                )
            )

            // Center Degree readout
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "${smoothHeading.toInt()}°",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (isAligned) "FACING KAABA" else "QIBLA ${qiblaBearingDegrees.toInt()}°",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp
                ),
                color = if (isAligned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      // Bottom Status & Calibration Prompt
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (isAligned) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
              )
              Text(
                text = "Perfect Alignment with the Kaaba",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        } else {
          Text(
            text = "Rotate your phone until the needle matches the Kaaba marker.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Tip: Move your phone in a figure-8 motion if compass needs calibration.",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
          textAlign = TextAlign.Center
        )
      }
    }
  }
}
