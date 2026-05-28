package cricket.knowledgespike.scorer.domain.matchsetup

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.datetime.LocalDate

data class MatchSetupDraft(
    val teamAName: String,
    val teamBName: String,
    val scheduleType: MatchScheduleType,
    val scheduleAmount: String,
    val tossWinner: TossWinner?,
    val tossDecision: TossDecision?,
    val matchDate: String,
    val venue: String,
    val umpireOne: String,
    val umpireTwo: String,
    val weather: String,
)

data class MatchSetup(
    val teamAName: String,
    val teamBName: String,
    val schedule: MatchSchedule,
    val tossWinner: TossWinner,
    val tossDecision: TossDecision,
    val matchDate: LocalDate,
    val venue: String?,
    val umpireOne: String?,
    val umpireTwo: String?,
    val weather: String?,
)

enum class TossWinner {
    TeamA,
    TeamB,
}

enum class TossDecision {
    Bat,
    Bowl,
}

enum class MatchScheduleType {
    Overs,
    Balls,
    Days,
}

data class MatchSchedule(
    val type: MatchScheduleType,
    val amount: Int,
)

sealed interface MatchSetupValidationError {
    data object MissingTeamAName : MatchSetupValidationError
    data object MissingTeamBName : MatchSetupValidationError
    data object TeamNamesMustDiffer : MatchSetupValidationError
    data object InvalidScheduleAmount : MatchSetupValidationError
    data object MissingTossWinner : MatchSetupValidationError
    data object MissingTossDecision : MatchSetupValidationError
    data object InvalidMatchDate : MatchSetupValidationError
}

class CreateMatchSetupUseCase {

    operator fun invoke(draft: MatchSetupDraft): Either<MatchSetupValidationError, MatchSetup> {
        val normalizedDraft = draft.toNormalizedDraft(::parseMatchDate)
        val validationError = normalizedDraft.firstValidationError()
        if (validationError != null) {
            return validationError.left()
        }

        return normalizedDraft.toMatchSetup().right()
    }

    private fun parseMatchDate(rawValue: String): LocalDate? {
        return runCatching { LocalDate.parse(rawValue.trim()) }.getOrNull()
    }
}

private data class NormalizedMatchSetupDraft(
    val teamAName: String,
    val teamBName: String,
    val scheduleType: MatchScheduleType,
    val scheduleAmount: Int?,
    val tossWinner: TossWinner?,
    val tossDecision: TossDecision?,
    val matchDate: LocalDate?,
    val venue: String?,
    val umpireOne: String?,
    val umpireTwo: String?,
    val weather: String?,
)

private data class MatchSetupValidationRule(
    val error: MatchSetupValidationError,
    val isInvalid: (NormalizedMatchSetupDraft) -> Boolean,
)

private val matchSetupValidationRules = listOf(
    MatchSetupValidationRule(
        error = MatchSetupValidationError.MissingTeamAName,
        isInvalid = { it.teamAName.isBlank() },
    ),
    MatchSetupValidationRule(
        error = MatchSetupValidationError.MissingTeamBName,
        isInvalid = { it.teamBName.isBlank() },
    ),
    MatchSetupValidationRule(
        error = MatchSetupValidationError.TeamNamesMustDiffer,
        isInvalid = { it.teamAName.equals(it.teamBName, ignoreCase = true) },
    ),
    MatchSetupValidationRule(
        error = MatchSetupValidationError.InvalidScheduleAmount,
        isInvalid = { scheduleDraft ->
            val scheduleAmount = scheduleDraft.scheduleAmount
            scheduleAmount == null || scheduleAmount <= 0 || scheduleAmount > 999
        },
    ),
    MatchSetupValidationRule(
        error = MatchSetupValidationError.MissingTossWinner,
        isInvalid = { it.tossWinner == null },
    ),
    MatchSetupValidationRule(
        error = MatchSetupValidationError.MissingTossDecision,
        isInvalid = { it.tossDecision == null },
    ),
    MatchSetupValidationRule(
        error = MatchSetupValidationError.InvalidMatchDate,
        isInvalid = { it.matchDate == null },
    ),
)

private fun MatchSetupDraft.toNormalizedDraft(parseDate: (String) -> LocalDate?): NormalizedMatchSetupDraft {
    return NormalizedMatchSetupDraft(
        teamAName = teamAName.trim(),
        teamBName = teamBName.trim(),
        scheduleType = scheduleType,
        scheduleAmount = scheduleAmount.trim().toIntOrNull(),
        tossWinner = tossWinner,
        tossDecision = tossDecision,
        matchDate = parseDate(matchDate),
        venue = venue.normalizedOrNull(),
        umpireOne = umpireOne.normalizedOrNull(),
        umpireTwo = umpireTwo.normalizedOrNull(),
        weather = weather.normalizedOrNull(),
    )
}

private fun NormalizedMatchSetupDraft.firstValidationError(): MatchSetupValidationError? {
    return matchSetupValidationRules
        .firstOrNull { rule -> rule.isInvalid(this) }
        ?.error
}

private fun NormalizedMatchSetupDraft.toMatchSetup(): MatchSetup {
    return MatchSetup(
        teamAName = teamAName,
        teamBName = teamBName,
        schedule = MatchSchedule(
            type = scheduleType,
            amount = requireNotNull(scheduleAmount) { "Schedule amount must be validated before creation" },
        ),
        tossWinner = requireNotNull(tossWinner) { "Toss winner must be validated before creation" },
        tossDecision = requireNotNull(tossDecision) { "Toss decision must be validated before creation" },
        matchDate = requireNotNull(matchDate) { "Match date must be validated before creation" },
        venue = venue,
        umpireOne = umpireOne,
        umpireTwo = umpireTwo,
        weather = weather,
    )
}

private fun String.normalizedOrNull(): String? =
    trim().ifBlank { null }
