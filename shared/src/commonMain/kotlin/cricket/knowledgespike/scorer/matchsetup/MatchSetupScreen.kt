package cricket.knowledgespike.scorer.matchsetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner
import cricket.knowledgespike.scorer.ui.theme.ScorerSpacing

@Composable
fun MatchSetupScreen(
    widthSizeClass: WindowWidthSizeClass,
    screenState: MatchSetupScreenState,
    contentPadding: PaddingValues,
    onEvent: (MatchSetupScreenEvent) -> Unit,
) {
    val layoutMetrics = layoutMetricsFor(widthSizeClass)

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(
                    horizontal = layoutMetrics.screenHorizontalPadding,
                    vertical = ScorerSpacing.Large,
                ),
            verticalArrangement = Arrangement.spacedBy(ScorerSpacing.Large),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MatchSetupHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = layoutMetrics.contentMaxWidth),
            )

            when (layoutMetrics.layoutType) {
                MatchSetupLayoutType.Expanded -> {
                    ExpandedMatchSetupLayout(
                        screenState = screenState,
                        onEvent = onEvent,
                        contentMaxWidth = layoutMetrics.contentMaxWidth,
                    )
                }

                MatchSetupLayoutType.Compact -> {
                    CompactMatchSetupLayout(
                        screenState = screenState,
                        onEvent = onEvent,
                        contentMaxWidth = layoutMetrics.contentMaxWidth,
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactMatchSetupLayout(
    screenState: MatchSetupScreenState,
    onEvent: (MatchSetupScreenEvent) -> Unit,
    contentMaxWidth: Dp,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = contentMaxWidth),
        verticalArrangement = Arrangement.spacedBy(ScorerSpacing.Large),
    ) {
        RequiredMatchDetailsSection(
            formState = screenState.formState,
            onEvent = onEvent,
            useHorizontalChoiceLayout = false,
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
    contentMaxWidth: Dp,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = contentMaxWidth),
        horizontalArrangement = Arrangement.spacedBy(ScorerSpacing.Large),
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(ScorerSpacing.Large),
        ) {
            RequiredMatchDetailsSection(
                formState = screenState.formState,
                onEvent = onEvent,
                useHorizontalChoiceLayout = true,
            )
            MatchSetupActionSection(
                screenState = screenState,
                onEvent = onEvent,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(ScorerSpacing.Large),
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
    useHorizontalChoiceLayout: Boolean,
) {
    MatchSetupSectionCard(
        title = "Required details",
        supportingText = "Complete these fields to enable Start Match.",
    ) {
        MatchSetupTextField(
            value = formState.teamAName,
            onValueChange = { onEvent(MatchSetupScreenEvent.TeamANameChanged(it)) },
            label = "Team A",
        )
        MatchSetupTextField(
            value = formState.teamBName,
            onValueChange = { onEvent(MatchSetupScreenEvent.TeamBNameChanged(it)) },
            label = "Team B",
        )
        MatchSetupTextField(
            value = formState.scheduledOvers,
            onValueChange = { onEvent(MatchSetupScreenEvent.ScheduledOversChanged(it)) },
            label = "Scheduled overs",
        )
        MatchSetupTextField(
            value = formState.matchDate,
            onValueChange = { onEvent(MatchSetupScreenEvent.MatchDateChanged(it)) },
            label = "Match date (YYYY-MM-DD)",
        )

        TossSelectionsSection(
            formState = formState,
            onEvent = onEvent,
            useHorizontalLayout = useHorizontalChoiceLayout,
        )
    }
}

@Composable
private fun OptionalMatchDetailsSection(
    formState: MatchSetupFormState,
    onEvent: (MatchSetupScreenEvent) -> Unit,
) {
    MatchSetupSectionCard(
        title = "Optional details",
        supportingText = "Rosters can be added later. Venue, umpires, and weather are optional.",
    ) {
        MatchSetupTextField(
            value = formState.venue,
            onValueChange = { onEvent(MatchSetupScreenEvent.VenueChanged(it)) },
            label = "Venue",
        )
        MatchSetupTextField(
            value = formState.umpireOne,
            onValueChange = { onEvent(MatchSetupScreenEvent.UmpireOneChanged(it)) },
            label = "Umpire one",
        )
        MatchSetupTextField(
            value = formState.umpireTwo,
            onValueChange = { onEvent(MatchSetupScreenEvent.UmpireTwoChanged(it)) },
            label = "Umpire two",
        )
        MatchSetupTextField(
            value = formState.weather,
            onValueChange = { onEvent(MatchSetupScreenEvent.WeatherChanged(it)) },
            label = "Weather",
        )
    }
}

@Composable
private fun TossSelectionsSection(
    formState: MatchSetupFormState,
    onEvent: (MatchSetupScreenEvent) -> Unit,
    useHorizontalLayout: Boolean,
) {
    if (useHorizontalLayout) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ScorerSpacing.Medium),
            verticalAlignment = Alignment.Top,
        ) {
            TossWinnerSection(
                modifier = Modifier.weight(1f),
                selectedValue = formState.tossWinner,
                onSelect = { onEvent(MatchSetupScreenEvent.TossWinnerChanged(it)) },
            )
            TossDecisionSection(
                modifier = Modifier.weight(1f),
                selectedValue = formState.tossDecision,
                onSelect = { onEvent(MatchSetupScreenEvent.TossDecisionChanged(it)) },
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(ScorerSpacing.Medium),
        ) {
            TossWinnerSection(
                selectedValue = formState.tossWinner,
                onSelect = { onEvent(MatchSetupScreenEvent.TossWinnerChanged(it)) },
            )
            TossDecisionSection(
                selectedValue = formState.tossDecision,
                onSelect = { onEvent(MatchSetupScreenEvent.TossDecisionChanged(it)) },
            )
        }
    }
}

@Composable
private fun TossWinnerSection(
    modifier: Modifier = Modifier,
    selectedValue: TossWinner?,
    onSelect: (TossWinner) -> Unit,
) {
    ChoiceSection(
        modifier = modifier,
        title = "Toss winner",
    ) {
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
}

@Composable
private fun TossDecisionSection(
    modifier: Modifier = Modifier,
    selectedValue: TossDecision?,
    onSelect: (TossDecision) -> Unit,
) {
    ChoiceSection(
        modifier = modifier,
        title = "Toss decision",
    ) {
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
}

@Composable
private fun ChoiceSection(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ScorerSpacing.Small),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
        content()
    }
}

@Composable
private fun RadioOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton,
            )
            .padding(ScorerSpacing.XSmall),
        horizontalArrangement = Arrangement.spacedBy(ScorerSpacing.Small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun MatchSetupActionSection(
    screenState: MatchSetupScreenState,
    onEvent: (MatchSetupScreenEvent) -> Unit,
) {
    MatchSetupSectionCard(
        title = "Actions",
        supportingText = "Start Match becomes available after all required fields are valid.",
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
}

@Composable
private fun MatchSetupResultMessage(
    message: String,
    color: Color,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = MaterialTheme.shapes.medium,
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = ScorerSpacing.Medium,
                vertical = ScorerSpacing.Small,
            ),
            text = message,
            color = color,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun MatchSetupHeader(
    modifier: Modifier = Modifier,
) {
    MatchSetupSectionCard(
        modifier = modifier,
        title = "Match setup",
        supportingText = "Create a new match by entering the required details first, then add optional context.",
        contentPadding = PaddingValues(ScorerSpacing.Large),
    ) {
        Text(
            text = "Core required fields: Team A, Team B, overs, toss winner, toss decision, and date.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun MatchSetupSectionCard(
    title: String,
    supportingText: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(ScorerSpacing.Large),
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(ScorerSpacing.Medium),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            HorizontalDivider()
            content()
        }
    }
}

@Composable
private fun MatchSetupTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
    )
}

@Immutable
private data class MatchSetupLayoutMetrics(
    val layoutType: MatchSetupLayoutType,
    val screenHorizontalPadding: Dp,
    val contentMaxWidth: Dp,
)

private enum class MatchSetupLayoutType {
    Compact,
    Expanded,
}

private fun layoutMetricsFor(widthSizeClass: WindowWidthSizeClass): MatchSetupLayoutMetrics {
    return when (widthSizeClass) {
        WindowWidthSizeClass.Expanded -> MatchSetupLayoutMetrics(
            layoutType = MatchSetupLayoutType.Expanded,
            screenHorizontalPadding = ScorerSpacing.XXLarge,
            contentMaxWidth = ScorerSpacing.ExpandedContentMaxWidth,
        )

        WindowWidthSizeClass.Medium -> MatchSetupLayoutMetrics(
            layoutType = MatchSetupLayoutType.Compact,
            screenHorizontalPadding = ScorerSpacing.XLarge,
            contentMaxWidth = ScorerSpacing.MediumContentMaxWidth,
        )

        else -> MatchSetupLayoutMetrics(
            layoutType = MatchSetupLayoutType.Compact,
            screenHorizontalPadding = ScorerSpacing.Large,
            contentMaxWidth = ScorerSpacing.CompactContentMaxWidth,
        )
    }
}
