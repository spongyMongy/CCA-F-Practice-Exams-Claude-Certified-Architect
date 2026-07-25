package com.arslan.ccafprep.presentation.analytics

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ScoreWheel(score: Float) {
    val animatedScore = remember { Animatable(0f) }
    LaunchedEffect(score) {
        animatedScore.animateTo(score, animationSpec = tween(1500))
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().height(200.dp)) {
        val color = when {
            score >= 0.8f -> Color(0xFF4CAF50)
            score >= 0.6f -> Color(0xFFFBC02D)
            else -> Color(0xFFE53935)
        }
        
        Canvas(modifier = Modifier.size(150.dp)) {
            drawArc(
                color = color.copy(alpha = 0.2f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = animatedScore.value * 360f,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${(score * 100).toInt()}%", style = MaterialTheme.typography.headlineLarge)
            Text("READINESS", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun MasteryRadarChart(
    domainScores: List<Pair<String, Float>>,
    modifier: Modifier = Modifier
) {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(domainScores) {
        animatedProgress.animateTo(1f, animationSpec = tween(1500))
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.outlineVariant

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Domain Mastery Index", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))
        Canvas(modifier = Modifier.size(240.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width / 2 * 0.8f
            val numDomains = domainScores.size
            if (numDomains < 3) return@Canvas
            
            val angleStep = (2 * Math.PI / numDomains).toFloat()

            // Draw Background Grid
            for (i in 1..4) {
                val currentRadius = radius * (i / 4f)
                val path = Path()
                for (j in 0 until numDomains) {
                    val angle = j * angleStep - Math.PI.toFloat() / 2
                    val x = center.x + currentRadius * cos(angle)
                    val y = center.y + currentRadius * sin(angle)
                    if (j == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(path, color = gridColor, style = Stroke(width = 1.dp.toPx()))
            }

            // Draw Score Path
            val scorePath = Path()
            domainScores.forEachIndexed { index, score ->
                val angle = index * angleStep - Math.PI.toFloat() / 2
                val targetRadius = radius * score.second.coerceIn(0.1f, 1f)
                val currentRadius = targetRadius * animatedProgress.value
                val x = center.x + currentRadius * cos(angle)
                val y = center.y + currentRadius * sin(angle)
                if (index == 0) scorePath.moveTo(x, y) else scorePath.lineTo(x, y)
            }
            scorePath.close()
            drawPath(scorePath, color = primaryColor.copy(alpha = 0.3f))
            drawPath(scorePath, color = primaryColor, style = Stroke(width = 3.dp.toPx()))
        }
    }
}

@Composable
fun StudyActivityHeatmap(
    activityData: List<Int>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Study Consistency (Last 28 Days)", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            activityData.takeLast(28).chunked(7).forEach { week ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    week.forEach { intensity ->
                        val color = when (intensity) {
                            0 -> MaterialTheme.colorScheme.surfaceVariant
                            1 -> Color(0xFFC8E6C9)
                            2 -> Color(0xFF81C784)
                            3 -> Color(0xFF4CAF50)
                            else -> Color(0xFF2E7D32)
                        }
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(color, MaterialTheme.shapes.extraSmall)
                        )
                    }
                }
            }
        }
    }
}
