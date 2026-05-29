package cricket.knowledgespike.scorer.home

import cricket.knowledgespike.scorer.domain.repository.MatchPersistenceError
import cricket.knowledgespike.scorer.domain.repository.MatchRepository
import cricket.knowledgespike.scorer.navigation.ScorerRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeStateStore(
    private val matchRepository: MatchRepository,
    private val onRouteRequested: (ScorerRoute) -> Unit,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) {
    private val _screenState = MutableStateFlow(HomeScreenState())
    val screenState: StateFlow<HomeScreenState> = _screenState.asStateFlow()

    init {
        loadMatches()
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.NewMatchRequested -> onRouteRequested(ScorerRoute.MatchSetupRoute)
            HomeScreenEvent.RefreshRequested -> loadMatches()
            HomeScreenEvent.DialogDismissRequested -> dismissDialog()
            is HomeScreenEvent.EditMatchRequested -> editMatch(matchId = event.matchId)
            is HomeScreenEvent.ScoreMatchRequested -> scoreMatch(matchId = event.matchId)
            is HomeScreenEvent.DeleteMatchRequested -> showDeleteConfirmation(matchId = event.matchId)
            is HomeScreenEvent.DeleteMatchConfirmed -> deleteMatch(matchId = event.matchId)
        }
    }

    private fun dismissDialog() {
        _screenState.update {
            it.copy(dialogState = HomeMatchDialogState.Hidden)
        }
    }

    private fun editMatch(matchId: Long) {
        dismissDialog()
        onRouteRequested(ScorerRoute.MatchSetupRoute)
    }

    private fun scoreMatch(matchId: Long) {
        dismissDialog()
        onRouteRequested(ScorerRoute.MatchSummaryRoute(matchId = matchId))
    }

    private fun showDeleteConfirmation(matchId: Long) {
        val matchTitle = resolveMatchTitle(matchId = matchId)

        _screenState.update {
            it.copy(
                dialogState = HomeMatchDialogState.DeleteConfirmation(
                    matchId = matchId,
                    matchTitle = matchTitle,
                ),
            )
        }
    }

    private fun deleteMatch(matchId: Long) {
        dismissDialog()

        coroutineScope.launch {
            matchRepository.deleteMatch(matchId).fold(
                ifLeft = { error ->
                    _screenState.update {
                        it.copy(listState = HomeMatchListState.Error(error.toUiMessage()))
                    }
                },
                ifRight = {
                    loadMatches()
                },
            )
        }
    }

    private fun resolveMatchTitle(matchId: Long): String {
        return when (val listState = _screenState.value.listState) {
            is HomeMatchListState.Content -> listState.matches
                .firstOrNull { it.id == matchId }
                ?.title
                ?: "Match #$matchId"

            HomeMatchListState.Empty,
            is HomeMatchListState.Error,
            HomeMatchListState.Loading,
            -> "Match #$matchId"
        }
    }

    private fun loadMatches() {
        _screenState.update {
            it.copy(
                listState = HomeMatchListState.Loading,
                dialogState = HomeMatchDialogState.Hidden,
            )
        }

        coroutineScope.launch {
            matchRepository.listStoredMatches().fold(
                ifLeft = { error ->
                    _screenState.update {
                        it.copy(listState = HomeMatchListState.Error(error.toUiMessage()))
                    }
                },
                ifRight = { matches ->
                    _screenState.update {
                        if (matches.isEmpty()) {
                            it.copy(listState = HomeMatchListState.Empty)
                        } else {
                            it.copy(listState = HomeMatchListState.Content(matches.map { storedMatch ->
                                HomeMatchListItem(
                                    id = storedMatch.id,
                                    title = "${storedMatch.matchSetup.teamAName} vs ${storedMatch.matchSetup.teamBName}",
                                    subtitle = "${storedMatch.matchSetup.schedule.type.name} ${storedMatch.matchSetup.schedule.amount} • ${storedMatch.matchSetup.matchDate}",
                                )
                            }))
                        }
                    }
                },
            )
        }
    }
}

private fun MatchPersistenceError.toUiMessage(): String {
    return when (this) {
        is MatchPersistenceError.UnableToWriteMatch -> "Unable to save match data."
        is MatchPersistenceError.UnableToReadMatches -> "Unable to load saved matches."
        is MatchPersistenceError.UnableToDeleteMatch -> "Unable to delete the saved match."
        is MatchPersistenceError.MatchNotFound -> "Saved match not found."
        is MatchPersistenceError.InvalidStoredMatchData -> "Saved match data is invalid."
    }
}
