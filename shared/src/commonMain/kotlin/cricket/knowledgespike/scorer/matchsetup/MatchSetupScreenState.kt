package cricket.knowledgespike.scorer.matchsetup

import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.matchsetup.MatchScheduleType
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner

data class MatchSetupFormState(
    val teamAName: String = "",
    val teamBName: String = "",
    val scheduleType: MatchScheduleType = MatchScheduleType.Overs,
    val scheduleAmount: String = "",
    val tossWinner: TossWinner? = null,
    val tossDecision: TossDecision? = null,
    val matchDate: String = "",
    val venue: String = "",
    val umpireOne: String = "",
    val umpireTwo: String = "",
    val weather: String = "",
)

data class TossWinnerOptionLabels(
    val teamA: String,
    val teamB: String,
)

data class MatchSetupScreenState(
    val formState: MatchSetupFormState = MatchSetupFormState(),
    val canStartMatch: Boolean = false,
    val startMatchResult: MatchSetupStartMatchResult = MatchSetupStartMatchResult.Idle,
)

sealed interface MatchSetupStartMatchResult {
    data object Idle : MatchSetupStartMatchResult
    data object Saving : MatchSetupStartMatchResult
    data class ValidationError(val message: String) : MatchSetupStartMatchResult
    data class Ready(val matchSetup: MatchSetup) : MatchSetupStartMatchResult
    data class Saved(val matchId: Long) : MatchSetupStartMatchResult
    data class PersistenceError(val message: String) : MatchSetupStartMatchResult
}

fun MatchSetupFormState.toTossWinnerOptionLabels(): TossWinnerOptionLabels {
    return TossWinnerOptionLabels(
        teamA = teamAName.toTossWinnerLabelOrFallback("Team A"),
        teamB = teamBName.toTossWinnerLabelOrFallback("Team B"),
    )
}

private fun String.toTossWinnerLabelOrFallback(teamSlotLabel: String): String {
    return trim().ifBlank { "$teamSlotLabel (name pending)" }
}

sealed interface MatchSetupScreenEvent {
    data class TeamANameChanged(val value: String) : MatchSetupScreenEvent
    data class TeamBNameChanged(val value: String) : MatchSetupScreenEvent
    data class ScheduleTypeChanged(val value: MatchScheduleType) : MatchSetupScreenEvent
    data class ScheduleAmountChanged(val value: String) : MatchSetupScreenEvent
    data class TossWinnerChanged(val value: TossWinner) : MatchSetupScreenEvent
    data class TossDecisionChanged(val value: TossDecision) : MatchSetupScreenEvent
    data class MatchDateChanged(val value: String) : MatchSetupScreenEvent
    data class VenueChanged(val value: String) : MatchSetupScreenEvent
    data class UmpireOneChanged(val value: String) : MatchSetupScreenEvent
    data class UmpireTwoChanged(val value: String) : MatchSetupScreenEvent
    data class WeatherChanged(val value: String) : MatchSetupScreenEvent
    data object StartMatchRequested : MatchSetupScreenEvent
    data object ResetRequested : MatchSetupScreenEvent
}
