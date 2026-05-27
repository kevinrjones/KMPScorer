package cricket.knowledgespike.scorer.preferences

import arrow.core.Either
import arrow.core.right
import cricket.knowledgespike.scorer.domain.preferences.AppPreferences
import cricket.knowledgespike.scorer.domain.preferences.AppThemePreference
import cricket.knowledgespike.scorer.domain.preferences.PreferencesPersistenceError
import cricket.knowledgespike.scorer.domain.preferences.RecentlyAccessedMatch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class JsonPreferencesRepositoryTest {

    @Test
    fun `given empty storage when loading preferences then defaults are returned`() {
        val repository = JsonPreferencesRepository(
            preferencesStorageDataSource = FakePreferencesStorageDataSource(initialContent = null),
        )

        val loadResult = repository.loadPreferences()

        assertEquals(AppPreferences().right(), loadResult)
    }

    @Test
    fun `given malformed json when loading preferences then read error is returned`() {
        val repository = JsonPreferencesRepository(
            preferencesStorageDataSource = FakePreferencesStorageDataSource(initialContent = "{bad-json"),
        )

        val loadResult = repository.loadPreferences()

        assertTrue(loadResult.isLeft())
        assertIs<PreferencesPersistenceError.UnableToReadPreferences>(loadResult.leftOrNull())
    }

    @Test
    fun `given saved preferences when loading preferences then stored values are restored`() {
        val fakeDataSource = FakePreferencesStorageDataSource(initialContent = null)
        val repository = JsonPreferencesRepository(preferencesStorageDataSource = fakeDataSource)
        val expectedPreferences = AppPreferences(
            activeTheme = AppThemePreference.Dark,
            recentlyAccessedMatches = listOf(
                RecentlyAccessedMatch(
                    teamAName = "Falcons",
                    teamBName = "Knights",
                    matchDate = "2026-05-26",
                    venue = "Oval",
                    accessedAtEpochMillis = 12345L,
                ),
            ),
        )

        val saveResult = repository.savePreferences(expectedPreferences)
        val loadResult = repository.loadPreferences()

        assertEquals(Unit.right(), saveResult)
        assertEquals(expectedPreferences.right(), loadResult)
    }
}

private class FakePreferencesStorageDataSource(
    initialContent: String?,
) : PreferencesStorageDataSource {
    private var content: String? = initialContent

    override fun readPreferencesContent(): Either<PreferencesPersistenceError, String?> {
        return content.right()
    }

    override fun writePreferencesContent(content: String): Either<PreferencesPersistenceError, Unit> {
        this.content = content
        return Unit.right()
    }
}
