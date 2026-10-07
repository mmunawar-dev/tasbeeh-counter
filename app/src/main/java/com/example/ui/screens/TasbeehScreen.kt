package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DhikrPreset
import com.example.model.TasbeehStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbeehScreen(
  currentDhikr: DhikrPreset,
  count: Int,
  totalTarget: Int,
  laps: Int,
  selectedStyle: TasbeehStyle,
  isHapticEnabled: Boolean,
  isClickSoundEnabled: Boolean,
  onIncrement: (Context) -> Unit,
  onReset: () -> Unit,
  onSelectDhikr: (DhikrPreset) -> Unit,
  onSelectStyle: (TasbeehStyle) -> Unit,
  onSetTarget: (Int) -> Unit,
  onToggleHaptic: () -> Unit,
  onToggleClickSound: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val isDark = isSystemInDarkTheme()

  var showDhikrPicker by remember { mutableStateOf(false) }
  var showTargetDialog by remember { mutableStateOf(false) }
  var showCelebration by remember { mutableStateOf(false) }

  // Detect completion pulse
  LaunchedEffect(count, laps) {
    if (count == 0 && laps > 0) {
      showCelebration = true
      delay(2200)
      showCelebration = false
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Digital Tasbeeh",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Lap $laps • Target $totalTarget",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        actions = {
          // Audio click sound toggle
          IconButton(onClick = onToggleClickSound) {
            Icon(
              imageVector = if (isClickSoundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeMute,
              contentDescription = "Click sound toggle",
              tint = if (isClickSoundEnabled) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          // Vibration toggle
          IconButton(onClick = onToggleHaptic) {
            Icon(
              imageVector = Icons.Default.Vibration,
              contentDescription = "Haptic feedback",
              tint = if (isHapticEnabled) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          // Dhikr catalog
          IconButton(onClick = { showDhikrPicker = true }) {
            Icon(
              imageVector = Icons.Default.List,
              contentDescription = "Select Dhikr"
            )
          }
          // Reset
          IconButton(onClick = onReset) {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "Reset count"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    modifier = modifier
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top Style Switcher bar
      TasbeehStyleSelector(
        selectedStyle = selectedStyle,
        onSelectStyle = onSelectStyle,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      )

      // Active Dhikr Card
      ActiveDhikrCard(
        currentDhikr = currentDhikr,
        count = count,
        totalTarget = totalTarget,
        onChangeDhikrClick = { showDhikrPicker = true },
        onChangeTargetClick = { showTargetDialog = true },
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp)
      )

      // Target Completion Celebration Banner
      AnimatedVisibility(visible = showCelebration) {
        Surface(
          color = Color(0xFFD1FAE5),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Default.Celebration,
              contentDescription = null,
              tint = Color(0xFF047857)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Masha'Allah! Target $totalTarget completed! Starting Lap ${laps + 1}",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF065F46)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Main Interactive Visual according to selectedStyle
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
      ) {
        when (selectedStyle) {
          TasbeehStyle.SEEDS, TasbeehStyle.CIRCULAR -> {
            SeedTasbeehInteractive(
              count = count,
              target = totalTarget,
              onTap = { onIncrement(context) }
            )
          }
          TasbeehStyle.HAND_TASBEEH, TasbeehStyle.PREMIUM_DARK, TasbeehStyle.MINIMAL -> {
            HandTasbeehInteractive(
              count = count,
              target = totalTarget,
              dhikrName = currentDhikr.transliteration,
              onTap = { onIncrement(context) }
            )
          }
          TasbeehStyle.BALLOONS_LINE -> {
            BalloonsLineTasbeehInteractive(
              count = count,
              target = totalTarget,
              onTap = { onIncrement(context) }
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Tap instructions hint
      Text(
        text = "Tap bead, button, or balloon to increment Dhikr",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
      )

      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  // Dhikr catalog bottom sheet
  if (showDhikrPicker) {
    ModalBottomSheet(
      onDismissRequest = { showDhikrPicker = false },
      sheetState = rememberModalBottomSheetState()
    ) {
      DhikrCatalogSheet(
        selectedDhikr = currentDhikr,
        onSelect = {
          onSelectDhikr(it)
          showDhikrPicker = false
        }
      )
    }
  }

  // Target customization bottom sheet
  if (showTargetDialog) {
    ModalBottomSheet(
      onDismissRequest = { showTargetDialog = false },
      sheetState = rememberModalBottomSheetState()
    ) {
      TargetPickerSheet(
        currentTarget = totalTarget,
        onSelectTarget = {
          onSetTarget(it)
          showTargetDialog = false
        }
      )
    }
  }
}

// -------------------------------------------------------------
// 1. STYLE SELECTOR
// -------------------------------------------------------------
@Composable
private fun TasbeehStyleSelector(
  selectedStyle: TasbeehStyle,
  onSelectStyle: (TasbeehStyle) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    Text(
      text = "TASBEEH MODE",
      style = MaterialTheme.typography.labelSmall,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.primary,
      letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(6.dp))
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      val styles = listOf(
        TasbeehStyle.SEEDS,
        TasbeehStyle.HAND_TASBEEH,
        TasbeehStyle.BALLOONS_LINE
      )

      styles.forEach { style ->
        val isSelected = style == selectedStyle
        Surface(
          onClick = { onSelectStyle(style) },
          shape = RoundedCornerShape(12.dp),
          color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
          } else {
            androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
          }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            if (isSelected) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
            }
            Column {
              Text(
                text = style.title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = when (style) {
                  TasbeehStyle.SEEDS -> "Olive wood beads"
                  TasbeehStyle.HAND_TASBEEH -> "Physical hand clicker"
                  TasbeehStyle.BALLOONS_LINE -> "Floating balloons line"
                  else -> style.subtitle
                },
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 2. ACTIVE DHIKR CARD
// -------------------------------------------------------------
@Composable
private fun ActiveDhikrCard(
  currentDhikr: DhikrPreset,
  count: Int,
  totalTarget: Int,
  onChangeDhikrClick: () -> Unit,
  onChangeTargetClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isDark = isSystemInDarkTheme()
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          color = Color(0xFF0F766E).copy(alpha = 0.15f),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = currentDhikr.transliteration.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F766E),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          TextButton(onClick = onChangeDhikrClick) {
            Icon(
              imageVector = Icons.Default.Edit,
              contentDescription = null,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Change", style = MaterialTheme.typography.labelMedium)
          }
          TextButton(onClick = onChangeTargetClick) {
            Text(text = "/$totalTarget", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Arabic Calligraphy with distinct glow
      Text(
        text = currentDhikr.arabicText,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A),
        lineHeight = 38.sp
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = currentDhikr.transliteration,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
      )

      Text(
        text = "“${currentDhikr.translation}”",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Progress bar
      val progress = (count.toFloat() / totalTarget.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(8.dp)
          .clip(RoundedCornerShape(4.dp)),
        color = Color(0xFF10B981),
        trackColor = if (isDark) Color(0xFF334155) else Color(0xFFE2E8F0)
      )
    }
  }
}

// -------------------------------------------------------------
// 3. STYLE 1: TASBEEH SEEDS (Wood / Olive Beads)
// -------------------------------------------------------------
@Composable
private fun SeedTasbeehInteractive(
  count: Int,
  target: Int,
  onTap: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isDark = isSystemInDarkTheme()
  val scope = rememberCoroutineScope()
  val scaleAnim = remember { Animatable(1f) }
  var flashEffect by remember { mutableStateOf(false) }

  val beadCount = 33
  val activeBeadIndex = count % beadCount

  Card(
    modifier = modifier
      .fillMaxWidth()
      .height(340.dp),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isDark) Color(0xFF121B24) else Color(0xFFFBF8F3)
    ),
    border = androidx.compose.foundation.BorderStroke(
      1.5.dp,
      if (isDark) Color(0xFF243342) else Color(0xFFE8DFD1)
    )
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
          detectTapGestures(
            onPress = {
              scope.launch {
                scaleAnim.animateTo(0.96f, spring(stiffness = Spring.StiffnessHigh))
                scaleAnim.animateTo(1f, spring(stiffness = Spring.StiffnessMedium))
              }
              flashEffect = true
              onTap()
            }
          )
        },
      contentAlignment = Alignment.Center
    ) {
      // Background subtle wood grain circle
      Canvas(
        modifier = Modifier
          .size(290.dp)
          .scale(scaleAnim.value)
      ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.width * 0.40f

        // Draw bead string cord
        drawCircle(
          color = if (isDark) Color(0xFF4B382A) else Color(0xFFB89876),
          radius = radius,
          center = center,
          style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw individual beads
        for (i in 0 until beadCount) {
          val angle = (2.0 * PI / beadCount * i) - (PI / 2.0)
          val bx = (center.x + radius * cos(angle)).toFloat()
          val by = (center.y + radius * sin(angle)).toFloat()

          val isCurrentBead = (i == activeBeadIndex)
          val isPassedBead = (i < activeBeadIndex)

          val beadRadius = if (isCurrentBead) 11.dp.toPx() else 8.5.dp.toPx()

          // Olive wood bead gradient
          val beadGradient = when {
            isCurrentBead -> Brush.radialGradient(
              colors = listOf(Color(0xFFF59E0B), Color(0xFFB45309), Color(0xFF78350F)),
              center = Offset(bx - 3, by - 3),
              radius = beadRadius * 1.5f
            )
            isPassedBead -> Brush.radialGradient(
              colors = listOf(Color(0xFF34D399), Color(0xFF059669), Color(0xFF064E3B)),
              center = Offset(bx - 2, by - 2),
              radius = beadRadius * 1.4f
            )
            else -> Brush.radialGradient(
              colors = if (isDark) {
                listOf(Color(0xFF8D6E63), Color(0xFF5D4037), Color(0xFF3E2723))
              } else {
                listOf(Color(0xFFD7CCC8), Color(0xFFA1887F), Color(0xFF6D4C41))
              },
              center = Offset(bx - 2, by - 2),
              radius = beadRadius * 1.3f
            )
          }

          // Bead shadow
          drawCircle(
            color = Color.Black.copy(alpha = 0.25f),
            radius = beadRadius + 1.dp.toPx(),
            center = Offset(bx, by + 2)
          )

          // Bead body
          drawCircle(
            brush = beadGradient,
            radius = beadRadius,
            center = Offset(bx, by)
          )

          // Bead glossy highlight
          drawCircle(
            color = Color.White.copy(alpha = if (isCurrentBead) 0.65f else 0.35f),
            radius = beadRadius * 0.35f,
            center = Offset(bx - beadRadius * 0.3f, by - beadRadius * 0.3f)
          )

          // Golden bead separator ring every 11 beads
          if (i % 11 == 0) {
            drawCircle(
              color = Color(0xFFFBBF24),
              radius = beadRadius + 2.dp.toPx(),
              center = Offset(bx, by),
              style = Stroke(width = 1.5.dp.toPx())
            )
          }
        }

        // Tasbeeh Tassel (Imam bead) at the bottom
        val tasselAngle = PI / 2.0
        val tx = (center.x + radius * cos(tasselAngle)).toFloat()
        val ty = (center.y + radius * sin(tasselAngle)).toFloat()

        // Imam bead cylinder
        drawRoundRect(
          color = Color(0xFFD97706),
          topLeft = Offset(tx - 6.dp.toPx(), ty),
          size = Size(12.dp.toPx(), 22.dp.toPx()),
          cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
        )
        // Silk tassel strands
        for (t in -2..2) {
          drawLine(
            color = Color(0xFFF59E0B),
            start = Offset(tx + t * 3.dp.toPx(), ty + 22.dp.toPx()),
            end = Offset(tx + t * 5.dp.toPx(), ty + 40.dp.toPx()),
            strokeWidth = 2.dp.toPx()
          )
        }
      }

      // Center Count Display
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
      ) {
        Text(
          text = "$count",
          fontSize = 64.sp,
          fontWeight = FontWeight.Black,
          color = if (isDark) Color(0xFFFBBF24) else Color(0xFFB45309),
          letterSpacing = (-1).sp
        )
        Text(
          text = "OF $target",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
          color = Color(0xFF10B981).copy(alpha = 0.15f),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text(
            text = "TAP BEAD",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF059669),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
          )
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 4. STYLE 2: HAND TASBEEH (Physical Tally Counter Device)
// -------------------------------------------------------------
@Composable
private fun HandTasbeehInteractive(
  count: Int,
  target: Int,
  dhikrName: String,
  onTap: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isDark = isSystemInDarkTheme()
  val scope = rememberCoroutineScope()
  val buttonDepress = remember { Animatable(0f) }
  var isPressed by remember { mutableStateOf(false) }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .height(340.dp),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isDark) Color(0xFF0F172A) else Color(0xFFF8FAFC)
    ),
    border = androidx.compose.foundation.BorderStroke(
      1.5.dp,
      if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1)
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // Digital Hand Tally Counter Graphic Housing
      Box(
        modifier = Modifier
          .size(width = 230.dp, height = 270.dp)
          .shadow(12.dp, RoundedCornerShape(50.dp))
          .background(
            brush = Brush.verticalGradient(
              colors = if (isDark) {
                listOf(Color(0xFF1E293B), Color(0xFF0F172A))
              } else {
                listOf(Color(0xFF047857), Color(0xFF065F46))
              }
            ),
            shape = RoundedCornerShape(50.dp)
          )
          .border(
            width = 3.dp,
            brush = Brush.linearGradient(
              listOf(Color(0xFF34D399), Color(0xFF047857), Color(0xFF064E3B))
            ),
            shape = RoundedCornerShape(50.dp)
          )
          .padding(16.dp),
        contentAlignment = Alignment.TopCenter
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxWidth()
        ) {
          // Metallic brand strip
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "SAKINAH",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFFFBBF24),
              letterSpacing = 2.sp
            )
            Text(
              text = "TALLY 99",
              fontSize = 9.sp,
              fontWeight = FontWeight.Medium,
              color = Color.White.copy(alpha = 0.6f)
            )
          }

          Spacer(modifier = Modifier.height(4.dp))

          // LCD Display Window (authentic seven-segment appearance)
          Box(
            modifier = Modifier
              .fillMaxWidth(0.88f)
              .height(58.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFF86A873)) // Classic vintage green LCD
              .border(2.dp, Color(0xFF4A5D40), RoundedCornerShape(10.dp))
              .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "COUNT",
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF263320).copy(alpha = 0.6f)
              )
              Text(
                text = String.format("%04d", count),
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFF1E2819),
                letterSpacing = 4.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Large Mechanical Click Button
          Box(
            modifier = Modifier
              .size(92.dp)
              .offset { IntOffset(0, (buttonDepress.value * 6).toInt()) }
              .shadow(
                elevation = if (isPressed) 2.dp else 10.dp,
                shape = CircleShape
              )
              .clip(CircleShape)
              .background(
                brush = Brush.radialGradient(
                  colors = if (isPressed) {
                    listOf(Color(0xFF10B981), Color(0xFF047857))
                  } else {
                    listOf(Color(0xFF34D399), Color(0xFF059669), Color(0xFF047857))
                  }
                )
              )
              .border(3.dp, Color(0xFFFBBF24), CircleShape)
              .pointerInput(Unit) {
                detectTapGestures(
                  onPress = {
                    isPressed = true
                    scope.launch { buttonDepress.animateTo(1f, tween(50)) }
                    tryAwaitRelease()
                    isPressed = false
                    scope.launch { buttonDepress.animateTo(0f, spring(dampingRatio = 0.4f)) }
                    onTap()
                  }
                )
              },
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Press to Count",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
              Text(
                text = "COUNT",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 1.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Finger strap contour
          Box(
            modifier = Modifier
              .width(70.dp)
              .height(12.dp)
              .clip(RoundedCornerShape(6.dp))
              .background(Color.Black.copy(alpha = 0.3f))
          )
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 5. STYLE 3: HORIZONTAL BALLOONS LINE
// -------------------------------------------------------------
@Composable
private fun BalloonsLineTasbeehInteractive(
  count: Int,
  target: Int,
  onTap: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isDark = isSystemInDarkTheme()
  val scope = rememberCoroutineScope()
  var balloonScale by remember { mutableFloatStateOf(1f) }

  val balloonColors = listOf(
    Color(0xFF3B82F6), // Sky Blue
    Color(0xFF10B981), // Emerald
    Color(0xFFEC4899), // Rose
    Color(0xFFF59E0B), // Amber
    Color(0xFF8B5CF6), // Purple
    Color(0xFF06B6D4)  // Cyan
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .height(340.dp),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isDark) Color(0xFF0B132B) else Color(0xFFF0F9FF)
    ),
    border = androidx.compose.foundation.BorderStroke(
      1.5.dp,
      if (isDark) Color(0xFF1C2541) else Color(0xFFBAE6FD)
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Header count indicator
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "FLOATING BALLOONS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0284C7),
            letterSpacing = 1.5.sp
          )
          Text(
            text = "$count / $target completed",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
          )
        }
        Surface(
          color = Color(0xFF0284C7).copy(alpha = 0.15f),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "TAP CENTER BALLOON",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0369A1),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      // Horizontal Row of 5 floating balloons line
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Display -2, -1, Current(0), +1, +2 relative positions
        for (offset in -2..2) {
          val balloonNum = count + offset
          val isCenter = (offset == 0)
          val colorIndex = ((balloonNum % balloonColors.size) + balloonColors.size) % balloonColors.size
          val baseColor = balloonColors[colorIndex]

          val balloonSize = if (isCenter) 92.dp else 48.dp
          val verticalBobbing = if (isCenter) 0.dp else if (offset % 2 == 0) (-12).dp else 12.dp

          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .offset(y = verticalBobbing)
              .then(
                if (isCenter) {
                  Modifier.pointerInput(Unit) {
                    detectTapGestures(
                      onPress = {
                        balloonScale = 0.88f
                        tryAwaitRelease()
                        balloonScale = 1.08f
                        scope.launch {
                          delay(90)
                          balloonScale = 1f
                        }
                        onTap()
                      }
                    )
                  }
                } else Modifier
              )
          ) {
            // Balloon Oval Body
            Box(
              modifier = Modifier
                .size(balloonSize)
                .scale(if (isCenter) balloonScale else 1f)
                .shadow(
                  elevation = if (isCenter) 12.dp else 4.dp,
                  shape = CircleShape
                )
                .clip(CircleShape)
                .background(
                  brush = Brush.radialGradient(
                    colors = listOf(
                      baseColor.copy(alpha = 0.95f),
                      baseColor,
                      baseColor.copy(alpha = 0.75f)
                    ),
                    center = Offset(30f, 30f)
                  )
                )
                .border(
                  width = if (isCenter) 3.dp else 1.dp,
                  color = if (isCenter) Color(0xFFFBBF24) else Color.White.copy(alpha = 0.5f),
                  shape = CircleShape
                ),
              contentAlignment = Alignment.Center
            ) {
              // Glossy highlight reflection
              Box(
                modifier = Modifier
                  .size(if (isCenter) 26.dp else 14.dp)
                  .align(Alignment.TopStart)
                  .offset(x = 10.dp, y = 10.dp)
                  .clip(CircleShape)
                  .background(Color.White.copy(alpha = 0.45f))
              )

              // Number inside balloon
              if (balloonNum >= 0) {
                Text(
                  text = "$balloonNum",
                  fontSize = if (isCenter) 26.sp else 14.sp,
                  fontWeight = FontWeight.Black,
                  color = Color.White
                )
              }
            }

            // Balloon Knot & String
            Canvas(
              modifier = Modifier
                .width(16.dp)
                .height(28.dp)
            ) {
              // Knot
              drawCircle(
                color = baseColor,
                radius = 3.dp.toPx(),
                center = Offset(size.width / 2f, 3.dp.toPx())
              )
              // Dangling string
              val path = Path().apply {
                moveTo(size.width / 2f, 4.dp.toPx())
                quadraticTo(
                  size.width * 0.8f, size.height * 0.5f,
                  size.width / 2f, size.height
                )
              }
              drawPath(
                path = path,
                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                style = Stroke(width = 1.5.dp.toPx())
              )
            }
          }
        }
      }

      // Tap instructions footer
      Surface(
        color = if (isDark) Color(0xFF1E293B) else Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Total Sessions Done: ${count / target}",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
          )
          Text(
            text = "Tap Center to Pop Next",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0284C7)
          )
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 6. DHIKR CATALOG & TARGET PICKER SHEETS
// -------------------------------------------------------------
@Composable
private fun DhikrCatalogSheet(
  selectedDhikr: DhikrPreset,
  onSelect: (DhikrPreset) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 12.dp)
  ) {
    Text(
      text = "Select Dhikr Preset",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(12.dp))

    DhikrPreset.PRESETS.forEach { preset ->
      val isSelected = preset.id == selectedDhikr.id
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
          .clickable { onSelect(preset) },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = preset.transliteration,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = preset.translation,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Text(
            text = preset.arabicText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )
        }
      }
    }
    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
private fun TargetPickerSheet(
  currentTarget: Int,
  onSelectTarget: (Int) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 12.dp)
  ) {
    Text(
      text = "Set Dhikr Target",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(12.dp))

    val commonTargets = listOf(33, 99, 100, 500, 1000)

    commonTargets.forEach { targetVal ->
      val isSelected = targetVal == currentTarget
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
          .clickable { onSelectTarget(targetVal) },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "$targetVal Counts",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
          )
          if (isSelected) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary
            )
          }
        }
      }
    }
    Spacer(modifier = Modifier.height(24.dp))
  }
}
