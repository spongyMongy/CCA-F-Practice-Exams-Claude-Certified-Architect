package com.arslan.ccafprep.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arslan.ccafprep.R
import com.arslan.ccafprep.domain.model.DomainProgress
import com.arslan.ccafprep.presentation.components.GlassCard
import com.arslan.ccafprep.presentation.components.MilestoneBadge
import com.arslan.ccafprep.presentation.components.PremiumBadge
import com.arslan.ccafprep.presentation.components.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onStartStudy: () -> Unit,
    onViewProgress: () -> Unit,
    onOpenPaywall: () -> Unit
) {
    val uiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("ARCHITECT ELITE", fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium, letterSpacing = 1.5.sp)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                ReadinessSummaryCard(uiState.overallReadiness, onViewProgress)
            }

            item {
                StreakAndGoalRow(uiState.streak, uiState.weeklyGoalProgress)
            }

            item {
                SectionHeader("Architect Milestones")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    uiState.milestones.forEach { milestone ->
                        MilestoneBadge(
                            title = milestone.title,
                            icon = milestone.icon,
                            isUnlocked = milestone.isUnlocked
                        )
                    }
                }
            }

            item {
                SectionHeader("Success Tip of the Day")
                DailyTipCard(uiState.dailyTip, uiState.isPro, onOpenPaywall)
            }

            item {
                SectionHeader("Domain Mastery")
                MasteryGrid(uiState.domainProgress, uiState.isPro, onOpenPaywall)
            }

            if (uiState.isPro) {
                item {
                    SectionHeader("Architect Forecast")
                    ForecastCard(uiState.forecast)
                }
            }

            item {
                SectionHeader("Quick Actions")
                QuickActionRow(onStartStudy, onOpenPaywall, uiState.isPro)
            }

            if (uiState.weakAreas.isNotEmpty()) {
                item {
                    SectionHeader("Focus Areas")
                    WeakAreasCard(uiState.weakAreas) { onViewProgress() }
                }
            }
            
            item {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.disclaimer_text),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                )
            }
        }
    }
}

@Composable
fun ForecastCard(forecast: com.arslan.ccafprep.domain.usecase.ScorePrediction) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(60.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { forecast.score / 100f },
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 6.dp,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Text("${forecast.score}%", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.width(20.dp))
            Column {
                Text("Pass Probability", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(forecast.status, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                LinearProgressIndicator(
                    progress = { forecast.confidence },
                    modifier = Modifier.width(100.dp).height(4.dp).padding(top = 4.dp).clip(CircleShape),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
                Text("Model Confidence: ${(forecast.confidence * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@Composable
fun ReadinessSummaryCard(score: Float, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Study Readiness", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                Text("${(score * 100).toInt()}%", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Black)
                LinearProgressIndicator(
                    progress = { score },
                    modifier = Modifier.fillMaxWidth().clip(CircleShape).height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
                )
            }
        }
    }
}

@Composable
fun StreakAndGoalRow(streak: Int, goalProgress: Float) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        GlassCard(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFF9800))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("$streak Day", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Streak", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        GlassCard(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).background(MaterialTheme.colorScheme.secondaryContainer, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("${(goalProgress * 100).toInt()}%", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Weekly Goal", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun DailyTipCard(tip: String, isPro: Boolean, onUpgrade: () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { if (!isPro) onUpgrade() }
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.width(8.dp))
                Text("Architectural Insight", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                if (!isPro) PremiumBadge()
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (isPro) tip else "Upgrade to PRO to unlock daily architectural and exam strategy insights.",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = if (isPro) androidx.compose.ui.text.font.FontStyle.Normal else androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}

@Composable
fun MasteryGrid(progress: List<DomainProgress>, isPro: Boolean, onUpgrade: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        progress.take(if (isPro) 5 else 2).forEach { domain ->
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(domain.domain.title, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                    Spacer(Modifier.width(12.dp))
                    Text("${(domain.readinessScore * 100).toInt()}%", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    LinearProgressIndicator(
                        progress = { domain.readinessScore },
                        modifier = Modifier.width(60.dp).height(4.dp).clip(CircleShape)
                    )
                }
            }
        }
        if (!isPro) {
            TextButton(onClick = onUpgrade, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("See all domain stats (+3 more)")
            }
        }
    }
}

@Composable
fun QuickActionRow(onStartStudy: () -> Unit, onOpenPaywall: () -> Unit, isPro: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onStartStudy,
            modifier = Modifier.weight(1f).height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("CONTINUE STUDY")
        }

        if (!isPro) {
            Button(
                onClick = onOpenPaywall,
                modifier = Modifier.weight(1f).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.Star, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("GO PRO")
            }
        }
    }
}
