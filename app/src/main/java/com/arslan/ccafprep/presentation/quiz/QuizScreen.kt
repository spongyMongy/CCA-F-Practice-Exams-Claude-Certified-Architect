package com.arslan.ccafprep.presentation.quiz

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arslan.ccafprep.presentation.components.GlassCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Practice Session") },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) { Text("Exit") }
                },
                actions = {
                    if (uiState.showTimer) {
                        Text(
                            text = formatTime(uiState.elapsedSeconds),
                            modifier = Modifier.padding(end = 16.dp),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        if (uiState.isFinished) {
            QuizResult(
                score = uiState.correctCount,
                total = uiState.questions.size,
                onFinish = onNavigateBack
            )
        } else if (uiState.questions.isNotEmpty()) {
            val currentQuestion = uiState.questions[uiState.currentQuestionIndex]

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                item {
                    LinearProgressIndicator(
                        progress = { (uiState.currentQuestionIndex + 1).toFloat() / uiState.questions.size },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Question ${uiState.currentQuestionIndex + 1} of ${uiState.questions.size}",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(text = currentQuestion.text, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(24.dp))
                }

                itemsIndexed(currentQuestion.options) { index, option ->
                    val isSelected = uiState.selectedAnswer == index
                    val isCorrect = index == currentQuestion.correctIndex
                    val showRationale = uiState.immediateFeedback && uiState.selectedAnswer != null
                    
                    val color = if (showRationale) {
                        when {
                            isCorrect -> Color(0xFF4CAF50).copy(alpha = 0.9f)
                            isSelected -> Color(0xFFE57373).copy(alpha = 0.9f)
                            else -> MaterialTheme.colorScheme.surface
                        }
                    } else if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }

                    Card(
                        onClick = { 
                            if (uiState.selectedAnswer == null) {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.submitAnswer(index) 
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = color),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(text = option, style = MaterialTheme.typography.bodyLarge)
                            
                            // ELITE FEATURE: Distractor Analysis
                            if (showRationale && isSelected && !isCorrect) {
                                currentQuestion.distractorRationale?.getOrNull(index)?.let { rationale ->
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        text = "Architect Insight: $rationale",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.error,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            
                            if (showRationale && isCorrect) {
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "✓ Core Architectural Principle verified.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                item {
                    if (uiState.selectedAnswer != null) {
                        Spacer(Modifier.height(32.dp))
                        Button(
                            onClick = { viewModel.nextQuestion() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (uiState.currentQuestionIndex == uiState.questions.size - 1) "Finish" else "Next Question")
                        }
                    }
                }
            }
        }
    }
}

fun formatTime(seconds: Long): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}

@Composable
fun QuizResult(score: Int, total: Int, onFinish: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlassCard(
            modifier = Modifier
                .padding(32.dp)
                .scale(scale)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (score.toFloat() / total >= 0.8f) "Master Architect!" else "Session Complete",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "$score / $total",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                
                val percentage = (score.toFloat() / total * 100).toInt()
                Text(
                    text = "$percentage% Accuracy",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = onFinish,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("BACK TO DASHBOARD")
                }
            }
        }
    }
}
