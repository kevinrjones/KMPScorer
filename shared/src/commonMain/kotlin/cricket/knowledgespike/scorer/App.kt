package cricket.knowledgespike.scorer

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cricket.knowledgespike.scorer.data.source.InMemoryMatchRepository
import cricket.knowledgespike.scorer.domain.match.CreateAndSaveMatchUseCase
import cricket.knowledgespike.scorer.domain.matchsetup.CreateMatchSetupUseCase
import cricket.knowledgespike.scorer.domain.preferences.AppThemePreference
import cricket.knowledgespike.scorer.domain.repository.MatchRepository
import cricket.knowledgespike.scorer.home.HomeScreen
import cricket.knowledgespike.scorer.home.HomeScreenEvent
import cricket.knowledgespike.scorer.home.HomeStateStore
import cricket.knowledgespike.scorer.matchsetup.MatchSetupScreen
import cricket.knowledgespike.scorer.matchsetup.MatchSetupScreenEvent
import cricket.knowledgespike.scorer.matchsetup.MatchSetupScreenState
import cricket.knowledgespike.scorer.matchsetup.MatchSetupStateStore
import cricket.knowledgespike.scorer.matchsummary.MatchSummaryScreen
import cricket.knowledgespike.scorer.matchsummary.MatchSummaryStateStore
import cricket.knowledgespike.scorer.navigation.AppRouteStateStore
import cricket.knowledgespike.scorer.navigation.PlatformBackHandler
import cricket.knowledgespike.scorer.navigation.ScorerRoute
import cricket.knowledgespike.scorer.preferences.AppPreferencesStateStore
import cricket.knowledgespike.scorer.preferences.InMemoryPreferencesRepository
import cricket.knowledgespike.scorer.ui.theme.ScorerSpacing
import cricket.knowledgespike.scorer.ui.theme.ScorerTheme

private const val ScoringEntryBackButtonTag = "scoring_entry_back_to_setup_button"
const val AppBackButtonTag = "app_back_button"
const val AppNavigationHomeTag = "app_navigation_home"
const val AppNavigationNewMatchTag = "app_navigation_new_match"


@Composable
@Preview
fun App(
    widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Medium,
    resetMatchSetupSignal: Int = 0,
    appPreferencesStateStore: AppPreferencesStateStore? = null,
    appRouteStateStore: AppRouteStateStore? = null,
    matchRepository: MatchRepository? = null,
) {
    val resolvedAppPreferencesStateStore = appPreferencesStateStore ?: remember {
        AppPreferencesStateStore(preferencesRepository = InMemoryPreferencesRepository)
    }
    val resolvedAppRouteStateStore = appRouteStateStore ?: remember { AppRouteStateStore() }
    val resolvedMatchRepository = matchRepository ?: remember { InMemoryMatchRepository() }

    val appPreferencesState by resolvedAppPreferencesStateStore.state.collectAsStateWithLifecycle()
    val routeStack by resolvedAppRouteStateStore.routeStack.collectAsStateWithLifecycle()
    val currentRoute by resolvedAppRouteStateStore.currentRoute.collectAsStateWithLifecycle()
    val canPopBack = routeStack.size > 1
    val isExpandedLayout = widthSizeClass == WindowWidthSizeClass.Expanded
    val onRouteRequested: (ScorerRoute) -> Unit by rememberUpdatedState(
        newValue = { route ->
            when (route) {
                is ScorerRoute.ScoringEntryRoute -> resolvedAppRouteStateStore.replaceTop(route)
                else -> resolvedAppRouteStateStore.push(route)
            }
        },
    )

    val createAndSaveMatchUseCase = remember(resolvedMatchRepository) {
        CreateAndSaveMatchUseCase(matchRepository = resolvedMatchRepository)
    }
    val matchSetupStateStore = remember(resolvedAppPreferencesStateStore, createAndSaveMatchUseCase) {
        MatchSetupStateStore(
            createMatchSetupUseCase = CreateMatchSetupUseCase(),
            createAndSaveMatchUseCase = createAndSaveMatchUseCase,
            onRouteRequested = { onRouteRequested(it) },
        )
    }
    val matchSetupScreenState by matchSetupStateStore.screenState.collectAsStateWithLifecycle()
    val homeStateStore = remember(resolvedMatchRepository, onRouteRequested) {
        HomeStateStore(
            matchRepository = resolvedMatchRepository,
            onRouteRequested = onRouteRequested,
        )
    }
    val homeScreenState by homeStateStore.screenState.collectAsStateWithLifecycle()
    val systemDarkTheme = isSystemInDarkTheme()
    val useDarkTheme = appPreferencesState.preferences.activeTheme.toDarkTheme(systemDarkTheme)

    LaunchedEffect(resetMatchSetupSignal) {
        if (resetMatchSetupSignal > 0) {
            matchSetupStateStore.onEvent(MatchSetupScreenEvent.ResetRequested)
            resolvedAppRouteStateStore.push(ScorerRoute.MatchSetupRoute)
        }
    }

    LaunchedEffect(currentRoute) {
        if (currentRoute == ScorerRoute.HomeRoute) {
            homeStateStore.onEvent(HomeScreenEvent.RefreshRequested)
        }
    }

    PlatformBackHandler(enabled = canPopBack) {
        resolvedAppRouteStateStore.pop()
    }

    ScorerTheme(darkTheme = useDarkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                AppTopBar(
                    currentRoute = currentRoute,
                    canPopBack = canPopBack,
                    onBackRequested = { resolvedAppRouteStateStore.pop() },
                )
            },
        ) { innerPadding ->
            if (isExpandedLayout) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    AppNavigationRail(
                        currentRoute = currentRoute,
                        onGoHome = resolvedAppRouteStateStore::resetToHome,
                        onNewMatchRequested = { resolvedAppRouteStateStore.push(ScorerRoute.MatchSetupRoute) },
                    )
                    AppRouteContent(
                        widthSizeClass = widthSizeClass,
                        route = currentRoute,
                        contentPadding = PaddingValues(ScorerSpacing.Large),
                        homeStateStore = homeStateStore,
                        homeScreenState = homeScreenState,
                        matchSetupStateStore = matchSetupStateStore,
                        matchSetupScreenState = matchSetupScreenState,
                        matchRepository = resolvedMatchRepository,
                        onBackToMatchSetup = {
                            resolvedAppRouteStateStore.replaceTop(ScorerRoute.MatchSetupRoute)
                        },
                    )
                }
            } else {
                AppRouteContent(
                    widthSizeClass = widthSizeClass,
                    route = currentRoute,
                    contentPadding = innerPadding,
                    homeStateStore = homeStateStore,
                    homeScreenState = homeScreenState,
                    matchSetupStateStore = matchSetupStateStore,
                    matchSetupScreenState = matchSetupScreenState,
                    matchRepository = resolvedMatchRepository,
                    onBackToMatchSetup = {
                        resolvedAppRouteStateStore.replaceTop(ScorerRoute.MatchSetupRoute)
                    },
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTopBar(
    currentRoute: ScorerRoute,
    canPopBack: Boolean,
    onBackRequested: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(currentRoute.toTopBarTitle())
        },
        navigationIcon = {
            if (canPopBack) {
                IconButton(
                    modifier = Modifier.testTag(AppBackButtonTag),
                    onClick = onBackRequested,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }
        },
    )
}

@Composable
private fun AppNavigationRail(
    currentRoute: ScorerRoute,
    onGoHome: () -> Unit,
    onNewMatchRequested: () -> Unit,
) {
    NavigationRail {
        NavigationRailItem(
            modifier = Modifier.testTag(AppNavigationHomeTag),
            selected = currentRoute == ScorerRoute.HomeRoute,
            onClick = onGoHome,
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Go Home",
                )
            },
            label = {
                Text("Home")
            },
        )
        NavigationRailItem(
            modifier = Modifier.testTag(AppNavigationNewMatchTag),
            selected = currentRoute == ScorerRoute.MatchSetupRoute,
            onClick = onNewMatchRequested,
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Match",
                )
            },
            label = {
                Text("New Match")
            },
        )
    }
}

@Composable
private fun AppRouteContent(
    widthSizeClass: WindowWidthSizeClass,
    route: ScorerRoute,
    contentPadding: PaddingValues,
    homeStateStore: HomeStateStore,
    homeScreenState: cricket.knowledgespike.scorer.home.HomeScreenState,
    matchSetupStateStore: MatchSetupStateStore,
    matchSetupScreenState: MatchSetupScreenState,
    matchRepository: MatchRepository,
    onBackToMatchSetup: () -> Unit,
) {
    when (route) {
        ScorerRoute.HomeRoute -> {
            HomeScreen(
                screenState = homeScreenState,
                contentPadding = contentPadding,
                onEvent = homeStateStore::onEvent,
            )
        }

        ScorerRoute.MatchSetupRoute -> {
            MatchSetupScreen(
                widthSizeClass = widthSizeClass,
                screenState = matchSetupScreenState,
                contentPadding = contentPadding,
                onEvent = matchSetupStateStore::onEvent,
            )
        }

        is ScorerRoute.ScoringEntryRoute -> {
            ScoringEntryRouteContent(
                route = route,
                contentPadding = contentPadding,
                onBackToMatchSetup = onBackToMatchSetup,
            )
        }

        is ScorerRoute.MatchSummaryRoute -> {
            val matchSummaryStateStore = remember(route.matchId, matchRepository) {
                MatchSummaryStateStore(
                    matchId = route.matchId,
                    matchRepository = matchRepository,
                )
            }
            val matchSummaryScreenState by matchSummaryStateStore.screenState.collectAsStateWithLifecycle()

            MatchSummaryScreen(
                screenState = matchSummaryScreenState,
                contentPadding = contentPadding,
                onEvent = matchSummaryStateStore::onEvent,
            )
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
            text = "Scoring entry ready for match #${route.matchId}.",
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

private fun ScorerRoute.toTopBarTitle(): String {
    return when (this) {
        ScorerRoute.HomeRoute -> "Matches"
        ScorerRoute.MatchSetupRoute -> "Match Setup"
        is ScorerRoute.ScoringEntryRoute -> "Scoring"
        is ScorerRoute.MatchSummaryRoute -> "Match Summary"
    }
}

private fun AppThemePreference.toDarkTheme(systemDarkTheme: Boolean): Boolean {
    return when (this) {
        AppThemePreference.System -> systemDarkTheme
        AppThemePreference.Light -> false
        AppThemePreference.Dark -> true
    }
}

