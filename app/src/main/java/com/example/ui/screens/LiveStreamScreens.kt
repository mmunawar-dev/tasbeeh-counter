package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LiveStreamScreen(
  title: String,
  arabicTitle: String,
  locationName: String,
  channelName: String,
  youtubeUrl: String,
  streamDescription: String,
  isMakkah: Boolean,
  onBack: () -> Unit
) {
  val context = LocalContext.current
  var isPlaying by remember { mutableStateOf(true) }
  var isMuted by remember { mutableStateOf(false) }

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
          modifier = Modifier.testTag("live_stream_back")
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
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = arabicTitle,
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
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Stream Video Player Window
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF0F1512),
        modifier = Modifier
          .fillMaxWidth()
          .height(230.dp)
          .testTag("live_stream_player_container")
      ) {
        Box(modifier = Modifier.fillMaxSize()) {
          // Decorative Subtle Holy Site Silhouette / Atmosphere
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                if (isMakkah) Color(0xFF141F1A) else Color(0xFF121E18)
              ),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF22352C),
                modifier = Modifier.size(54.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(
                    text = if (isMakkah) "🕋" else "🕌",
                    fontSize = 28.sp
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = if (isPlaying) "Streaming Live from $locationName" else "Stream Paused",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = Color(0xFFE5EDE8)
              )

              Text(
                text = "Official 24/7 HD Broadcast",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = Color(0xFF9CAD9F)
              )
            }
          }

          // Top Badges Overlay (Live 24/7 + Viewers)
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFC0392B)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                )
                Text(
                  text = "LIVE 24/7",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                  ),
                  color = Color.White
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0x66000000)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Outlined.Visibility,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(12.dp)
                )
                Text(
                  text = "48.2K watching",
                  style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                  color = Color.White
                )
              }
            }
          }

          // Bottom Control Overlay
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .align(Alignment.BottomCenter)
              .background(Color(0x80000000))
              .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              IconButton(
                onClick = { isPlaying = !isPlaying },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  imageVector = if (isPlaying) Icons.Outlined.Pause else Icons.Outlined.PlayArrow,
                  contentDescription = if (isPlaying) "Pause" else "Play",
                  tint = Color.White,
                  modifier = Modifier.size(20.dp)
                )
              }

              IconButton(
                onClick = { isMuted = !isMuted },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  imageVector = if (isMuted) Icons.Outlined.VolumeOff else Icons.Outlined.VolumeUp,
                  contentDescription = "Mute/Unmute",
                  tint = Color.White,
                  modifier = Modifier.size(20.dp)
                )
              }
            }

            IconButton(
              onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                context.startActivity(intent)
              },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.OpenInNew,
                contentDescription = "Open in YouTube",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }

      // Stream Metadata Card
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = channelName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Official Ministry of Media • Kingdom of Saudi Arabia",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.primary
          )

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = streamDescription,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // Open in YouTube Button
      Button(
        onClick = {
          val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
          context.startActivity(intent)
        },
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary,
          contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
      ) {
        Icon(
          imageVector = Icons.Outlined.LiveTv,
          contentDescription = null,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Open Full Screen Stream",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
        )
      }
    }
  }
}
