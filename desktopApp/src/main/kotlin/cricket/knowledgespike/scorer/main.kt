package cricket.knowledgespike.scorer

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cricket.knowledgespike.scorer.domain.preferences.AppThemePreference
import cricket.knowledgespike.scorer.domain.preferences.DesktopWindowPreferences
import cricket.knowledgespike.scorer.preferences.AppPreferencesStateStore
import cricket.knowledgespike.scorer.preferences.JsonPreferencesRepository
import cricket.knowledgespike.scorer.preferences.OkioPreferencesStorageDataSource
import kotlinx.coroutines.flow.distinctUntilChanged
import java.io.File

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun main() {

    application {
        val appPreferencesStateStore = remember {
            val preferencesFilePath = File(
                File(System.getProperty("user.home"), ".kmpscorer"),
                "app_preferences.json",
            ).absolutePath

            AppPreferencesStateStore(
                preferencesRepository = JsonPreferencesRepository(
                    preferencesStorageDataSource = OkioPreferencesStorageDataSource(preferencesFilePath),
                ),
            )
        }

        val appPreferencesState by appPreferencesStateStore.state.collectAsState()
        val desktopWindowPreferences = appPreferencesState.preferences.desktopWindowPreferences

        val windowState = rememberWindowState(
            width = desktopWindowPreferences.widthDp.dp,
            height = desktopWindowPreferences.heightDp.dp,
            position = desktopWindowPreferences.toWindowPosition(),
        )

        Window(
            onCloseRequest = ::exitApplication,
            title = "Knowledgespike Cricket Scorer",
            state = windowState,
        ) {
            var resetMatchSetupSignal by remember { mutableIntStateOf(0) }
            val activeThemePreference = appPreferencesState.preferences.activeTheme

            LaunchedEffect(windowState) {
                snapshotFlow { windowState.size to windowState.position }
                    .distinctUntilChanged()
                    .collect { (windowSize, windowPosition) ->
                        val widthDp = windowSize.width.value
                        val heightDp = windowSize.height.value
                        if (!widthDp.isFinite() || !heightDp.isFinite()) {
                            return@collect
                        }

                        val xDp = (windowPosition as? WindowPosition.Absolute)?.x?.value
                        val yDp = (windowPosition as? WindowPosition.Absolute)?.y?.value

                        appPreferencesStateStore.updateDesktopWindowPreferences(
                            widthDp = widthDp,
                            heightDp = heightDp,
                            xDp = xDp,
                            yDp = yDp,
                        )
                    }
            }

            MenuBar {
                Menu("Match") {
                    Item(
                        text = "New Match Setup",
                        onClick = { resetMatchSetupSignal += 1 },
                    )
                }

                Menu("Theme") {
                    Item(
                        text = activeThemePreference.asMenuLabelFor(AppThemePreference.System, "System"),
                        onClick = { appPreferencesStateStore.updateThemePreference(AppThemePreference.System) },
                    )
                    Item(
                        text = activeThemePreference.asMenuLabelFor(AppThemePreference.Light, "Light"),
                        onClick = { appPreferencesStateStore.updateThemePreference(AppThemePreference.Light) },
                    )
                    Item(
                        text = activeThemePreference.asMenuLabelFor(AppThemePreference.Dark, "Dark"),
                        onClick = { appPreferencesStateStore.updateThemePreference(AppThemePreference.Dark) },
                    )
                }
            }

            val widthSizeClass = calculateWindowSizeClass().widthSizeClass
            App(
                widthSizeClass = widthSizeClass,
                resetMatchSetupSignal = resetMatchSetupSignal,
                appPreferencesStateStore = appPreferencesStateStore,
            )
        }
    }
}

private fun DesktopWindowPreferences.toWindowPosition(): WindowPosition {
    val resolvedX = xDp
    val resolvedY = yDp
    if (resolvedX == null || resolvedY == null) {
        return WindowPosition(Alignment.Center)
    }

    return WindowPosition(resolvedX.dp, resolvedY.dp)
}

private fun AppThemePreference.asMenuLabelFor(candidate: AppThemePreference, label: String): String {
    return if (this == candidate) {
        "✓ $label"
    } else {
        label
    }
}