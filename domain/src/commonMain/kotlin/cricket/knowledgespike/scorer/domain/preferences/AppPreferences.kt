package cricket.knowledgespike.scorer.domain.preferences

import arrow.core.Either

const val DefaultRecentMatchesLimit = 10

data class AppPreferences(
    val activeTheme: AppThemePreference = AppThemePreference.System,
    val desktopWindowPreferences: DesktopWindowPreferences = DesktopWindowPreferences(),
    val recentlyAccessedMatches: List<RecentlyAccessedMatch> = emptyList(),
)

enum class AppThemePreference {
    System,
    Light,
    Dark,
}

data class DesktopWindowPreferences(
    val widthDp: Float = 1200f,
    val heightDp: Float = 800f,
    val xDp: Float? = null,
    val yDp: Float? = null,
)

data class RecentlyAccessedMatch(
    val teamAName: String,
    val teamBName: String,
    val matchDate: String,
    val venue: String?,
    val accessedAtEpochMillis: Long,
)

sealed interface PreferencesPersistenceError {
    data class UnableToReadPreferences(val reason: String?) : PreferencesPersistenceError
    data class UnableToWritePreferences(val reason: String?) : PreferencesPersistenceError
}

interface PreferencesRepository {
    fun loadPreferences(): Either<PreferencesPersistenceError, AppPreferences>

    fun savePreferences(preferences: AppPreferences): Either<PreferencesPersistenceError, Unit>
}

class RecordRecentlyAccessedMatchUseCase(
    private val maxRecentMatches: Int = DefaultRecentMatchesLimit,
) {
    operator fun invoke(
        currentPreferences: AppPreferences,
        recentlyAccessedMatch: RecentlyAccessedMatch,
    ): AppPreferences {
        val deduplicatedHistory = currentPreferences.recentlyAccessedMatches.filterNot {
            it.isSameMatchAs(recentlyAccessedMatch)
        }

        return currentPreferences.copy(
            recentlyAccessedMatches = (listOf(recentlyAccessedMatch) + deduplicatedHistory)
                .take(maxRecentMatches),
        )
    }
}

private fun RecentlyAccessedMatch.isSameMatchAs(other: RecentlyAccessedMatch): Boolean {
    return teamAName.equals(other.teamAName, ignoreCase = true) &&
        teamBName.equals(other.teamBName, ignoreCase = true) &&
        matchDate == other.matchDate &&
        venue.normalizedVenue() == other.venue.normalizedVenue()
}

private fun String?.normalizedVenue(): String {
    return this.orEmpty().trim().lowercase()
}