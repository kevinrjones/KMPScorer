package cricket.knowledgespike.scorer.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import cricket.knowledgespike.scorer.ui.theme.ScorerSpacing

const val HomeNewButtonTag = "home_new_button"
const val HomeEditMatchButtonTagPrefix = "home_edit_match_button_"
const val HomeScoreMatchButtonTagPrefix = "home_score_match_button_"
const val HomeDeleteMatchButtonTagPrefix = "home_delete_match_button_"

@Composable
fun HomeScreen(
    screenState: HomeScreenState,
    contentPadding: PaddingValues,
    onEvent: (HomeScreenEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(ScorerSpacing.Large),
        verticalArrangement = Arrangement.spacedBy(ScorerSpacing.Medium),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Matches",
                style = MaterialTheme.typography.headlineSmall,
            )
            Button(
                modifier = Modifier.testTag(HomeNewButtonTag),
                onClick = { onEvent(HomeScreenEvent.NewMatchRequested) },
            ) {
                Text("New")
            }
        }

        when (val listState = screenState.listState) {
            HomeMatchListState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator()
                }
            }

            HomeMatchListState.Empty -> {
                Text(
                    text = "No saved matches yet. Start a new match to begin.",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            is HomeMatchListState.Error -> {
                Column(verticalArrangement = Arrangement.spacedBy(ScorerSpacing.Small)) {
                    Text(
                        text = listState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Button(onClick = { onEvent(HomeScreenEvent.RefreshRequested) }) {
                        Text("Retry")
                    }
                }
            }

            is HomeMatchListState.Content -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(ScorerSpacing.Small),
                ) {
                    items(listState.matches, key = { it.id }) { matchItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = ScorerSpacing.Small),
                            horizontalArrangement = Arrangement.spacedBy(ScorerSpacing.Small),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(ScorerSpacing.XSmall),
                            ) {
                                Text(
                                    text = matchItem.title,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Text(
                                    text = matchItem.subtitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(ScorerSpacing.XSmall),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                TextButton(
                                    modifier = Modifier.testTag("$HomeEditMatchButtonTagPrefix${matchItem.id}"),
                                    onClick = { onEvent(HomeScreenEvent.EditMatchRequested(matchItem.id)) },
                                ) {
                                    Text("Edit")
                                }
                                TextButton(
                                    modifier = Modifier.testTag("$HomeScoreMatchButtonTagPrefix${matchItem.id}"),
                                    onClick = { onEvent(HomeScreenEvent.ScoreMatchRequested(matchItem.id)) },
                                ) {
                                    Text("Score")
                                }
                                TextButton(
                                    modifier = Modifier.testTag("$HomeDeleteMatchButtonTagPrefix${matchItem.id}"),
                                    onClick = { onEvent(HomeScreenEvent.DeleteMatchRequested(matchItem.id)) },
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }

        when (val dialogState = screenState.dialogState) {
            HomeMatchDialogState.Hidden -> Unit

            is HomeMatchDialogState.DeleteConfirmation -> {
                AlertDialog(
                    onDismissRequest = { onEvent(HomeScreenEvent.DialogDismissRequested) },
                    title = {
                        Text("Delete match?")
                    },
                    text = {
                        Text("Delete ${dialogState.matchTitle}? This action cannot be undone.")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { onEvent(HomeScreenEvent.DeleteMatchConfirmed(dialogState.matchId)) },
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { onEvent(HomeScreenEvent.DialogDismissRequested) }) {
                            Text("Cancel")
                        }
                    },
                )
            }
        }
    }
}
