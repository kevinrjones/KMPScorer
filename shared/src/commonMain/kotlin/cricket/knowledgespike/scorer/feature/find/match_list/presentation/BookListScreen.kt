package cricket.knowledgespike.scorer.feature.find.match_list.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cricket.knowledgespike.scorer.feature.find.match_list.domain.Match
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchListScreenRoot(
    viewModel: MatchListVewModel = koinViewModel(),
    onMatchClick: (Match) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MatchListScreen(
        state,
        onAction = { action ->

            when (action) {
                is MatchListAction.OnMatchClick -> onMatchClick(action.match)
                else -> Unit
            }
            viewModel.onAction(action)
        },
        modifier
    )
}

@Composable
fun MatchListScreen(
    state: MatchListState,
    onAction: (MatchListAction) -> Unit,
    modifier: Modifier = Modifier,
) {

}

@Preview
@Composable
fun MatchListScreenPreview() {
    MatchListScreen(state = MatchListState(), onAction = {})
}