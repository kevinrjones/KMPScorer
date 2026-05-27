package cricket.knowledgespike.scorer.preferences

import arrow.core.Either
import arrow.core.right
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner
import cricket.knowledgespike.scorer.domain.preferences.AppPreferences
import cricket.knowledgespike.scorer.domain.preferences.AppThemePreference
import cricket.knowledgespike.scorer.domain.preferences.PreferencesPersistenceError
import cricket.knowledgespike.scorer.domain.preferences.PreferencesRepository
import cricket.knowledgespike.scorer.domain.preferences.RecordRecentlyAccessedMatchUseCase
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class AppPreferencesStateStoreTest {

    @Test
    fun `given stored preferences when creating state store then stored theme is exposed`() {
        val repository = FakePreferencesRepository(
            loadedPreferences = AppPreferences(activeTheme = AppThemePreference.Dark).right(),
        )

        val stateStore = AppPreferencesStateStore(preferencesRepository = repository)

        assertEquals(AppThemePreference.Dark, stateStore.state.value.preferences.activeTheme)
    }

    @Test
    fun `given duplicate match access when recording history then latest entry is moved to top`() {
        val repository = FakePreferencesRepository(loadedPreferences = AppPreferences().right())
        val stateStore = AppPreferencesStateStore(preferencesRepository = repository)

        stateStore.recordRecentlyAccessedMatch(matchSetup(teamAName = "Falcons", teamBName = "Kings"))
        stateStore.recordRecentlyAccessedMatch(matchSetup(teamAName = "Tigers", teamBName = "Lions"))
        stateStore.recordRecentlyAccessedMatch(matchSetup(teamAName = "Falcons", teamBName = "Kings"))

        val history = stateStore.state.value.preferences.recentlyAccessedMatches
        assertEquals(2, history.size)
        assertEquals("Falcons", history[0].teamAName)
        assertEquals("Tigers", history[1].teamAName)
    }

    @Test
    fun `given more than max history size when recording history then only latest entries are kept`() {
        val repository = FakePreferencesRepository(loadedPreferences = AppPreferences().right())
        val stateStore = AppPreferencesStateStore(
            preferencesRepository = repository,
            recordRecentlyAccessedMatchUseCase = RecordRecentlyAccessedMatchUseCase(maxRecentMatches = 3),
        )

        stateStore.recordRecentlyAccessedMatch(matchSetup(teamAName = "A1", teamBName = "B1"))
        stateStore.recordRecentlyAccessedMatch(matchSetup(teamAName = "A2", teamBName = "B2"))
        stateStore.recordRecentlyAccessedMatch(matchSetup(teamAName = "A3", teamBName = "B3"))
        stateStore.recordRecentlyAccessedMatch(matchSetup(teamAName = "A4", teamBName = "B4"))

        val history = stateStore.state.value.preferences.recentlyAccessedMatches
        assertEquals(listOf("A4", "A3", "A2"), history.map { it.teamAName })
    }
}

private class FakePreferencesRepository(
    private val loadedPreferences: Either<PreferencesPersistenceError, AppPreferences>,
) : PreferencesRepository {
    var savedPreferences: AppPreferences = AppPreferences()

    override fun loadPreferences(): Either<PreferencesPersistenceError, AppPreferences> {
        return loadedPreferences
    }

    override fun savePreferences(preferences: AppPreferences): Either<PreferencesPersistenceError, Unit> {
        savedPreferences = preferences
        return Unit.right()
    }
}

private fun matchSetup(teamAName: String, teamBName: String): MatchSetup {
    return MatchSetup(
        teamAName = teamAName,
        teamBName = teamBName,
        scheduledOvers = 20,
        tossWinner = TossWinner.TeamA,
        tossDecision = TossDecision.Bat,
        matchDate = LocalDate.parse("2026-05-26"),
        venue = "Oval",
        umpireOne = null,
        umpireTwo = null,
        weather = null,
    )
}
