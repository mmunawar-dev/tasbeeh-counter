package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AudioTrack

@Composable
fun AudioMiniPlayer(
  activeTrack: AudioTrack?,
  onTogglePlayPause: () -> Unit,
  onClose: () -> Unit,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = activeTrack != null,
    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
    modifier = modifier
  ) {
    if (activeTrack != null) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
          .testTag("audio_mini_player")
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          // Progress line on top
          LinearProgressIndicator(
            progress = { activeTrack.progressFraction },
            modifier = Modifier
              .fillMaxWidth()
              .height(3.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Outlined.GraphicEq,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(18.dp)
                )
              }

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = activeTrack.title,
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                  color = MaterialTheme.colorScheme.onSurface,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Text(
                  text = activeTrack.arabicTitle,
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              IconButton(
                onClick = onTogglePlayPause,
                modifier = Modifier
                  .size(36.dp)
                  .testTag("audio_mini_play_pause")
              ) {
                Icon(
                  imageVector = if (activeTrack.isPlaying) Icons.Outlined.Pause else Icons.Outlined.PlayArrow,
                  contentDescription = if (activeTrack.isPlaying) "Pause" else "Play",
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(22.dp)
                )
              }

              IconButton(
                onClick = onClose,
                modifier = Modifier
                  .size(36.dp)
                  .testTag("audio_mini_close")
              ) {
                Icon(
                  imageVector = Icons.Outlined.Close,
                  contentDescription = "Stop & Close",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}
