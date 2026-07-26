package com.arslan.ccafprep.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arslan.ccafprep.R
import com.arslan.ccafprep.domain.model.theme.AppTheme
import com.arslan.ccafprep.domain.model.theme.BackgroundStyle
import com.arslan.ccafprep.presentation.components.PremiumBadge
import com.arslan.ccafprep.presentation.components.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onOpenPaywall: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Settings") },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) { Text("Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent 
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SectionHeader("Visual Theme")
            }

            items(AppTheme.entries) { theme ->
                val isSelected = uiState.theme == theme
                val isLocked = !uiState.isPro && theme != AppTheme.DEFAULT
                
                OutlinedCard(
                    onClick = { if (!isLocked) viewModel.setTheme(theme) else onOpenPaywall() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            theme.displayName, 
                            style = MaterialTheme.typography.titleMedium, 
                            modifier = Modifier.weight(1f),
                            color = if (isLocked) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                        )
                        if (isLocked) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            PremiumBadge()
                        } else if (isSelected) {
                            RadioButton(selected = true, onClick = null)
                        }
                    }
                }
            }

            item {
                SectionHeader("Background Effects")
            }

            items(BackgroundStyle.entries) { style ->
                val isLocked = !uiState.isPro && (style == BackgroundStyle.MESH || style == BackgroundStyle.GRID || style == BackgroundStyle.GRADIENT)
                val isSelected = uiState.background == style
                
                Surface(
                    onClick = { if (!isLocked) viewModel.setBackground(style) else onOpenPaywall() },
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Transparent 
                ) {
                    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            style.displayName, 
                            color = if (isLocked) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface,
                            style = if (isSelected) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
                        )
                        if (isLocked) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                PremiumBadge()
                            }
                        } else if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            item {
                SectionHeader("Quiz Experience")
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Surface(
                            onClick = { if (uiState.isPro) viewModel.setShowTimer(!uiState.showTimer) else onOpenPaywall() },
                            color = Color.Transparent
                        ) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        "Show Timer", 
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (!uiState.isPro) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text("Track session duration", style = MaterialTheme.typography.bodySmall)
                                }
                                if (!uiState.isPro) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    PremiumBadge()
                                }
                                Switch(
                                    checked = uiState.showTimer, 
                                    onCheckedChange = { if (uiState.isPro) viewModel.setShowTimer(it) else onOpenPaywall() },
                                    enabled = uiState.isPro
                                )
                            }
                        }
                        
                        Spacer(Modifier.height(12.dp))
                        
                        Surface(
                            onClick = { if (uiState.isPro) viewModel.setImmediateFeedback(!uiState.immediateFeedback) else onOpenPaywall() },
                            color = Color.Transparent
                        ) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        "Immediate Feedback", 
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (!uiState.isPro) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text("Show answers per question", style = MaterialTheme.typography.bodySmall)
                                }
                                if (!uiState.isPro) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    PremiumBadge()
                                }
                                Switch(
                                    checked = uiState.immediateFeedback, 
                                    onCheckedChange = { if (uiState.isPro) viewModel.setImmediateFeedback(it) else onOpenPaywall() },
                                    enabled = uiState.isPro
                                )
                            }
                        }
                    }
                }
            }

            item {
                SectionHeader("About & Legal")
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Surface(
                            onClick = { uriHandler.openUri("https://github.com/spongyMongy/CCA-F-Practice-Exams-Claude-Certified-Architect/blob/main/PRIVACY_POLICY.md") },
                            color = Color.Transparent,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(12.dp))
                                Text("Privacy Policy", style = MaterialTheme.typography.labelLarge)
                                Spacer(Modifier.weight(1f))
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            item {
                SectionHeader("Developer Options")
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth(),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { viewModel.togglePro() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(if (uiState.isPro) "LOCK PRO FEATURES" else "UNLOCK PRO FEATURES")
                        }

                        OutlinedButton(
                            onClick = { viewModel.forceSync() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("FORCE DB RE-SEED")
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(32.dp))
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
