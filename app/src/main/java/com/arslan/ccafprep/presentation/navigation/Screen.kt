package com.arslan.ccafprep.presentation.navigation

sealed class Screen(val route: String) {
    // Bottom Nav Destinations
    object Dashboard : Screen("dashboard")
    object Study : Screen("study")
    object Progress : Screen("progress")
    object Settings : Screen("settings")

    // Feature Screens
    object Quiz : Screen("quiz/{mode}/{domainId}") {
        fun createRoute(mode: String, domainId: Int = -1) = "quiz/$mode/$domainId"
    }
    object Flashcards : Screen("flashcards/{domainId}") {
        fun createRoute(domainId: Int = -1) = "flashcards/$domainId"
    }
    object Paywall : Screen("paywall")
    object Analytics : Screen("analytics")

    companion object {
        @Deprecated("Use Dashboard or Study instead", ReplaceWith("Dashboard"))
        val Home = Dashboard
    }
}
