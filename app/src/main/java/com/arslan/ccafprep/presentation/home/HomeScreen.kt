package com.arslan.ccafprep.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arslan.ccafprep.domain.model.ExamDomain
import com.arslan.ccafprep.presentation.components.PremiumBadge
import com.arslan.ccafprep.presentation.components.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onStartQuiz: (String, Int) -> Unit,
    onOpenFlashcards: (Int) -> Unit,
    onOpenPaywall: () -> Unit
) {
    val uiState by viewModel.homeUiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Center") },
                actions = {
                    if (!uiState.isPro) {
                        Button(
                            onClick = onOpenPaywall,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Go Pro", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                    Text("Practice", modifier = Modifier.padding(16.dp))
                }
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                    Text("Domains", modifier = Modifier.padding(16.dp))
                }
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                    Text("Flashcards", modifier = Modifier.padding(16.dp))
                }
            }

            when (selectedTab) {
                0 -> PracticeTab(uiState, onStartQuiz, onOpenPaywall)
                1 -> DomainsTab(uiState, onStartQuiz, onOpenPaywall)
                2 -> FlashcardsTab(uiState, onOpenFlashcards, onOpenPaywall)
            }
        }
    }
}

@Composable
fun PracticeTab(
    uiState: HomeUiState,
    onStartQuiz: (String, Int) -> Unit,
    onOpenPaywall: () -> Unit
) {
    var showMockDialog by remember { mutableStateOf(false) }
    var showRandomDialog by remember { mutableStateOf(false) }
    var pendingExamId by remember { mutableIntStateOf(-1) }
    
    val viewModel: HomeViewModel = hiltViewModel()

    if (showMockDialog) {
        FeedbackDialog(
            onDismiss = { showMockDialog = false },
            onConfirm = { immediate ->
                viewModel.setImmediateFeedback(immediate)
                onStartQuiz("mock", pendingExamId)
            }
        )
    }

    if (showRandomDialog) {
        FeedbackDialog(
            onDismiss = { showRandomDialog = false },
            onConfirm = { immediate ->
                viewModel.setImmediateFeedback(immediate)
                onStartQuiz("random", -1)
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionHeader("Quick Practice")
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showRandomDialog = true }
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Randomized Exam", style = MaterialTheme.typography.titleLarge)
                    Text("60 questions from all domains.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        item { SectionHeader("Mock Exams") }

        items(listOf(1, 2, 3)) { examId ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (uiState.isPro) {
                        pendingExamId = examId
                        showMockDialog = true
                    } else {
                        onOpenPaywall()
                    }
                },
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.isPro) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Full Mock Exam $examId", style = MaterialTheme.typography.titleMedium)
                        Text("Timed, 60 questions.", style = MaterialTheme.typography.bodySmall)
                    }
                    if (!uiState.isPro) PremiumBadge()
                }
            }
        }
    }
}

@Composable
fun DomainsTab(
    uiState: HomeUiState,
    onStartQuiz: (String, Int) -> Unit,
    onOpenPaywall: () -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()
    var showDomainDialog by remember { mutableStateOf(false) }
    var pendingDomainId by remember { mutableIntStateOf(-1) }

    if (showDomainDialog) {
        FeedbackDialog(
            onDismiss = { showDomainDialog = false },
            onConfirm = { immediate ->
                viewModel.setImmediateFeedback(immediate)
                onStartQuiz("domain", pendingDomainId)
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(ExamDomain.entries) { domain ->
            val isLocked = !uiState.isPro && domain.id != 1
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (!isLocked) {
                        pendingDomainId = domain.id
                        showDomainDialog = true
                    } else {
                        onOpenPaywall()
                    }
                }
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(domain.title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    if (isLocked) Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    else Icon(Icons.Default.PlayArrow, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun FlashcardsTab(
    uiState: HomeUiState,
    onOpenFlashcards: (Int) -> Unit,
    onOpenPaywall: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(ExamDomain.entries) { domain ->
            val isLocked = !uiState.isPro && domain.id != 1
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { if (!isLocked) onOpenFlashcards(domain.id) else onOpenPaywall() }
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(domain.title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    if (isLocked) PremiumBadge()
                }
            }
        }
    }
}

@Composable
fun FeedbackDialog(onDismiss: () -> Unit, onConfirm: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Practice Mode") },
        text = { Text("Show correct answers immediately?") },
        confirmButton = {
            Button(onClick = { onConfirm(true); onDismiss() }) { Text("Immediate") }
        },
        dismissButton = {
            OutlinedButton(onClick = { onConfirm(false); onDismiss() }) { Text("At the End") }
        }
    )
}

@Composable
fun WeakAreasCard(weakAreas: List<com.arslan.ccafprep.domain.usecase.WeakArea>, onUpgrade: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        modifier = Modifier.fillMaxWidth(),
        onClick = onUpgrade
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Spacer(Modifier.width(8.dp))
                Text("Focus Needed", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(8.dp))
            Text("You are scoring below 70% in ${weakAreas.first().domain.title}. Tap to see detailed recommendations.")
            TextButton(onClick = onUpgrade) {
                Text("View Detailed Analytics")
            }
        }
    }
}
