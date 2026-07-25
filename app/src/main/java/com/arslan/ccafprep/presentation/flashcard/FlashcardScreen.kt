package com.arslan.ccafprep.presentation.flashcard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(
    viewModel: FlashcardViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var isExiting by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Flashcards") },
                navigationIcon = {
                    TextButton(onClick = { 
                        if (!isExiting) {
                            isExiting = true
                            onNavigateBack()
                        }
                    }) { Text("Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.flashcards.isNotEmpty()) {
                val currentCard = uiState.flashcards[uiState.currentIndex]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator(
                        progress = { (uiState.currentIndex + 1).toFloat() / uiState.flashcards.size },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(32.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        var flipped by remember { mutableStateOf(false) }
                        val rotation by animateFloatAsState(targetValue = if (flipped) 180f else 0f)

                        Card(
                            onClick = { 
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                flipped = !flipped 
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    rotationY = rotation
                                    cameraDistance = 12f * density
                                },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize().padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (rotation <= 90f) {
                                    Text(
                                        text = currentCard.term,
                                        style = MaterialTheme.typography.headlineSmall,
                                        textAlign = TextAlign.Center
                                    )
                                } else {
                                    Column(
                                        modifier = Modifier.graphicsLayer { rotationY = 180f },
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Definition",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(Modifier.height(16.dp))
                                        Text(
                                            text = currentCard.definition,
                                            style = MaterialTheme.typography.titleLarge,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                        
                        LaunchedEffect(uiState.currentIndex) {
                            flipped = false
                        }
                    }

                    Button(
                        onClick = { 
                            if (uiState.currentIndex >= uiState.flashcards.size - 1) {
                                if (!isExiting) {
                                    isExiting = true
                                    onNavigateBack()
                                }
                            } else {
                                viewModel.nextCard()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (uiState.currentIndex >= uiState.flashcards.size - 1) "Finish Session" else "Next Card")
                    }
                }
            } else {
                // Empty state or error
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No flashcards found for this domain.", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { 
                        if (!isExiting) {
                            isExiting = true
                            onNavigateBack()
                        }
                    }) {
                        Text("Go Back")
                    }
                }
            }
        }
    }
}
