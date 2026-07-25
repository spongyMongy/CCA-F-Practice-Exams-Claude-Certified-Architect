package com.arslan.ccafprep.domain.model.theme

enum class AppTheme(val displayName: String) {
    DEFAULT("Anthropic Blue"),
    MIDNIGHT("Midnight Pro"),
    FOREST("Forest Guide"),
    SUNSET("Sunset Architect"),
    MONOCHROME("Monochrome")
}

enum class BackgroundStyle(val displayName: String) {
    NONE("No Effect (Default)"),
    SOLID("Tinted Solid"),
    GRADIENT("Subtle Gradient"),
    MESH("Glassmorphism Mesh"),
    GRID("Architect Grid")
}
