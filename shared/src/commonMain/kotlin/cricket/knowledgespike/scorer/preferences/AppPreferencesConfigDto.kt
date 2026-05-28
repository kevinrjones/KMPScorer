package cricket.knowledgespike.scorer.preferences

import kotlinx.serialization.Serializable

@Serializable
internal data class AppPreferencesConfigDto(
    val activeTheme: AppThemePreferenceDto = AppThemePreferenceDto.SYSTEM,
    val desktopWindowPreferences: DesktopWindowPreferencesDto = DesktopWindowPreferencesDto(),
    val recentlyAccessedMatches: List<RecentlyAccessedMatchDto> = emptyList(),
)

@Serializable
internal enum class AppThemePreferenceDto {
    SYSTEM,
    LIGHT,
    DARK,
}

@Serializable
internal data class DesktopWindowPreferencesDto(
    val widthDp: Float = 1200f,
    val heightDp: Float = 800f,
    val xDp: Float? = null,
    val yDp: Float? = null,
)

@Serializable
internal data class RecentlyAccessedMatchDto(
    val teamAName: String,
    val teamBName: String,
    val matchDate: String,
    val venue: String? = null,
    val accessedAtEpochMillis: Long,
)