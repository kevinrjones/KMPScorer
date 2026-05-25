package cricket.knowledgespike.scorer.domain.matchsetup

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.datetime.LocalDate

data class MatchSetupDraft(
    val teamAName: String,
    val teamBName: String,
    val scheduledOvers: String,
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
    val scheduledOvers: Int,
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

sealed interface MatchSetupValidationError {
    data object MissingTeamAName : MatchSetupValidationError
    data object MissingTeamBName : MatchSetupValidationError
    data object TeamNamesMustDiffer : MatchSetupValidationError
    data object InvalidScheduledOvers : MatchSetupValidationError
    data object MissingTossWinner : MatchSetupValidationError
    data object MissingTossDecision : MatchSetupValidationError
    data object InvalidMatchDate : MatchSetupValidationError
}

class CreateMatchSetupUseCase {

    operator fun invoke(draft: MatchSetupDraft): Either<MatchSetupValidationError, MatchSetup> {
        val trimmedTeamAName = draft.teamAName.trim()
        if (trimmedTeamAName.isBlank()) {
            return MatchSetupValidationError.MissingTeamAName.left()
        }

        val trimmedTeamBName = draft.teamBName.trim()
        if (trimmedTeamBName.isBlank()) {
            return MatchSetupValidationError.MissingTeamBName.left()
        }

        if (trimmedTeamAName.equals(trimmedTeamBName, ignoreCase = true)) {
            return MatchSetupValidationError.TeamNamesMustDiffer.left()
        }

        val parsedOvers = draft.scheduledOvers.trim().toIntOrNull()
        if (parsedOvers == null || parsedOvers <= 0) {
            return MatchSetupValidationError.InvalidScheduledOvers.left()
        }

        val tossWinner = draft.tossWinner ?: return MatchSetupValidationError.MissingTossWinner.left()
        val tossDecision = draft.tossDecision ?: return MatchSetupValidationError.MissingTossDecision.left()

        val parsedMatchDate = parseMatchDate(draft.matchDate) ?: return MatchSetupValidationError.InvalidMatchDate.left()

        return MatchSetup(
            teamAName = trimmedTeamAName,
            teamBName = trimmedTeamBName,
            scheduledOvers = parsedOvers,
            tossWinner = tossWinner,
            tossDecision = tossDecision,
            matchDate = parsedMatchDate,
            venue = draft.venue.normalizedOrNull(),
            umpireOne = draft.umpireOne.normalizedOrNull(),
            umpireTwo = draft.umpireTwo.normalizedOrNull(),
            weather = draft.weather.normalizedOrNull(),
        ).right()
    }

    private fun parseMatchDate(rawValue: String): LocalDate? {
        return runCatching { LocalDate.parse(rawValue.trim()) }.getOrNull()
    }
}

private fun String.normalizedOrNull(): String? =
    trim().ifBlank { null }
