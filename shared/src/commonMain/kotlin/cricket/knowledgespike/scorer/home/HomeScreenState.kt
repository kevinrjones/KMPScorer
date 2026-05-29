package cricket.knowledgespike.scorer.home

data class HomeScreenState(
    val listState: HomeMatchListState = HomeMatchListState.Loading,
    val dialogState: HomeMatchDialogState = HomeMatchDialogState.Hidden,
)

sealed interface HomeMatchListState {
    data object Loading : HomeMatchListState
    data object Empty : HomeMatchListState
    data class Content(val matches: List<HomeMatchListItem>) : HomeMatchListState
    data class Error(val message: String) : HomeMatchListState
}

data class HomeMatchListItem(
    val id: Long,
    val title: String,
    val subtitle: String,
)

sealed interface HomeMatchDialogState {
    data object Hidden : HomeMatchDialogState

    data class DeleteConfirmation(
        val matchId: Long,
        val matchTitle: String,
    ) : HomeMatchDialogState
}

sealed interface HomeScreenEvent {
    data object NewMatchRequested : HomeScreenEvent
    data object RefreshRequested : HomeScreenEvent
    data class EditMatchRequested(val matchId: Long) : HomeScreenEvent
    data class OpenSavedMatchRequested(val matchId: Long) : HomeScreenEvent
    data class DeleteMatchRequested(val matchId: Long) : HomeScreenEvent
    data class DeleteMatchConfirmed(val matchId: Long) : HomeScreenEvent
    data object DialogDismissRequested : HomeScreenEvent
}
