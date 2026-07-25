package com.arslan.ccafprep.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                OutlinedCard(
                    onClick = { viewModel.setTheme(theme) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(theme.displayName, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                        if (isSelected) RadioButton(selected = true, onClick = null)
                    }
                }
            }

            item {
                SectionHeader("Background Effects")
            }

            items(BackgroundStyle.entries) { style ->
                val isLocked = !uiState.isPro && (style == BackgroundStyle.MESH || style == BackgroundStyle.GRID)
                val isSelected = uiState.background == style
                
                Surface(
                    onClick = { if (!isLocked) viewModel.setBackground(style) else onOpenPaywall() },
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Transparent 
                ) {
                    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            style.displayName, 
                            color = if (isLocked) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface,
                            style = if (isSelected) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
                        )
                        if (isLocked) PremiumBadge() else if (isSelected) Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
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
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Show Timer", style = MaterialTheme.typography.labelLarge)
                                Text("Track session duration", style = MaterialTheme.typography.bodySmall)
                            }
                            Switch(checked = uiState.showTimer, onCheckedChange = { viewModel.setShowTimer(it) })
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Immediate Feedback", style = MaterialTheme.typography.labelLarge)
                                Text("Show answers per question", style = MaterialTheme.typography.bodySmall)
                            }
                            Switch(checked = uiState.immediateFeedback, onCheckedChange = { viewModel.setImmediateFeedback(it) })
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
