package com.arslan.ccafprep.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.arslan.ccafprep.domain.model.theme.AppTheme

private val MidnightColorScheme = darkColorScheme(
    primary = MidnightSecondary,
    secondary = MidnightAccent,
    background = Color(0xFF001122), // Deep OLED Navy
    surface = Color(0xFF001F3F)      // Dark Navy Cards
)

private val ForestColorScheme = lightColorScheme(
    primary = ForestPrimary,
    secondary = ForestSecondary,
    background = Color(0xFFF1F8E9),
    surface = Color.White
)

private val SunsetColorScheme = lightColorScheme(
    primary = SunsetPrimary,
    secondary = SunsetSecondary,
    background = Color(0xFFFFF3E0),
    surface = Color.White
)

private val MonochromeColorScheme = lightColorScheme(
    primary = Color(0xFF212121),
    secondary = Color(0xFF757575),
    background = Color.White,
    surface = Color.White
)

private val DefaultColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFDFBFF),
    surface = Color.White
)

@Composable
fun CcafPrepTheme(
    appTheme: AppTheme = AppTheme.DEFAULT,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.MIDNIGHT -> MidnightColorScheme
        AppTheme.FOREST -> ForestColorScheme
        AppTheme.SUNSET -> SunsetColorScheme
        AppTheme.MONOCHROME -> if (darkTheme) darkColorScheme(primary = Color.White, background = Color.Black, surface = Color(0xFF121212)) else MonochromeColorScheme
        AppTheme.DEFAULT -> if (darkTheme) darkColorScheme() else DefaultColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
