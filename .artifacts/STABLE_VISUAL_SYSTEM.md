# Stable Visual System Checkpoint (Elite Architect UI)

This document records the exact architectural pattern used to achieve the current professional, customizable look and feel. Use this to restore the visual state if future changes disrupt the layers.

## 1. The "Root Canvas" Pattern (`MainActivity.kt`)
The background is NOT drawn in the screens. It is drawn once at the absolute root of the app in `MainActivity`.
- **Logic**: A `Box` with a conditional `drawBehind` modifier.
- **Key Code**:
```kotlin
Box(modifier = Modifier.fillMaxSize().then(bgModifier)) {
    Surface(color = Color.Transparent) {
        NavGraph(navController = navController)
    }
}
```

## 2. Transparent Stacking Rule
Every screen (e.g., `HomeScreen.kt`, `QuizScreen.kt`) MUST follow these transparency rules:
- `Scaffold(containerColor = Color.Transparent)`
- `TopAppBar(colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))`
- If these are not transparent, they will block the global background effect.

## 3. Opaque Legibility Rule
To ensure text is always readable, study content must be placed in **Fully Opaque Cards**:
- `Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface))`
- This prevents background gradients or grids from "bleeding" into the text areas.

## 4. Theme Differentiation
- **Midnight Pro**: Uses `background = Color(0xFF001122)` and `surface = Color(0xFF001F3F)`.
- **Monochrome**: Uses `background = Color.White` and `surface = Color.White`.
- **Anthropic Blue**: Uses `background = Color(0xFFFDFBFF)` and `surface = Color.White`.

## 5. Background Effect Definitions
- **Gradient**: `Brush.verticalGradient` with 12% primary tint.
- **Mesh**: `Brush.radialGradient` with 15% primary tint.
- **Grid**: `drawLine` on Canvas with 15% primary tint and 40dp steps.
