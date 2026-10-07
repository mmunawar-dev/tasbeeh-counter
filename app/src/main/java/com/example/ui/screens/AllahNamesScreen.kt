package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AllahName
import com.example.service.AudioPlayerService

@Composable
fun AllahNamesScreen(
  playbackState: AudioPlayerService.PlaybackState,
  onStartFullRecitation: () -> Unit,
  onToggleFullRecitation: () -> Unit,
  onStopFullRecitation: () -> Unit,
  onSelectName: (AllahName) -> Unit,
  onPlayName: (AllahName) -> Unit,
  onBack: () -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }

  val filteredNames = remember(searchQuery) {
    if (searchQuery.isBlank()) {
      AllahName.NAMES_LIST
    } else {
      AllahName.NAMES_LIST.filter {
        it.transliteration.contains(searchQuery, ignoreCase = true) ||
          it.englishMeaning.contains(searchQuery, ignoreCase = true) ||
          it.arabic.contains(searchQuery)
      }
    }
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
          modifier = Modifier.testTag("allah_names_back")
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
            text = "Allah's 99 Names",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "أسماء الله الحسنى • Asma ul-Husna",
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
        .padding(horizontal = 20.dp)
    ) {
      // 1. TOP HERO CARD: PLAY ALLAH'S NAMES FOREGROUND SERVICE MP3
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
        ),
        border = androidx.compose.foundation.BorderStroke(
          1.5.dp,
          MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("play_allah_names_hero_card")
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Icon(
                  imageVector = Icons.Outlined.GraphicEq,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = "FOREGROUND AUDIO",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    fontSize = 10.sp
                  ),
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }

            Text(
              text = "أسماء الله الحسنى",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Play 99 Names of Allah Recitation",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
          )

          Text(
            text = "Play authentic Asma-ul-Husna audio (.mp3) with background foreground service",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(12.dp))

          if (playbackState.isServiceRunning) {
            val progress = if (playbackState.durationMs > 0) {
              (playbackState.currentPositionMs.toFloat() / playbackState.durationMs.toFloat()).coerceIn(0f, 1f)
            } else 0f

            LinearProgressIndicator(
              progress = { progress },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = MaterialTheme.colorScheme.primary,
              trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = formatTime(playbackState.currentPositionMs),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = if (playbackState.durationMs > 0) formatTime(playbackState.durationMs) else "--:--",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Button(
                onClick = onToggleFullRecitation,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = MaterialTheme.colorScheme.primary,
                  contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                  .weight(1f)
                  .height(46.dp)
                  .testTag("hero_audio_toggle_btn")
              ) {
                Icon(
                  imageVector = if (playbackState.isPlaying) Icons.Outlined.Pause else Icons.Outlined.PlayArrow,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = if (playbackState.isPlaying) "Pause" else "Resume",
                  style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                )
              }

              OutlinedButton(
                onClick = onStopFullRecitation,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .height(46.dp)
                  .testTag("hero_audio_stop_btn")
              ) {
                Icon(
                  imageVector = Icons.Outlined.Stop,
                  contentDescription = "Stop",
                  tint = MaterialTheme.colorScheme.error,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Stop", color = MaterialTheme.colorScheme.error)
              }
            }
          } else {
            Button(
              onClick = onStartFullRecitation,
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
              ),
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("hero_audio_start_btn")
            ) {
              Icon(
                imageVector = Icons.Outlined.PlayCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Play 99 Names of Allah (Audio MP3)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Search Bar
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search by name or meaning...") },
        leadingIcon = {
          Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Outlined.Close, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = MaterialTheme.colorScheme.surface,
          unfocusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("allah_names_search")
      )

      Spacer(modifier = Modifier.height(12.dp))

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(filteredNames, key = { it.number }) { name ->
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .clickable { onSelectName(name) }
              .testTag("allah_name_item_${name.number}")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
              ) {
                // Number Badge
                Box(
                  modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "${name.number}",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                  )
                }

                Column {
                  Text(
                    text = name.transliteration,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = name.englishMeaning,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              // Arabic Script on the right
              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = name.arabic,
                  style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                  ),
                  color = MaterialTheme.colorScheme.primary
                )

                IconButton(
                  onClick = { onPlayName(name) },
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(
                    imageVector = Icons.Outlined.PlayArrow,
                    contentDescription = "Listen to pronunciation",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(84.dp))
        }
      }
    }
  }
}

private fun formatTime(ms: Int): String {
  val totalSec = ms / 1000
  val minutes = totalSec / 60
  val seconds = totalSec % 60
  return "%02d:%02d".format(minutes, seconds)
}
