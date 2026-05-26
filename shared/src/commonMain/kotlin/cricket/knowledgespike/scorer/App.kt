package cricket.knowledgespike.scorer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cricket.knowledgespike.scorer.matchsetup.MatchSetupScreen
import cricket.knowledgespike.scorer.matchsetup.MatchSetupScreenEvent
import cricket.knowledgespike.scorer.matchsetup.MatchSetupStateStore
import cricket.knowledgespike.scorer.navigation.ScorerRoute
import cricket.knowledgespike.scorer.ui.theme.ScorerTheme
import cricket.knowledgespike.scorer.domain.matchsetup.CreateMatchSetupUseCase


@Composable
@Preview
fun App(
    widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Medium,
    resetMatchSetupSignal: Int = 0,
) {
    val matchSetupStateStore = remember {
        MatchSetupStateStore(
            createMatchSetupUseCase = CreateMatchSetupUseCase(),
        )
    }
    val matchSetupScreenState by matchSetupStateStore.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(resetMatchSetupSignal) {
        if (resetMatchSetupSignal > 0) {
            matchSetupStateStore.onEvent(MatchSetupScreenEvent.ResetRequested)
        }
    }

    ScorerTheme {
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

