package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails
import cricket.knowledgespike.scorer.ui.theme.ScorerTheme
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.delete
import kmpscorer.shared.generated.resources.edit
import kmpscorer.shared.generated.resources.score
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScorecardListItem(
    modifier: Modifier = Modifier,
    scorecard: ScorecardHeaderDetails,
    onEdit: (Int?) -> Unit,
    onDelete: (ScorecardHeaderDetails) -> Unit,
    onScore: (Int?) -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),

        ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OneLineText(
                    text = scorecard.teamName,
                    modifier = Modifier.weight(0.5f),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(text = "vs", modifier = Modifier.width(24.dp))
                OneLineText(
                    text = scorecard.opponentsName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(0.5f)
                        .wrapContentWidth(Alignment.End),
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "at")

                OneLineText(
                    text = scorecard.venue,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(0.5f)
                        .wrapContentWidth(Alignment.End),
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "on")

                OneLineText(
                    text = scorecard.matchDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(0.5f)
                        .wrapContentWidth(Alignment.End),
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    onEdit(scorecard.id)
                }) {
                    Text(text = stringResource(Res.string.edit))
                }

                Button(onClick = {
                    onDelete(scorecard)
                }) {
                    Text(text = stringResource(Res.string.delete))
                }

                Button(onClick = {
                    onScore(scorecard.id)
                }) {
                    Text(text = stringResource(Res.string.score))
                }
            }
        }
    }
}

@Composable
private fun OneLineText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
    color: Color
) {
    Text(
        text = text,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = style,
        color = color
    )

}


@Composable
@Preview
fun PreviewScorecardListItem() {
    ScorerTheme {
        ScorecardListItem(
            modifier = Modifier,
            scorecard = ScorecardHeaderDetails(
                id = 1,
                teamName = "India",
                opponentsName = "Australia",
                venue = "Melbourne",
                matchDate = "23rd, 24th, 25th, 26th Nov 2024"
            ),
            onEdit = {},
            onDelete = {},
            onScore = {}
        )
    }
}