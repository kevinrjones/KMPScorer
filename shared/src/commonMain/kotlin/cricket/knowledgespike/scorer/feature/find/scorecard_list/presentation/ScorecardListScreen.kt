package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails
import cricket.knowledgespike.scorer.ui.theme.ScorerTheme
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.add_a_scorecard_descrirption
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchListScreenRoot(
    viewModel: ScorecardListVewModel = koinViewModel(),
    onAddOrEditScorecard: (Int?) -> Unit,
    onScore: (Int?) -> Unit,
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onAddOrEditScorecard(null)
            }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(Res.string.add_a_scorecard_descrirption)
                )
            }
        }
    ) { innerPadding ->

        ScorecardListScreen(
            innerPadding = innerPadding,
            state,
            onAction = { action ->
                when (action) {
                    is ScorecardListAction.onAddOrEditScorecard -> onAddOrEditScorecard(action.id)
                    is ScorecardListAction.onScore -> onScore(action.id)
                    else -> Unit
                }
                viewModel.onAction(action)
            },
            isExpandedScreen = isExpandedScreen,
            modifier
        )
    }
}

@Composable
fun ScorecardListScreen(
    innerPadding: PaddingValues,
    state: ScorecardListState,
    onAction: (ScorecardListAction) -> Unit,
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier,
) {
    val sizeFraction = if (!isExpandedScreen) {
        1f
    } else {
        0.5f
    }

    Column(
        modifier = modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(sizeFraction),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state.scorecards) { match ->
                ScorecardListItem(
                    scorecard = match,
                    onEdit = { id -> onAction(ScorecardListAction.onAddOrEditScorecard(id)) },
                    onScore = { id -> onAction(ScorecardListAction.onScore(id)) }
                )
            }
        }
    }
}


private val dummyScorecardIdentifyingDetails = (1..10).map {
    ScorecardHeaderDetails(
        id = 1,
        teamName = "Team $it",
        opponentsName = "Team ${it + 1}",
        venue = "Melbourne",
        matchDate = "23rd, 24th, 25th, 26th Nov 2024"
    )
}

@Preview
@Composable
fun ScorecardListScreenPreview() {
    ScorerTheme {
        ScorecardListScreen(
            innerPadding = PaddingValues(10.dp),
            state = ScorecardListState(scorecards = dummyScorecardIdentifyingDetails),
            onAction = {},
            isExpandedScreen = false,
        )
    }
}