package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PremiumPlan
import com.example.model.SakinahUiState

@Composable
fun PremiumScreen(
  state: SakinahUiState,
  onUnlock: () -> Unit,
  onBack: () -> Unit
) {
  var selectedPlan by remember { mutableStateOf(PremiumPlan.YEARLY) }

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
          modifier = Modifier.testTag("premium_back_button")
        ) {
          Text(
            text = "←",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
          )
        }
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
          onClick = {
            onUnlock()
            onBack()
          },
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
          ),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("premium_unlock_button")
        ) {
          Text(
            text = if (state.isPremiumUnlocked) {
              "Sakinah Plus Active"
            } else {
              when (selectedPlan) {
                PremiumPlan.MONTHLY -> "Subscribe Monthly • $1.99/mo"
                PremiumPlan.YEARLY -> "Start Free Trial • Then $9.99/yr"
                PremiumPlan.LIFETIME -> "Unlock Lifetime Access • $24.99"
              }
            },
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
          )
        }
      }
    }
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(horizontal = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        Box(
          modifier = Modifier
            .size(68.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.size(34.dp)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Make your prayer experience serene",
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
          ),
          color = MaterialTheme.colorScheme.onBackground,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "Support pure Islamic utility software crafted without advertisements, tracking scripts, or distractions.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )
      }

      // Three Purchase Plans: Monthly, Yearly, Lifetime
      item {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          // 1. Monthly Plan
          PlanSelectionCard(
            plan = PremiumPlan.MONTHLY,
            isSelected = selectedPlan == PremiumPlan.MONTHLY,
            onClick = { selectedPlan = PremiumPlan.MONTHLY }
          )

          // 2. Yearly Plan (Most Popular)
          PlanSelectionCard(
            plan = PremiumPlan.YEARLY,
            isSelected = selectedPlan == PremiumPlan.YEARLY,
            onClick = { selectedPlan = PremiumPlan.YEARLY }
          )

          // 3. Lifetime Plan
          PlanSelectionCard(
            plan = PremiumPlan.LIFETIME,
            isSelected = selectedPlan == PremiumPlan.LIFETIME,
            onClick = { selectedPlan = PremiumPlan.LIFETIME }
          )
        }
      }

      // Benefits List
      val benefits = listOf(
        "100% ad-free, tranquil worship environment",
        "Real-time Aladhan Platform synchronization",
        "Home screen & Lock screen prayer widgets",
        "Multiple authentic adhan audio reciters",
        "5 distinct digital Tasbeeh counter designs",
        "Audio recitations for all 99 Names and daily Duas"
      )

      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
          Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Text(
              text = "INCLUDED IN SAKINAH PLUS",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
              ),
              color = MaterialTheme.colorScheme.primary
            )

            benefits.forEach { benefit ->
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(11.dp)
                  )
                }

                Text(
                  text = benefit,
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

@Composable
private fun PlanSelectionCard(
  plan: PremiumPlan,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
    border = androidx.compose.foundation.BorderStroke(
      width = if (isSelected) 2.dp else 1.dp,
      color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    ),
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .testTag("plan_${plan.name.lowercase()}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = plan.title,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
          )

          plan.badge?.let { badgeText ->
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = if (plan.isPopular) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
            ) {
              Text(
                text = badgeText,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 10.sp
                ),
                color = if (plan.isPopular) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = plan.description,
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = plan.priceFormatted,
          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.primary
        )
        Text(
          text = plan.billingPeriod,
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
