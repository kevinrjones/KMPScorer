package cricket.knowledgespike.scorer.matchsummary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cricket.knowledgespike.scorer.ui.theme.ScorerSpacing

@Composable
fun MatchSummaryScreen(
    screenState: MatchSummaryScreenState,
    contentPadding: PaddingValues,
    onEvent: (MatchSummaryEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(ScorerSpacing.Large),
        verticalArrangement = Arrangement.spacedBy(ScorerSpacing.Medium),
    ) {
        when (val summaryState = screenState.summaryState) {
            MatchSummaryState.Loading -> {
                CircularProgressIndicator()
            }

            is MatchSummaryState.Error -> {
                Text(
                    text = summaryState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Button(onClick = { onEvent(MatchSummaryEvent.RetryRequested) }) {
                    Text("Retry")
                }
            }

            is MatchSummaryState.Content -> {
                Text(
                    text = "Match Summary",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = summaryState.summary.title,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = "Schedule: ${summaryState.summary.schedule}",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "Date: ${summaryState.summary.date}",
                    style = MaterialTheme.typography.bodyLarge,
                )
                summaryState.summary.venue?.let { venue ->
                    Text(
                        text = "Venue: $venue",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Text(
                    text = "Score events: ${summaryState.summary.scoreEventCount}",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
