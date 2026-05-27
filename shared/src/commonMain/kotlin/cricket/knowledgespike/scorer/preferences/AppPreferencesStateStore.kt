package cricket.knowledgespike.scorer.preferences

import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.preferences.AppPreferences
import cricket.knowledgespike.scorer.domain.preferences.AppThemePreference
import cricket.knowledgespike.scorer.domain.preferences.DesktopWindowPreferences
import cricket.knowledgespike.scorer.domain.preferences.PreferencesPersistenceError
import cricket.knowledgespike.scorer.domain.preferences.PreferencesRepository
import cricket.knowledgespike.scorer.domain.preferences.RecordRecentlyAccessedMatchUseCase
import cricket.knowledgespike.scorer.domain.preferences.RecentlyAccessedMatch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Clock

data class AppPreferencesState(
    val preferences: AppPreferences = AppPreferences(),
    val lastPersistenceError: PreferencesPersistenceError? = null,
)

class AppPreferencesStateStore(
    private val preferencesRepository: PreferencesRepository,
    private val recordRecentlyAccessedMatchUseCase: RecordRecentlyAccessedMatchUseCase = RecordRecentlyAccessedMatchUseCase(),
    private val clock: Clock = Clock.System,
) {

    private val _state = MutableStateFlow(loadInitialPreferences())
    val state: StateFlow<AppPreferencesState> = _state.asStateFlow()

    fun updateThemePreference(activeTheme: AppThemePreference) {
        updatePreferences { currentPreferences ->
            currentPreferences.copy(activeTheme = activeTheme)
        }
    }

    fun updateDesktopWindowPreferences(
        widthDp: Float,
        heightDp: Float,
        xDp: Float?,
        yDp: Float?,
    ) {
        updatePreferences { currentPreferences ->
            currentPreferences.copy(
                desktopWindowPreferences = DesktopWindowPreferences(
                    widthDp = widthDp.coerceAtLeast(1f),
                    heightDp = heightDp.coerceAtLeast(1f),
                    xDp = xDp,
                    yDp = yDp,
                ),
            )
        }
    }

    fun recordRecentlyAccessedMatch(matchSetup: MatchSetup) {
        val recentlyAccessedMatch = RecentlyAccessedMatch(
            teamAName = matchSetup.teamAName,
            teamBName = matchSetup.teamBName,
            matchDate = matchSetup.matchDate.toString(),
            venue = matchSetup.venue,
            accessedAtEpochMillis = clock.now().toEpochMilliseconds(),
        )

        updatePreferences { currentPreferences ->
            recordRecentlyAccessedMatchUseCase(
                currentPreferences = currentPreferences,
                recentlyAccessedMatch = recentlyAccessedMatch,
            )
        }
    }

    private fun loadInitialPreferences(): AppPreferencesState {
        return preferencesRepository.loadPreferences().fold(
            ifLeft = { AppPreferencesState(lastPersistenceError = it) },
            ifRight = { AppPreferencesState(preferences = it) },
        )
    }

    private fun updatePreferences(update: (AppPreferences) -> AppPreferences) {
        _state.update { currentState ->
            val updatedPreferences = update(currentState.preferences)
            val saveResult = preferencesRepository.savePreferences(updatedPreferences)

            currentState.copy(
                preferences = updatedPreferences,
                lastPersistenceError = saveResult.fold(
                    ifLeft = { it },
                    ifRight = { null },
                ),
            )
        }
    }
}
