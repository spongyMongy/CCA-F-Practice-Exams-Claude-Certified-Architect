package com.arslan.ccafprep.presentation.paywall

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arslan.ccafprep.findActivity
import com.arslan.ccafprep.presentation.components.GlassCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaywallScreen(
    viewModel: PaywallViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Go Pro", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Architect Elite",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Everything you need to pass CCA-F.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                ComparisonRow("Full Question Bank (250+)", free = false, pro = true)
                ComparisonRow("Complete Flashcard Set (125+)", free = false, pro = true)
                ComparisonRow("Timed Mock Exams", free = false, pro = true)
                ComparisonRow("Domain 1: Agentic Arch", free = true, pro = true)
                ComparisonRow("Domains 2-5: Full Content", free = false, pro = true)
                ComparisonRow("Architect Insights & Rationale", free = false, pro = true)
                ComparisonRow("Custom Themes & Backgrounds", free = false, pro = true)
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { context.findActivity()?.let { viewModel.buyFullUnlock(it) } },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("UNLOCK EVERYTHING", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("One-time purchase • $4.99", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = { /* Restore Logic */ }) {
                Text("Restore Previous Purchase", textDecoration = TextDecoration.Underline)
            }
            
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun ComparisonRow(feature: String, free: Boolean, pro: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(feature, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        
        Icon(
            imageVector = if (free) Icons.Default.CheckCircle else Icons.Default.Close,
            contentDescription = null,
            tint = if (free) Color(0xFF4CAF50) else MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(Modifier.width(24.dp))
        
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
    }
}
