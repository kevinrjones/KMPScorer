package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails
import cricket.knowledgespike.scorer.foundation.compose.ActionIcon
import cricket.knowledgespike.scorer.foundation.compose.Swipeable
import cricket.knowledgespike.scorer.ui.theme.ScorerTheme
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.add_a_scorecard_descrirption
import kmpscorer.shared.generated.resources.deleted_scorecard
import kmpscorer.shared.generated.resources.deleting_scorecard
import kmpscorer.shared.generated.resources.no_scorecards_saved
import kotlinx.coroutines.flow.collectLatest
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
    val snackbarHostState = remember { SnackbarHostState() }
    val deletingMessage = stringResource(Res.string.deleting_scorecard)
    val deletedMessage = stringResource(Res.string.deleted_scorecard)


    LaunchedEffect(true) {
        viewModel.listScorecardEvent.collectLatest { event ->
            when (event) {
                is ListScorecardEvent.Deleted -> {
                    // todo: should I delete?
                    snackbarHostState.showSnackbar(message = deletingMessage)
                }

                ListScorecardEvent.ErrorDeletingScorecard -> {
                    snackbarHostState.showSnackbar(message = deletedMessage)
                }
            }
        }
    }

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
            onUiEvent = viewModel::onEvent,
            updateExpandedState = { card, state ->
                viewModel.updateExpandedState(card, state)
            },
            isExpandedScreen = isExpandedScreen,
            modifier = modifier
        )
    }
}

@Composable
fun ScorecardListScreen(
    innerPadding: PaddingValues,
    state: ScorecardListState,
    onAction: (ScorecardListAction) -> Unit,
    onUiEvent: (ListScorecardUiEvent) -> Unit,
    updateExpandedState: (ScorecardDetailsView, Boolean) -> Unit,
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
        if (state.scorecards.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    fontSize = 32.sp,
                    text = stringResource(Res.string.no_scorecards_saved)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(sizeFraction),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.scorecards) { match ->
                    Swipeable(
                        isRevealed = match.isExpanded,
                        onExpanded = {
                            updateExpandedState(match, true)
                        },
                        onCollapsed = {
                            updateExpandedState(match, false)
                        },
                        actions = {
                            ActionIcon(
                                onClick = {
                                    println("Delete was clicked")
                                    updateExpandedState(match, false)
//                                    contacts[index] = contact.copy(isOptionsRevealed = false)
//                                    Toast.makeText(
//                                        context,
//                                        "Contact ${contact.id} was sent an email.",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
                                },
                                backgroundColor = Color.Red,
                                icon = Icons.Default.Delete,
                                modifier = Modifier.fillMaxHeight()
                            )
                        },
                    ) {
                        ScorecardListItem(
                            scorecard = match.scorecardHeaderDetails,
                            onEdit = { id -> onAction(ScorecardListAction.onAddOrEditScorecard(id)) },
                            onDelete = { scorecard ->
                                onUiEvent(ListScorecardUiEvent.TryDelete(scorecard))
                            },
                            onScore = { id -> onAction(ScorecardListAction.onScore(id)) }
                        )
                    }
                }
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
            state = ScorecardListState(scorecards = dummyScorecardIdentifyingDetails.map {
                ScorecardDetailsView(
                    it,
                    false
                )
            }),
            onAction = {},
            onUiEvent = {},
            updateExpandedState = {_, _ -> },
            isExpandedScreen = false,
        )
    }
}