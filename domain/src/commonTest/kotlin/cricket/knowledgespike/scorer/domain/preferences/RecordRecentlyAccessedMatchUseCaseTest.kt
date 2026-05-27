package cricket.knowledgespike.scorer.domain.preferences

import kotlin.test.Test
import kotlin.test.assertEquals

class RecordRecentlyAccessedMatchUseCaseTest {

    private val useCase = RecordRecentlyAccessedMatchUseCase(maxRecentMatches = 3)

    @Test
    fun `given duplicate match identity when recording recently accessed match then latest duplicate moves to top`() {
        val initialPreferences = AppPreferences(
            recentlyAccessedMatches = listOf(
                recentlyAccessedMatch(teamAName = "Falcons", teamBName = "Kings", matchDate = "2026-05-25", venue = " Main Ground ", accessedAt = 1L),
                recentlyAccessedMatch(teamAName = "Tigers", teamBName = "Lions", matchDate = "2026-05-24", venue = "City Oval", accessedAt = 2L),
            ),
        )

        val updatedPreferences = useCase(
            currentPreferences = initialPreferences,
            recentlyAccessedMatch = recentlyAccessedMatch(
                teamAName = "falcons",
                teamBName = "kings",
                matchDate = "2026-05-25",
                venue = "main ground",
                accessedAt = 3L,
            ),
        )

        assertEquals(2, updatedPreferences.recentlyAccessedMatches.size)
        assertEquals(3L, updatedPreferences.recentlyAccessedMatches[0].accessedAtEpochMillis)
        assertEquals("Tigers", updatedPreferences.recentlyAccessedMatches[1].teamAName)
    }

    @Test
    fun `given more recorded matches than max when recording recently accessed match then history is capped`() {
        var preferences = AppPreferences()

        preferences = useCase(preferences, recentlyAccessedMatch("A1", "B1", accessedAt = 1L))
        preferences = useCase(preferences, recentlyAccessedMatch("A2", "B2", accessedAt = 2L))
        preferences = useCase(preferences, recentlyAccessedMatch("A3", "B3", accessedAt = 3L))
        preferences = useCase(preferences, recentlyAccessedMatch("A4", "B4", accessedAt = 4L))

        assertEquals(listOf("A4", "A3", "A2"), preferences.recentlyAccessedMatches.map { it.teamAName })
    }
}

private fun recentlyAccessedMatch(
    teamAName: String,
    teamBName: String,
    matchDate: String = "2026-05-25",
    venue: String? = null,
    accessedAt: Long,
): RecentlyAccessedMatch {
    return RecentlyAccessedMatch(
        teamAName = teamAName,
        teamBName = teamBName,
        matchDate = matchDate,
        venue = venue,
        accessedAtEpochMillis = accessedAt,
    )
}
