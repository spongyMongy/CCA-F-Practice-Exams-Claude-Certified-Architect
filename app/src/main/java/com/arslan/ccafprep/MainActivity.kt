package com.arslan.ccafprep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.arslan.ccafprep.data.local.SettingsManager
import com.arslan.ccafprep.domain.model.theme.AppTheme
import com.arslan.ccafprep.domain.model.theme.BackgroundStyle
import com.arslan.ccafprep.presentation.navigation.MainScaffold
import com.arslan.ccafprep.ui.theme.CcafPrepTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            settingsManager.setProUnlocked(true)
            settingsManager.setTheme(AppTheme.SUNSET)
            settingsManager.setBackgroundStyle(BackgroundStyle.MESH)
        }

        setContent {
            val theme by settingsManager.selectedTheme.collectAsState(initial = AppTheme.DEFAULT)
            val bgStyle by settingsManager.backgroundStyle.collectAsState(initial = BackgroundStyle.NONE)
            
            CcafPrepTheme(appTheme = theme) {
                // LEGIBILITY FIRST: The actual color of the theme's background
                val baseBackgroundColor = MaterialTheme.colorScheme.background
                val primaryColor = MaterialTheme.colorScheme.primary

                val bgModifier = Modifier
                    .fillMaxSize()
                    .background(baseBackgroundColor) 
                    .drawBehind {
                        when (bgStyle) {
                            BackgroundStyle.GRADIENT -> {
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, primaryColor.copy(alpha = 0.2f))
                                    )
                                )
                            }
                            BackgroundStyle.MESH -> {
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(primaryColor.copy(alpha = 0.25f), Color.Transparent)
                                    ),
                                    radius = size.maxDimension,
                                    center = Offset(size.width, 0f)
                                )
                            }
                            BackgroundStyle.GRID -> {
                                val step = 40.dp.toPx()
                                for (i in 0 until (size.width / step).toInt() + 1) {
                                    drawLine(
                                        color = primaryColor.copy(alpha = 0.15f),
                                        start = Offset(i * step, 0f),
                                        end = Offset(i * step, size.height),
                                        strokeWidth = 1.dp.toPx()
                                    )
                                }
                                for (i in 0 until (size.height / step).toInt() + 1) {
                                    drawLine(
                                        color = primaryColor.copy(alpha = 0.15f),
                                        start = Offset(0f, i * step),
                                        end = Offset(size.width, i * step),
                                        strokeWidth = 1.dp.toPx()
                                    )
                                }
                            }
                            BackgroundStyle.SOLID -> {
                                drawRect(primaryColor.copy(alpha = 0.05f))
                            }
                            else -> {}
                        }
                    }

                Surface(
                    modifier = bgModifier,
                    color = Color.Transparent 
                ) {
                    MainScaffold()
                }
            }
        }
    }
}
