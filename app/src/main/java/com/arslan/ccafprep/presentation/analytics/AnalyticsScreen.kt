package com.arslan.ccafprep.presentation.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arslan.ccafprep.presentation.components.SectionHeader
import com.arslan.ccafprep.presentation.progress.ProgressViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: ProgressViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Architect Insights") },
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
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item {
                ScoreWheel(uiState.overallReadiness)
            }

            item {
                MasteryRadarChart(
                    domainScores = uiState.domainProgress.map { it.domain.title.take(10) to it.accuracy }
                )
            }

            item {
                StudyActivityHeatmap(
                    activityData = uiState.activityData
                )
            }

            item {
                SectionHeader("Architect Strategy Analysis")
                val lowestDomain = uiState.domainProgress.minByOrNull { it.accuracy }
                val analysisText = if (uiState.overallReadiness == 0f) {
                    "Complete a few practice sessions to generate your architectural readiness forecast."
                } else if (uiState.forecast.score >= 80) {
                    "Model Confidence: ${(uiState.forecast.confidence * 100).toInt()}%. You are currently on track to pass. Continue refined study on ${lowestDomain?.domain?.title ?: "all areas"} to minimize variance."
                } else {
                    "Current Pass Probability: ${uiState.forecast.score}%. To improve, prioritize ${lowestDomain?.domain?.title ?: "lower scoring domains"} where your model confidence is currently low."
                }
                
                Text(
                    text = analysisText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
