package cricket.knowledgespike.scorer.preferences

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import cricket.knowledgespike.scorer.domain.preferences.AppPreferences
import cricket.knowledgespike.scorer.domain.preferences.AppThemePreference
import cricket.knowledgespike.scorer.domain.preferences.DesktopWindowPreferences
import cricket.knowledgespike.scorer.domain.preferences.PreferencesPersistenceError
import cricket.knowledgespike.scorer.domain.preferences.PreferencesRepository
import cricket.knowledgespike.scorer.domain.preferences.RecentlyAccessedMatch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class JsonPreferencesRepository(
    private val preferencesStorageDataSource: PreferencesStorageDataSource,
    private val json: Json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        prettyPrint = true
    },
) : PreferencesRepository {

    override fun loadPreferences(): Either<PreferencesPersistenceError, AppPreferences> {
        return preferencesStorageDataSource.readPreferencesContent().flatMap { rawContent ->
            if (rawContent.isNullOrBlank()) {
                AppPreferences().right()
            } else {
                runCatching {
                    json.decodeFromString<AppPreferencesConfigDto>(rawContent).toDomain()
                }.fold(
                    onSuccess = { it.right() },
                    onFailure = { PreferencesPersistenceError.UnableToReadPreferences(it.message).left() },
                )
            }
        }
    }

    override fun savePreferences(preferences: AppPreferences): Either<PreferencesPersistenceError, Unit> {
        return runCatching {
            json.encodeToString(preferences.toConfigDto())
        }.fold(
            onSuccess = preferencesStorageDataSource::writePreferencesContent,
            onFailure = { PreferencesPersistenceError.UnableToWritePreferences(it.message).left() },
        )
    }
}

object InMemoryPreferencesRepository : PreferencesRepository {
    private var currentPreferences: AppPreferences = AppPreferences()

    override fun loadPreferences(): Either<PreferencesPersistenceError, AppPreferences> {
        return currentPreferences.right()
    }

    override fun savePreferences(preferences: AppPreferences): Either<PreferencesPersistenceError, Unit> {
        currentPreferences = preferences
        return Unit.right()
    }
}

@Serializable
private data class AppPreferencesConfigDto(
    val activeTheme: AppThemePreferenceDto = AppThemePreferenceDto.SYSTEM,
    val desktopWindowPreferences: DesktopWindowPreferencesDto = DesktopWindowPreferencesDto(),
    val recentlyAccessedMatches: List<RecentlyAccessedMatchDto> = emptyList(),
)

@Serializable
private enum class AppThemePreferenceDto {
    SYSTEM,
    LIGHT,
    DARK,
}

@Serializable
private data class DesktopWindowPreferencesDto(
    val widthDp: Float = 1200f,
    val heightDp: Float = 800f,
    val xDp: Float? = null,
    val yDp: Float? = null,
)

@Serializable
private data class RecentlyAccessedMatchDto(
    val teamAName: String,
    val teamBName: String,
    val matchDate: String,
    val venue: String? = null,
    val accessedAtEpochMillis: Long,
)

private fun AppPreferencesConfigDto.toDomain(): AppPreferences {
    return AppPreferences(
        activeTheme = activeTheme.toDomain(),
        desktopWindowPreferences = desktopWindowPreferences.toDomain(),
        recentlyAccessedMatches = recentlyAccessedMatches.map { it.toDomain() },
    )
}

private fun AppThemePreferenceDto.toDomain(): AppThemePreference {
    return when (this) {
        AppThemePreferenceDto.SYSTEM -> AppThemePreference.System
        AppThemePreferenceDto.LIGHT -> AppThemePreference.Light
        AppThemePreferenceDto.DARK -> AppThemePreference.Dark
    }
}

private fun DesktopWindowPreferencesDto.toDomain(): DesktopWindowPreferences {
    return DesktopWindowPreferences(
        widthDp = widthDp.coerceAtLeast(1f),
        heightDp = heightDp.coerceAtLeast(1f),
        xDp = xDp,
        yDp = yDp,
    )
}

private fun RecentlyAccessedMatchDto.toDomain(): RecentlyAccessedMatch {
    return RecentlyAccessedMatch(
        teamAName = teamAName,
        teamBName = teamBName,
        matchDate = matchDate,
        venue = venue,
        accessedAtEpochMillis = accessedAtEpochMillis,
    )
}

private fun AppPreferences.toConfigDto(): AppPreferencesConfigDto {
    return AppPreferencesConfigDto(
        activeTheme = activeTheme.toConfigDto(),
        desktopWindowPreferences = desktopWindowPreferences.toConfigDto(),
        recentlyAccessedMatches = recentlyAccessedMatches.map { it.toConfigDto() },
    )
}

private fun AppThemePreference.toConfigDto(): AppThemePreferenceDto {
    return when (this) {
        AppThemePreference.System -> AppThemePreferenceDto.SYSTEM
        AppThemePreference.Light -> AppThemePreferenceDto.LIGHT
        AppThemePreference.Dark -> AppThemePreferenceDto.DARK
    }
}

private fun DesktopWindowPreferences.toConfigDto(): DesktopWindowPreferencesDto {
    return DesktopWindowPreferencesDto(
        widthDp = widthDp,
        heightDp = heightDp,
        xDp = xDp,
        yDp = yDp,
    )
}

private fun RecentlyAccessedMatch.toConfigDto(): RecentlyAccessedMatchDto {
    return RecentlyAccessedMatchDto(
        teamAName = teamAName,
        teamBName = teamBName,
        matchDate = matchDate,
        venue = venue,
        accessedAtEpochMillis = accessedAtEpochMillis,
    )
}
