package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DuaCategory
import com.example.model.DuaItem

// ==========================================
// 1. DUA CATEGORIES SCREEN
// ==========================================
@Composable
fun DuaCategoriesScreen(
  onSelectCategory: (DuaCategory) -> Unit,
  onBack: () -> Unit
) {
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
          modifier = Modifier.testTag("dua_categories_back")
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
            text = "Daily Duas",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "الأدعية والأذكار • Authentic Supplications",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(horizontal = 20.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(4.dp))
      }

      items(DuaCategory.CATEGORIES, key = { it.id }) { category ->
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = MaterialTheme.colorScheme.surface,
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelectCategory(category) }
            .testTag("dua_cat_${category.id}")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Text(
                  text = category.title,
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = MaterialTheme.colorScheme.primaryContainer
                ) {
                  Text(
                    text = "${category.count} Duas",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(2.dp))

              Text(
                text = category.arabicTitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
              )

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = category.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }

            Icon(
              imageVector = Icons.Outlined.ChevronRight,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }
}

// ==========================================
// 2. DUA LIST SCREEN
// ==========================================
@Composable
fun DuaListScreen(
  category: DuaCategory,
  onSelectDua: (DuaItem) -> Unit,
  onBack: () -> Unit
) {
  val duasInCategory = DuaItem.DUA_ITEMS.filter {
    it.categoryId == category.id || category.id == "morning_evening"
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
          modifier = Modifier.testTag("dua_list_back")
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
            text = category.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = category.arabicTitle,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(horizontal = 20.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(4.dp))
      }

      items(duasInCategory, key = { it.id }) { dua ->
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = MaterialTheme.colorScheme.surface,
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelectDua(dua) }
            .testTag("dua_item_${dua.id}")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = dua.title,
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = dua.arabic,
              style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
              ),
              color = MaterialTheme.colorScheme.primary,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = dua.translation,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = dua.reference,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.secondary
              )

              Text(
                text = "Read & Listen →",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
              )
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

// ==========================================
// 3. DUA DETAIL SCREEN (FOCUSED READING & AUDIO)
// ==========================================
@Composable
fun DuaDetailScreen(
  dua: DuaItem,
  isPlaying: Boolean,
  onPlayAudio: () -> Unit,
  onBack: () -> Unit
) {
  val scrollState = rememberScrollState()

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
          modifier = Modifier.testTag("dua_detail_back")
        ) {
          Text(
            text = "←",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
          )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Supplication",
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground
        )
      }
    },
    bottomBar = {
      Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 24.dp, vertical = 14.dp)
      ) {
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
            .testTag("dua_detail_play_btn")
        ) {
          Icon(
            imageVector = if (isPlaying) Icons.Outlined.GraphicEq else Icons.Outlined.PlayArrow,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isPlaying) "Playing Recitation..." else "Listen to Dua Recitation",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
          )
        }
      }
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .verticalScroll(scrollState)
        .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
      Text(
        text = dua.title,
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onBackground
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = dua.reference,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.secondary
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Arabic Card
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = dua.arabic,
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            lineHeight = 40.sp,
            textAlign = TextAlign.Right
          ),
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Transliteration
      Text(
        text = "TRANSLITERATION",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.8.sp
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = dua.transliteration,
        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Translation
      Text(
        text = "TRANSLATION",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.8.sp
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "“${dua.translation}”",
        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Spiritual Benefit
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Benefit & Merit",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = dua.benefit,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}
