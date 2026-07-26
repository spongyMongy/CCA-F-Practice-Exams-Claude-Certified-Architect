package com.arslan.ccafprep.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arslan.ccafprep.data.local.SettingsManager
import com.arslan.ccafprep.domain.model.theme.AppTheme
import com.arslan.ccafprep.domain.model.theme.BackgroundStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager,
    private val seeder: com.arslan.ccafprep.data.local.seed.DatabaseSeeder
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsManager.selectedTheme,
        settingsManager.backgroundStyle,
        settingsManager.isProUnlocked,
        settingsManager.showTimer,
        settingsManager.feedbackMode
    ) { theme, background, isPro, showTimer, immediateFeedback ->
        SettingsUiState(theme, background, isPro, showTimer, immediateFeedback)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SettingsUiState())

    fun setTheme(theme: AppTheme) = viewModelScope.launch {
        settingsManager.setTheme(theme)
    }

    fun setBackground(style: BackgroundStyle) = viewModelScope.launch {
        settingsManager.setBackgroundStyle(style)
    }

    fun setShowTimer(show: Boolean) = viewModelScope.launch {
        settingsManager.setShowTimer(show)
    }

    fun setImmediateFeedback(immediate: Boolean) = viewModelScope.launch {
        settingsManager.setImmediateFeedback(immediate)
    }

    fun togglePro() = viewModelScope.launch {
        settingsManager.setProUnlocked(!uiState.value.isPro)
    }

    fun forceSync() = viewModelScope.launch {
        settingsManager.setSeeded(false)
        seeder.seedIfNeeded()
    }
}

data class SettingsUiState(
    val theme: AppTheme = AppTheme.DEFAULT,
    val background: BackgroundStyle = BackgroundStyle.SOLID,
    val isPro: Boolean = false,
    val showTimer: Boolean = true,
    val immediateFeedback: Boolean = true
)
