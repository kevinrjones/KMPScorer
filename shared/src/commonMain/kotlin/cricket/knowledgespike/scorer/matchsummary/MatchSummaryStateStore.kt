package cricket.knowledgespike.scorer.matchsummary

import cricket.knowledgespike.scorer.domain.repository.MatchPersistenceError
import cricket.knowledgespike.scorer.domain.repository.MatchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MatchSummaryStateStore(
    private val matchId: Long,
    private val matchRepository: MatchRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) {
    private val _screenState = MutableStateFlow(MatchSummaryScreenState())
    val screenState: StateFlow<MatchSummaryScreenState> = _screenState.asStateFlow()

    init {
        loadSummary()
    }

    fun onEvent(event: MatchSummaryEvent) {
        when (event) {
            MatchSummaryEvent.RetryRequested -> loadSummary()
        }
    }

    private fun loadSummary() {
        _screenState.update {
            it.copy(summaryState = MatchSummaryState.Loading)
        }

        coroutineScope.launch {
            matchRepository.getMatchSummary(matchId).fold(
                ifLeft = { error ->
                    _screenState.update {
                        it.copy(summaryState = MatchSummaryState.Error(error.toUiMessage()))
                    }
                },
                ifRight = { matchSummary ->
                    _screenState.update {
                        val storedMatch = matchSummary.storedMatch
                        it.copy(
                            summaryState = MatchSummaryState.Content(
                                summary = MatchSummaryUiModel(
                                    id = storedMatch.id,
                                    title = "${storedMatch.matchSetup.teamAName} vs ${storedMatch.matchSetup.teamBName}",
                                    schedule = "${storedMatch.matchSetup.schedule.type.name} ${storedMatch.matchSetup.schedule.amount}",
                                    date = storedMatch.matchSetup.matchDate.toString(),
                                    venue = storedMatch.matchSetup.venue,
                                    scoreEventCount = matchSummary.scoreEvents.size,
                                ),
                            ),
                        )
                    }
                },
            )
        }
    }
}

private fun MatchPersistenceError.toUiMessage(): String {
    return when (this) {
        is MatchPersistenceError.UnableToWriteMatch -> "Unable to save match data."
        is MatchPersistenceError.UnableToReadMatches -> "Unable to load match summary."
        is MatchPersistenceError.UnableToDeleteMatch -> "Unable to delete match data."
        is MatchPersistenceError.MatchNotFound -> "Match #${this.matchId} was not found."
        is MatchPersistenceError.InvalidStoredMatchData -> "Saved match data is invalid."
    }
}
