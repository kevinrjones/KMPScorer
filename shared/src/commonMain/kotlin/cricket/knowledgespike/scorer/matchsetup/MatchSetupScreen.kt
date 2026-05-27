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
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import cricket.knowledgespike.scorer.domain.matchsetup.MatchScheduleType
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner
import cricket.knowledgespike.scorer.ui.theme.ScorerSpacing

private const val MatchScheduleTypeFieldTag = "match_setup_schedule_type_field"
private const val MatchScheduleAmountFieldTag = "match_setup_schedule_amount_field"
private const val MatchDateFieldTag = "match_setup_match_date_field"
private const val MatchDatePickButtonTag = "match_setup_match_date_pick_button"
private const val VenueFieldTag = "match_setup_venue_field"
private const val TossWinnerGroupTag = "match_setup_toss_winner_group"
private const val TossDecisionGroupTag = "match_setup_toss_decision_group"
private const val TossWinnerTeamAOptionTag = "match_setup_toss_winner_team_a_option"
private const val TossWinnerTeamBOptionTag = "match_setup_toss_winner_team_b_option"
private const val TossDecisionBatOptionTag = "match_setup_toss_decision_bat_option"
private const val TossDecisionBowlOptionTag = "match_setup_toss_decision_bowl_option"

@Composable
fun MatchSetupScreen(
    widthSizeClass: WindowWidthSizeClass,
    screenState: MatchSetupScreenState,
    contentPadding: PaddingValues,
    onEvent: (MatchSetupScreenEvent) -> Unit,
    matchDatePickerDialog: @Composable (
        initialDateIso: String?,
        onDismissRequest: () -> Unit,
        onDateConfirmed: (String) -> Unit,
    ) -> Unit = { initialDateIso, onDismissRequest, onDateConfirmed ->
        PlatformMatchDatePickerDialog(
            initialDateIso = initialDateIso,
            onDismissRequest = onDismissRequest,
            onDateConfirmed = onDateConfirmed,
        )
    },
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
                        matchDatePickerDialog = matchDatePickerDialog,
                        contentMaxWidth = layoutMetrics.contentMaxWidth,
                    )
                }

                MatchSetupLayoutType.Compact -> {
                    CompactMatchSetupLayout(
                        screenState = screenState,
                        onEvent = onEvent,
                        matchDatePickerDialog = matchDatePickerDialog,
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
    matchDatePickerDialog: @Composable (
        initialDateIso: String?,
        onDismissRequest: () -> Unit,
        onDateConfirmed: (String) -> Unit,
    ) -> Unit,
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
            matchDatePickerDialog = matchDatePickerDialog,
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
    matchDatePickerDialog: @Composable (
        initialDateIso: String?,
        onDismissRequest: () -> Unit,
        onDateConfirmed: (String) -> Unit,
    ) -> Unit,
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
                matchDatePickerDialog = matchDatePickerDialog,
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
    matchDatePickerDialog: @Composable (
        initialDateIso: String?,
        onDismissRequest: () -> Unit,
        onDateConfirmed: (String) -> Unit,
    ) -> Unit,
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
        MatchScheduleInputRow(
            formState = formState,
            onEvent = onEvent,
        )
        MatchDateField(
            value = formState.matchDate,
            onDateSelected = { onEvent(MatchSetupScreenEvent.MatchDateChanged(it)) },
            matchDatePickerDialog = matchDatePickerDialog,
            modifier = Modifier.testTag(MatchDateFieldTag),
        )

        TossSelectionsSection(
            formState = formState,
            onEvent = onEvent,
            useHorizontalLayout = useHorizontalChoiceLayout,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MatchScheduleInputRow(
    formState: MatchSetupFormState,
    onEvent: (MatchSetupScreenEvent) -> Unit,
) {
    var scheduleTypeMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ScorerSpacing.Small),
        verticalAlignment = Alignment.Top,
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier.weight(1f),
            expanded = scheduleTypeMenuExpanded,
            onExpandedChange = { scheduleTypeMenuExpanded = !scheduleTypeMenuExpanded },
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag(MatchScheduleTypeFieldTag),
                value = formState.scheduleType.toUiLabel(),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                label = { Text("Schedule type") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = scheduleTypeMenuExpanded)
                },
            )
            DropdownMenu(
                expanded = scheduleTypeMenuExpanded,
                onDismissRequest = { scheduleTypeMenuExpanded = false },
            ) {
                MatchScheduleType.entries.forEach { scheduleType ->
                    DropdownMenuItem(
                        text = { Text(scheduleType.toUiLabel()) },
                        onClick = {
                            scheduleTypeMenuExpanded = false
                            onEvent(MatchSetupScreenEvent.ScheduleTypeChanged(scheduleType))
                        },
                    )
                }
            }
        }

        MatchSetupTextField(
            modifier = Modifier
                .weight(0.42f)
                .testTag(MatchScheduleAmountFieldTag),
            value = formState.scheduleAmount,
            onValueChange = { rawValue ->
                onEvent(MatchSetupScreenEvent.ScheduleAmountChanged(rawValue.toScheduleAmountInput()))
            },
            label = "Amount",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
    }
}

private fun MatchScheduleType.toUiLabel(): String {
    return when (this) {
        MatchScheduleType.Overs -> "Overs"
        MatchScheduleType.Balls -> "Balls"
        MatchScheduleType.Days -> "Days"
    }
}

private fun String.toScheduleAmountInput(): String {
    return filter(Char::isDigit).take(3)
}

@Composable
private fun MatchDateField(
    value: String,
    onDateSelected: (String) -> Unit,
    matchDatePickerDialog: @Composable (
        initialDateIso: String?,
        onDismissRequest: () -> Unit,
        onDateConfirmed: (String) -> Unit,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isPickerVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .onPreviewKeyEvent { event ->
                if (
                    event.type == KeyEventType.KeyDown &&
                    (event.key == Key.Enter || event.key == Key.NumPadEnter)
                ) {
                    isPickerVisible = true
                    true
                } else {
                    false
                }
            },
        value = value,
        onValueChange = {},
        readOnly = true,
        label = { Text("Match date") },
        placeholder = { Text("YYYY-MM-DD") },
        trailingIcon = {
            TextButton(
                modifier = Modifier
                    .testTag(MatchDatePickButtonTag)
                    .focusProperties {
                        canFocus = false
                    },
                onClick = { isPickerVisible = true },
            ) {
                Text("Pick")
            }
        },
    )

    if (isPickerVisible) {
        matchDatePickerDialog(
            value.toCanonicalMatchDateOrNull(),
            { isPickerVisible = false },
            { selectedDate ->
                isPickerVisible = false
                val canonicalDate = selectedDate.toCanonicalMatchDateOrNull() ?: return@matchDatePickerDialog
                if (canonicalDate != value) {
                    onDateSelected(canonicalDate)
                }
            },
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
        supportingText = "Player names can be added later. Venue, umpires, and weather are optional.",
    ) {
        MatchSetupTextField(
            value = formState.venue,
            onValueChange = { onEvent(MatchSetupScreenEvent.VenueChanged(it)) },
            label = "Venue",
            modifier = Modifier.testTag(VenueFieldTag),
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
    val tossWinnerOptionLabels = formState.toTossWinnerOptionLabels()

    if (useHorizontalLayout) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(ScorerSpacing.Medium),
            verticalAlignment = Alignment.Top,
        ) {
            TossWinnerSection(
                modifier = Modifier.weight(1f),
                selectedValue = formState.tossWinner,
                teamAOptionLabel = tossWinnerOptionLabels.teamA,
                teamBOptionLabel = tossWinnerOptionLabels.teamB,
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
                teamAOptionLabel = tossWinnerOptionLabels.teamA,
                teamBOptionLabel = tossWinnerOptionLabels.teamB,
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
    teamAOptionLabel: String,
    teamBOptionLabel: String,
    onSelect: (TossWinner) -> Unit,
) {
    val options = listOf(TossWinner.TeamA, TossWinner.TeamB)
    val focusRequesterByOption = remember {
        options.associateWith { FocusRequester() }
    }
    var focusedOption by remember { mutableStateOf(selectedValue ?: TossWinner.TeamA) }
    var pendingFocusRequest by remember { mutableStateOf<TossWinner?>(null) }

    LaunchedEffect(pendingFocusRequest) {
        pendingFocusRequest?.let { option ->
            focusRequesterByOption.getValue(option).requestFocus()
            pendingFocusRequest = null
        }
    }

    ChoiceSection(
        modifier = modifier
            .testTag(TossWinnerGroupTag)
            .selectableGroup()
            .onPreviewKeyEvent { event ->
                handleChoiceSectionDirectionalKey(
                    event = event,
                    selectedValue = selectedValue,
                    options = options,
                    onSelect = onSelect,
                    onDirectionalFocusOption = { nextOption ->
                        focusedOption = nextOption
                        pendingFocusRequest = nextOption
                    },
                )
            },
        title = "Toss winner",
    ) {
        RadioOption(
            modifier = Modifier
                .testTag(TossWinnerTeamAOptionTag)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        focusedOption = TossWinner.TeamA
                    }
                },
            text = teamAOptionLabel,
            selected = selectedValue == TossWinner.TeamA,
            onClick = { onSelect(TossWinner.TeamA) },
            allowKeyboardFocus = focusedOption != TossWinner.TeamB,
            focusRequester = focusRequesterByOption.getValue(TossWinner.TeamA),
        )
        RadioOption(
            modifier = Modifier
                .testTag(TossWinnerTeamBOptionTag)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        focusedOption = TossWinner.TeamB
                    }
                },
            text = teamBOptionLabel,
            selected = selectedValue == TossWinner.TeamB,
            onClick = { onSelect(TossWinner.TeamB) },
            allowKeyboardFocus = focusedOption == TossWinner.TeamB,
            focusRequester = focusRequesterByOption.getValue(TossWinner.TeamB),
        )
    }
}

@Composable
private fun TossDecisionSection(
    modifier: Modifier = Modifier,
    selectedValue: TossDecision?,
    onSelect: (TossDecision) -> Unit,
) {
    val options = listOf(TossDecision.Bat, TossDecision.Bowl)
    val focusRequesterByOption = remember {
        options.associateWith { FocusRequester() }
    }
    var focusedOption by remember { mutableStateOf(selectedValue ?: TossDecision.Bat) }
    var pendingFocusRequest by remember { mutableStateOf<TossDecision?>(null) }

    LaunchedEffect(pendingFocusRequest) {
        pendingFocusRequest?.let { option ->
            focusRequesterByOption.getValue(option).requestFocus()
            pendingFocusRequest = null
        }
    }

    ChoiceSection(
        modifier = modifier
            .testTag(TossDecisionGroupTag)
            .selectableGroup()
            .onPreviewKeyEvent { event ->
                handleChoiceSectionDirectionalKey(
                    event = event,
                    selectedValue = selectedValue,
                    options = options,
                    onSelect = onSelect,
                    onDirectionalFocusOption = { nextOption ->
                        focusedOption = nextOption
                        pendingFocusRequest = nextOption
                    },
                )
            },
        title = "Toss decision",
    ) {
        RadioOption(
            modifier = Modifier
                .testTag(TossDecisionBatOptionTag)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        focusedOption = TossDecision.Bat
                    }
                },
            text = "Bat",
            selected = selectedValue == TossDecision.Bat,
            onClick = { onSelect(TossDecision.Bat) },
            allowKeyboardFocus = focusedOption != TossDecision.Bowl,
            focusRequester = focusRequesterByOption.getValue(TossDecision.Bat),
        )
        RadioOption(
            modifier = Modifier
                .testTag(TossDecisionBowlOptionTag)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        focusedOption = TossDecision.Bowl
                    }
                },
            text = "Bowl",
            selected = selectedValue == TossDecision.Bowl,
            onClick = { onSelect(TossDecision.Bowl) },
            allowKeyboardFocus = focusedOption == TossDecision.Bowl,
            focusRequester = focusRequesterByOption.getValue(TossDecision.Bowl),
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
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    allowKeyboardFocus: Boolean,
    focusRequester: FocusRequester,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .focusProperties {
                canFocus = allowKeyboardFocus
            }
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

private fun <T> handleChoiceSectionDirectionalKey(
    event: KeyEvent,
    selectedValue: T?,
    options: List<T>,
    onSelect: (T) -> Unit,
    onDirectionalFocusOption: (T) -> Unit,
): Boolean {
    if (event.type != KeyEventType.KeyDown) {
        return false
    }

    val direction = when (event.key) {
        Key.DirectionDown, Key.DirectionRight -> 1
        Key.DirectionUp, Key.DirectionLeft -> -1
        else -> return false
    }

    val nextOption = options.nextDirectionalOption(
        selectedValue = selectedValue,
        direction = direction,
    ) ?: return false

    if (nextOption != selectedValue) {
        onSelect(nextOption)
    }
    onDirectionalFocusOption(nextOption)
    return true
}

private fun <T> List<T>.nextDirectionalOption(
    selectedValue: T?,
    direction: Int,
): T? {
    if (isEmpty()) {
        return null
    }

    val selectedIndex = indexOf(selectedValue)
    if (selectedIndex < 0) {
        return if (direction > 0) first() else last()
    }

    val nextIndex = (selectedIndex + direction).coerceIn(0, lastIndex)
    return get(nextIndex)
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
            text = "Core required fields: Team A, Team B, schedule type + amount, toss winner, toss decision, and date.",
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
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
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
