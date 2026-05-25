package cricket.knowledgespike.scorer.matchsetup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner

@Composable
fun MatchSetupScreen(
    widthSizeClass: WindowWidthSizeClass,
    screenState: MatchSetupScreenState,
    onEvent: (MatchSetupScreenEvent) -> Unit,
) {
    Surface {
        when (widthSizeClass) {
            WindowWidthSizeClass.Expanded -> {
                ExpandedMatchSetupLayout(
                    screenState = screenState,
                    onEvent = onEvent,
                )
            }

            else -> {
                CompactMatchSetupLayout(
                    screenState = screenState,
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
private fun CompactMatchSetupLayout(
    screenState: MatchSetupScreenState,
    onEvent: (MatchSetupScreenEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        RequiredMatchDetailsSection(
            formState = screenState.formState,
            onEvent = onEvent,
        )
        OptionalMatchDetailsSection(
            formState = screenState.formState,
            onEvent = onEvent,
        )
        MatchSetupActionSection(
            screenState = screenState,
            onEvent = onEvent,
        )
    }
}

@Composable
private fun ExpandedMatchSetupLayout(
    screenState: MatchSetupScreenState,
    onEvent: (MatchSetupScreenEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.5f),
        ) {
            RequiredMatchDetailsSection(
                formState = screenState.formState,
                onEvent = onEvent,
            )
            MatchSetupActionSection(
                screenState = screenState,
                onEvent = onEvent,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            OptionalMatchDetailsSection(
                formState = screenState.formState,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun RequiredMatchDetailsSection(
    formState: MatchSetupFormState,
    onEvent: (MatchSetupScreenEvent) -> Unit,
) {
    Text(
        text = "Match Setup",
        style = MaterialTheme.typography.headlineMedium,
    )
    Text(
        text = "Required",
        style = MaterialTheme.typography.titleMedium,
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = formState.teamAName,
        onValueChange = { onEvent(MatchSetupScreenEvent.TeamANameChanged(it)) },
        label = { Text("Team A") },
        singleLine = true,
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = formState.teamBName,
        onValueChange = { onEvent(MatchSetupScreenEvent.TeamBNameChanged(it)) },
        label = { Text("Team B") },
        singleLine = true,
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = formState.scheduledOvers,
        onValueChange = { onEvent(MatchSetupScreenEvent.ScheduledOversChanged(it)) },
        label = { Text("Scheduled overs") },
        singleLine = true,
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = formState.matchDate,
        onValueChange = { onEvent(MatchSetupScreenEvent.MatchDateChanged(it)) },
        label = { Text("Match date (YYYY-MM-DD)") },
        singleLine = true,
    )

    TossWinnerSection(
        selectedValue = formState.tossWinner,
        onSelect = { onEvent(MatchSetupScreenEvent.TossWinnerChanged(it)) },
    )
    TossDecisionSection(
        selectedValue = formState.tossDecision,
        onSelect = { onEvent(MatchSetupScreenEvent.TossDecisionChanged(it)) },
    )
}

@Composable
private fun OptionalMatchDetailsSection(
    formState: MatchSetupFormState,
    onEvent: (MatchSetupScreenEvent) -> Unit,
) {
    Text(
        text = "Optional",
        style = MaterialTheme.typography.titleMedium,
    )
    Text(
        text = "Rosters are optional in this first increment.",
        style = MaterialTheme.typography.bodyMedium,
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = formState.venue,
        onValueChange = { onEvent(MatchSetupScreenEvent.VenueChanged(it)) },
        label = { Text("Venue") },
        singleLine = true,
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = formState.umpireOne,
        onValueChange = { onEvent(MatchSetupScreenEvent.UmpireOneChanged(it)) },
        label = { Text("Umpire one") },
        singleLine = true,
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = formState.umpireTwo,
        onValueChange = { onEvent(MatchSetupScreenEvent.UmpireTwoChanged(it)) },
        label = { Text("Umpire two") },
        singleLine = true,
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = formState.weather,
        onValueChange = { onEvent(MatchSetupScreenEvent.WeatherChanged(it)) },
        label = { Text("Weather") },
        singleLine = true,
    )
}

@Composable
private fun TossWinnerSection(
    selectedValue: TossWinner?,
    onSelect: (TossWinner) -> Unit,
) {
    Text(
        text = "Toss winner",
        style = MaterialTheme.typography.titleMedium,
    )
    RadioOption(
        text = "Team A",
        selected = selectedValue == TossWinner.TeamA,
        onClick = { onSelect(TossWinner.TeamA) },
    )
    RadioOption(
        text = "Team B",
        selected = selectedValue == TossWinner.TeamB,
        onClick = { onSelect(TossWinner.TeamB) },
    )
}

@Composable
private fun TossDecisionSection(
    selectedValue: TossDecision?,
    onSelect: (TossDecision) -> Unit,
) {
    Text(
        text = "Toss decision",
        style = MaterialTheme.typography.titleMedium,
    )
    RadioOption(
        text = "Bat",
        selected = selectedValue == TossDecision.Bat,
        onClick = { onSelect(TossDecision.Bat) },
    )
    RadioOption(
        text = "Bowl",
        selected = selectedValue == TossDecision.Bowl,
        onClick = { onSelect(TossDecision.Bowl) },
    )
}

@Composable
private fun RadioOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun MatchSetupActionSection(
    screenState: MatchSetupScreenState,
    onEvent: (MatchSetupScreenEvent) -> Unit,
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        enabled = screenState.canStartMatch,
        onClick = { onEvent(MatchSetupScreenEvent.StartMatchRequested) },
    ) {
        Text("Start Match")
    }

    when (val startMatchResult = screenState.startMatchResult) {
        MatchSetupStartMatchResult.Idle -> Unit

        is MatchSetupStartMatchResult.ValidationError -> {
            MatchSetupResultMessage(
                message = startMatchResult.message,
                color = MaterialTheme.colorScheme.error,
            )
        }

        is MatchSetupStartMatchResult.Ready -> {
            MatchSetupResultMessage(
                message = "${startMatchResult.matchSetup.teamAName} vs ${startMatchResult.matchSetup.teamBName} is ready.",
                color = MaterialTheme.colorScheme.primary,
            )
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEvent(MatchSetupScreenEvent.ResetRequested) },
            ) {
                Text("Prepare another match")
            }
        }
    }
}

@Composable
private fun MatchSetupResultMessage(
    message: String,
    color: Color,
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = message,
        color = color,
        style = MaterialTheme.typography.bodyMedium,
    )
}
