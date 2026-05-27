package cricket.knowledgespike.scorer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cricket.knowledgespike.scorer.domain.matchsetup.CreateMatchSetupUseCase
import cricket.knowledgespike.scorer.domain.preferences.AppThemePreference
import cricket.knowledgespike.scorer.matchsetup.MatchSetupScreen
import cricket.knowledgespike.scorer.matchsetup.MatchSetupScreenEvent
import cricket.knowledgespike.scorer.matchsetup.MatchSetupStateStore
import cricket.knowledgespike.scorer.navigation.AppRouteStateStore
import cricket.knowledgespike.scorer.navigation.ScorerRoute
import cricket.knowledgespike.scorer.preferences.AppPreferencesStateStore
import cricket.knowledgespike.scorer.preferences.InMemoryPreferencesRepository
import cricket.knowledgespike.scorer.ui.theme.ScorerSpacing
import cricket.knowledgespike.scorer.ui.theme.ScorerTheme
import androidx.compose.ui.platform.testTag

private const val ScoringEntryBackButtonTag = "scoring_entry_back_to_setup_button"


@Composable
@Preview
fun App(
    widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Medium,
    resetMatchSetupSignal: Int = 0,
    appPreferencesStateStore: AppPreferencesStateStore? = null,
    appRouteStateStore: AppRouteStateStore? = null,
) {
    val resolvedAppPreferencesStateStore = appPreferencesStateStore ?: remember {
        AppPreferencesStateStore(preferencesRepository = InMemoryPreferencesRepository)
    }
    val resolvedAppRouteStateStore = appRouteStateStore ?: remember { AppRouteStateStore() }

    val appPreferencesState by resolvedAppPreferencesStateStore.state.collectAsStateWithLifecycle()
    val currentRoute by resolvedAppRouteStateStore.currentRoute.collectAsStateWithLifecycle()
    val onRouteRequested: (ScorerRoute) -> Unit by rememberUpdatedState(
        newValue = { route: ScorerRoute ->
            if (route is ScorerRoute.ScoringEntryRoute) {
                resolvedAppPreferencesStateStore.recordRecentlyAccessedMatch(route.matchSetup)
            }
            resolvedAppRouteStateStore.showRoute(route)
        },
    )

    val matchSetupStateStore = remember(resolvedAppPreferencesStateStore) {
        MatchSetupStateStore(
            createMatchSetupUseCase = CreateMatchSetupUseCase(),
            onRouteRequested = { onRouteRequested(it) },
        )
    }
    val matchSetupScreenState by matchSetupStateStore.screenState.collectAsStateWithLifecycle()
    val systemDarkTheme = isSystemInDarkTheme()
    val useDarkTheme = appPreferencesState.preferences.activeTheme.toDarkTheme(systemDarkTheme)

    LaunchedEffect(resetMatchSetupSignal) {
        if (resetMatchSetupSignal > 0) {
            matchSetupStateStore.onEvent(MatchSetupScreenEvent.ResetRequested)
            resolvedAppRouteStateStore.showMatchSetup()
        }
    }

    ScorerTheme(darkTheme = useDarkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
        ) { innerPadding ->
            when (val route = currentRoute) {
                ScorerRoute.MatchSetupRoute -> {
                    MatchSetupScreen(
                        widthSizeClass = widthSizeClass,
                        screenState = matchSetupScreenState,
                        contentPadding = innerPadding,
                        onEvent = matchSetupStateStore::onEvent,
                    )
                }

                is ScorerRoute.ScoringEntryRoute -> {
                    ScoringEntryRouteContent(
                        route = route,
                        contentPadding = innerPadding,
                        onBackToMatchSetup = resolvedAppRouteStateStore::showMatchSetup,
                    )
                }
            }
        }
    }

}

@Composable
private fun ScoringEntryRouteContent(
    route: ScorerRoute.ScoringEntryRoute,
    contentPadding: PaddingValues,
    onBackToMatchSetup: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(ScorerSpacing.Large),
        verticalArrangement = Arrangement.spacedBy(ScorerSpacing.Medium),
    ) {
        Text(
            text = "Scoring placeholder",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Scoring entry ready for ${route.matchSetup.teamAName} vs ${route.matchSetup.teamBName}.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(ScoringEntryBackButtonTag),
            onClick = onBackToMatchSetup,
        ) {
            Text("Back to Match setup")
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

