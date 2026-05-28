package cricket.knowledgespike.scorer.preferences

import cricket.knowledgespike.scorer.domain.preferences.AppPreferences
import cricket.knowledgespike.scorer.domain.preferences.AppThemePreference
import cricket.knowledgespike.scorer.domain.preferences.DesktopWindowPreferences
import cricket.knowledgespike.scorer.domain.preferences.RecentlyAccessedMatch

internal fun AppPreferencesConfigDto.toDomain(): AppPreferences {
    return AppPreferences(
        activeTheme = activeTheme.toDomain(),
        desktopWindowPreferences = desktopWindowPreferences.toDomain(),
        recentlyAccessedMatches = recentlyAccessedMatches.map { it.toDomain() },
    )
}

internal fun AppPreferences.toConfigDto(): AppPreferencesConfigDto {
    return AppPreferencesConfigDto(
        activeTheme = activeTheme.toConfigDto(),
        desktopWindowPreferences = desktopWindowPreferences.toConfigDto(),
        recentlyAccessedMatches = recentlyAccessedMatches.map { it.toConfigDto() },
    )
}

private fun AppThemePreferenceDto.toDomain(): AppThemePreference {
    return when (this) {
        AppThemePreferenceDto.SYSTEM -> AppThemePreference.System
        AppThemePreferenceDto.LIGHT -> AppThemePreference.Light
        AppThemePreferenceDto.DARK -> AppThemePreference.Dark
    }
}

private fun AppThemePreference.toConfigDto(): AppThemePreferenceDto {
    return when (this) {
        AppThemePreference.System -> AppThemePreferenceDto.SYSTEM
        AppThemePreference.Light -> AppThemePreferenceDto.LIGHT
        AppThemePreference.Dark -> AppThemePreferenceDto.DARK
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

private fun DesktopWindowPreferences.toConfigDto(): DesktopWindowPreferencesDto {
    return DesktopWindowPreferencesDto(
        widthDp = widthDp,
        heightDp = heightDp,
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

private fun RecentlyAccessedMatch.toConfigDto(): RecentlyAccessedMatchDto {
    return RecentlyAccessedMatchDto(
        teamAName = teamAName,
        teamBName = teamBName,
        matchDate = matchDate,
        venue = venue,
        accessedAtEpochMillis = accessedAtEpochMillis,
    )
}