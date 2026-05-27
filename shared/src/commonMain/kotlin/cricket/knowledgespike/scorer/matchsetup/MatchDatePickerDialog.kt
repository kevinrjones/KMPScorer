package cricket.knowledgespike.scorer.matchsetup

import androidx.compose.runtime.Composable
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.todayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
internal expect fun PlatformMatchDatePickerDialog(
    initialDateIso: String?,
    onDismissRequest: () -> Unit,
    onDateConfirmed: (String) -> Unit,
)

internal fun String.toCanonicalMatchDateOrNull(): String? {
    val trimmedValue = trim()
    if (trimmedValue.isBlank()) {
        return null
    }

    return runCatching { LocalDate.parse(trimmedValue).toString() }.getOrNull()
}

internal fun resolveInitialMatchDate(
    initialDateIso: String?,
    todayDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
): LocalDate {
    return initialDateIso
        ?.toCanonicalMatchDateOrNull()
        ?.let(LocalDate::parse)
        ?: todayDate
}

internal fun LocalDate.toUtcEpochMillis(): Long {
    return atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
}

internal fun utcEpochMillisToLocalDate(value: Long): LocalDate {
    return Instant.fromEpochMilliseconds(value).toLocalDateTime(TimeZone.UTC).date
}