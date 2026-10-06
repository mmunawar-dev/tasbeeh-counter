package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
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

@Composable
fun AllahNamesScreen(
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

      Spacer(modifier = Modifier.height(14.dp))

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
          Spacer(modifier = Modifier.height(80.dp))
        }
      }
    }
  }
}
