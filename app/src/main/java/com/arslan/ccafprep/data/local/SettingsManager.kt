package com.arslan.ccafprep.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.arslan.ccafprep.domain.model.theme.AppTheme
import com.arslan.ccafprep.domain.model.theme.BackgroundStyle
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("settings")

@Singleton
class SettingsManager @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.dataStore

    val isSeeded: Flow<Boolean> = dataStore.data.map { it[IS_SEEDED] ?: false }
    suspend fun setSeeded(seeded: Boolean) = dataStore.edit { it[IS_SEEDED] = seeded }

    val isProUnlocked: Flow<Boolean> = dataStore.data.map { it[IS_PRO_UNLOCKED] ?: false }
    suspend fun setProUnlocked(unlocked: Boolean) = dataStore.edit { it[IS_PRO_UNLOCKED] = unlocked }

    val selectedTheme: Flow<AppTheme> = dataStore.data.map { 
        AppTheme.valueOf(it[SELECTED_THEME] ?: AppTheme.DEFAULT.name) 
    }
    suspend fun setTheme(theme: AppTheme) = dataStore.edit { it[SELECTED_THEME] = theme.name }

    val backgroundStyle: Flow<BackgroundStyle> = dataStore.data.map { 
        BackgroundStyle.valueOf(it[BACKGROUND_STYLE] ?: BackgroundStyle.SOLID.name) 
    }
    suspend fun setBackgroundStyle(style: BackgroundStyle) = dataStore.edit { it[BACKGROUND_STYLE] = style.name }

    // QUIZ PREFERENCES
    val showTimer: Flow<Boolean> = dataStore.data.map { it[SHOW_TIMER] ?: true }
    suspend fun setShowTimer(show: Boolean) = dataStore.edit { it[SHOW_TIMER] = show }

    val feedbackMode: Flow<Boolean> = dataStore.data.map { it[IMMEDIATE_FEEDBACK] ?: true }
    suspend fun setImmediateFeedback(immediate: Boolean) = dataStore.edit { it[IMMEDIATE_FEEDBACK] = immediate }

    companion object {
        private val IS_SEEDED = booleanPreferencesKey("is_seeded")
        private val IS_PRO_UNLOCKED = booleanPreferencesKey("is_pro_unlocked")
        private val SELECTED_THEME = stringPreferencesKey("selected_theme")
        private val BACKGROUND_STYLE = stringPreferencesKey("background_style")
        private val SHOW_TIMER = booleanPreferencesKey("show_timer")
        private val IMMEDIATE_FEEDBACK = booleanPreferencesKey("immediate_feedback")
    }
}
