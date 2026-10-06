package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AllahName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllahNameDetailBottomSheet(
  name: AllahName,
  isPlaying: Boolean,
  onPlayAudio: () -> Unit,
  onDismiss: () -> Unit
) {
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    containerColor = MaterialTheme.colorScheme.surface,
    dragHandle = {
      Box(
        modifier = Modifier
          .padding(vertical = 12.dp)
          .width(40.dp)
          .height(4.dp)
          .background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(2.dp))
      )
    }
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 8.dp)
        .padding(bottom = 32.dp)
        .testTag("allah_name_detail_sheet"),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Number Pill
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer
      ) {
        Text(
          text = "NAME #${name.number}",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          ),
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Large Arabic Typography
      Text(
        text = name.arabic,
        style = MaterialTheme.typography.displayLarge.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 44.sp,
          textAlign = TextAlign.Center
        ),
        color = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = name.transliteration,
        style = MaterialTheme.typography.headlineSmall.copy(
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        ),
        color = MaterialTheme.colorScheme.onSurface
      )

      Text(
        text = "“${name.englishMeaning}”",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Medium,
          textAlign = TextAlign.Center
        ),
        color = MaterialTheme.colorScheme.secondary
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Explanation Box
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "SPIRITUAL SIGNIFICANCE",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.8.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = name.explanation,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Play Pronunciation Button
      Button(
        onClick = onPlayAudio,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary,
          contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("allah_name_play_audio_btn")
      ) {
        Icon(
          imageVector = if (isPlaying) Icons.Outlined.GraphicEq else Icons.Outlined.PlayArrow,
          contentDescription = null,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (isPlaying) "Playing Pronunciation..." else "Listen to Pronunciation",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
        )
      }
    }
  }
}
