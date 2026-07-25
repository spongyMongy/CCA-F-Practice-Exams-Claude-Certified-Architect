package com.arslan.ccafprep.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arslan.ccafprep.presentation.home.DashboardScreen
import com.arslan.ccafprep.presentation.home.HomeScreen
import com.arslan.ccafprep.presentation.quiz.QuizScreen
import com.arslan.ccafprep.presentation.progress.ProgressScreen
import com.arslan.ccafprep.presentation.paywall.PaywallScreen
import com.arslan.ccafprep.presentation.flashcard.FlashcardScreen
import com.arslan.ccafprep.presentation.settings.SettingsScreen
import com.arslan.ccafprep.presentation.analytics.AnalyticsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onStartStudy = {
                    navController.navigate(Screen.Study.route)
                },
                onViewProgress = {
                    navController.navigate(Screen.Progress.route)
                },
                onOpenPaywall = {
                    navController.navigate(Screen.Paywall.route)
                }
            )
        }

        composable(Screen.Study.route) {
            HomeScreen(
                onStartQuiz = { mode, domainId ->
                    navController.navigate(Screen.Quiz.createRoute(mode, domainId))
                },
                onOpenFlashcards = { domainId ->
                    navController.navigate(Screen.Flashcards.createRoute(domainId))
                },
                onOpenPaywall = {
                    navController.navigate(Screen.Paywall.route)
                }
            )
        }

        composable(
            route = Screen.Quiz.route,
            arguments = listOf(
                navArgument("mode") { type = NavType.StringType },
                navArgument("domainId") { type = NavType.IntType }
            )
        ) {
            QuizScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Flashcards.route,
            arguments = listOf(
                navArgument("domainId") { type = NavType.IntType }
            )
        ) {
            FlashcardScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Progress.route) {
            ProgressScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Paywall.route) {
            PaywallScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenPaywall = { navController.navigate(Screen.Paywall.route) }
            )
        }

        composable(Screen.Analytics.route) {
            AnalyticsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
