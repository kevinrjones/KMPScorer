package cricket.knowledgespike.scorer.matchsummary

data class MatchSummaryScreenState(
    val summaryState: MatchSummaryState = MatchSummaryState.Loading,
)

sealed interface MatchSummaryState {
    data object Loading : MatchSummaryState
    data class Content(val summary: MatchSummaryUiModel) : MatchSummaryState
    data class Error(val message: String) : MatchSummaryState
}

data class MatchSummaryUiModel(
    val id: Long,
    val title: String,
    val schedule: String,
    val date: String,
    val venue: String?,
    val scoreEventCount: Int,
)

sealed interface MatchSummaryEvent {
    data object RetryRequested : MatchSummaryEvent
}
