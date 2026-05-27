package cricket.knowledgespike.scorer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cricket.knowledgespike.scorer.domain.preferences.AppThemePreference
import cricket.knowledgespike.scorer.matchsetup.MatchSetupScreen
import cricket.knowledgespike.scorer.matchsetup.MatchSetupScreenEvent
import cricket.knowledgespike.scorer.matchsetup.MatchSetupStateStore
import cricket.knowledgespike.scorer.navigation.ScorerRoute
import cricket.knowledgespike.scorer.preferences.AppPreferencesStateStore
import cricket.knowledgespike.scorer.preferences.InMemoryPreferencesRepository
import cricket.knowledgespike.scorer.ui.theme.ScorerTheme
import cricket.knowledgespike.scorer.domain.matchsetup.CreateMatchSetupUseCase


@Composable
@Preview
fun App(
    widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Medium,
    resetMatchSetupSignal: Int = 0,
    appPreferencesStateStore: AppPreferencesStateStore? = null,
) {
    val resolvedAppPreferencesStateStore = appPreferencesStateStore ?: remember {
        AppPreferencesStateStore(preferencesRepository = InMemoryPreferencesRepository)
    }

    val appPreferencesState by resolvedAppPreferencesStateStore.state.collectAsStateWithLifecycle()
    val onMatchSetupReady by rememberUpdatedState(newValue = resolvedAppPreferencesStateStore::recordRecentlyAccessedMatch)

    val matchSetupStateStore = remember(resolvedAppPreferencesStateStore) {
        MatchSetupStateStore(
            createMatchSetupUseCase = CreateMatchSetupUseCase(),
            onMatchSetupReady = { onMatchSetupReady(it) },
        )
    }
    val matchSetupScreenState by matchSetupStateStore.screenState.collectAsStateWithLifecycle()
    val systemDarkTheme = isSystemInDarkTheme()
    val useDarkTheme = appPreferencesState.preferences.activeTheme.toDarkTheme(systemDarkTheme)

    LaunchedEffect(resetMatchSetupSignal) {
        if (resetMatchSetupSignal > 0) {
            matchSetupStateStore.onEvent(MatchSetupScreenEvent.ResetRequested)
        }
    }

    ScorerTheme(darkTheme = useDarkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
        ) { innerPadding ->
            when (ScorerRoute.MatchSetupRoute) {
                ScorerRoute.MatchSetupRoute -> {
                    MatchSetupScreen(
                        widthSizeClass = widthSizeClass,
                        screenState = matchSetupScreenState,
                        contentPadding = innerPadding,
                        onEvent = matchSetupStateStore::onEvent,
                    )
                }
            }
        }
    }

}

private fun AppThemePreference.toDarkTheme(systemDarkTheme: Boolean): Boolean {
    return when (this) {
        AppThemePreference.System -> systemDarkTheme
        AppThemePreference.Light -> false
        AppThemePreference.Dark -> true
    }
}

