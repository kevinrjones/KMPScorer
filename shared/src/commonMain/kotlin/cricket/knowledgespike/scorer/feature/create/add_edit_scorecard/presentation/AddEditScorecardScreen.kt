@file:OptIn(ExperimentalMaterial3Api::class)
package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.AddEditScorecardUseCases
import cricket.knowledgespike.scorer.foundation.compose.moveFocusOnTab
import cricket.knowledgespike.scorer.foundation.ValidationReason
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.batting_side_label
import kmpscorer.shared.generated.resources.cancel
import kmpscorer.shared.generated.resources.date_label
import kmpscorer.shared.generated.resources.duration_label
import kmpscorer.shared.generated.resources.invalid_batting_side_name
import kmpscorer.shared.generated.resources.invalid_team_winning_toss_name
import kmpscorer.shared.generated.resources.missing_date
import kmpscorer.shared.generated.resources.missing_duration
import kmpscorer.shared.generated.resources.missing_match_title
import kmpscorer.shared.generated.resources.missing_scorer
import kmpscorer.shared.generated.resources.missing_start_time
import kmpscorer.shared.generated.resources.missing_team_name
import kmpscorer.shared.generated.resources.missing_type_of_match
import kmpscorer.shared.generated.resources.missing_venue
import kmpscorer.shared.generated.resources.opponents_label
import kmpscorer.shared.generated.resources.pitch_conditions_label
import kmpscorer.shared.generated.resources.referee_label
import kmpscorer.shared.generated.resources.save
import kmpscorer.shared.generated.resources.saved_scorecard
import kmpscorer.shared.generated.resources.scorer1_label
import kmpscorer.shared.generated.resources.scorer2_label
import kmpscorer.shared.generated.resources.start_time_label
import kmpscorer.shared.generated.resources.team_label
import kmpscorer.shared.generated.resources.team_winning_toss_label
import kmpscorer.shared.generated.resources.third_umpire_label
import kmpscorer.shared.generated.resources.title_label
import kmpscorer.shared.generated.resources.type_of_match_label
import kmpscorer.shared.generated.resources.umpire1_label
import kmpscorer.shared.generated.resources.umpire2_label
import kmpscorer.shared.generated.resources.unable_to_save_scorecard
import kmpscorer.shared.generated.resources.venue_label
import kmpscorer.shared.generated.resources.weather_label
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddEditScorecardScreenRoot(
    viewModel: AddEditScorecardViewModel = koinViewModel<AddEditScorecardViewModel>(),
    isExpandedScreen: Boolean = false,
    onSaveOrCancel: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val savedMessage = stringResource(Res.string.saved_scorecard)
    val unableToSaveScorecardMessage = stringResource(Res.string.unable_to_save_scorecard)

    LaunchedEffect(true) {
        viewModel.addEditScorecardEvent.collectLatest { event ->
            when (event) {
                AddEditScorecardEvent.SavedScorecard -> {
                    snackbarHostState.showSnackbar(message = savedMessage)
                    onSaveOrCancel()
                }

                is AddEditScorecardEvent.ErrorSavingScorecard -> {
                    snackbarHostState.showSnackbar(message = unableToSaveScorecardMessage)
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Create a New Scorecard")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }

    ) { innerPadding: PaddingValues ->

        AddEditScorecardScreen(
            innerPadding = innerPadding,
            useCases = viewModel.addEditScorecardUseCases,
            state = state,
            onUiEvent = viewModel::onEvent,
            onSaveOrCancel = onSaveOrCancel,
            isExpandedScreen = isExpandedScreen,
            isValid = viewModel.isValid
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScorecardScreen(
    innerPadding: PaddingValues,
    useCases: AddEditScorecardUseCases,
    state: AddEditScorecardState,
    onUiEvent: (AddEditScorecardUiEvent) -> Unit,
    onSaveOrCancel: () -> Unit,
    isExpandedScreen: Boolean = false,
    isValid: Boolean = false,
    modifier: Modifier = Modifier,
) {


    Column(
        modifier = Modifier.padding(innerPadding)
            .fillMaxSize()
    ) {

        Row(modifier = Modifier.weight(0.9f)) {
            if (!isExpandedScreen) {
                SingleColumnDisplay(
                    useCases = useCases,
                    state = state,
                    modifier = modifier,
                    onEvent = onUiEvent
                )
            } else {
                TwoColumnDisplay(
                    useCases = useCases,
                    state = state,
                    onEvent = onUiEvent
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                onUiEvent(AddEditScorecardUiEvent.SaveScorecard)
            }, enabled = isValid) {
                Text(text = stringResource(Res.string.save))
            }

            Button(onClick = onSaveOrCancel) {
                Text(text = stringResource(Res.string.cancel))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }

}


@Composable
fun TwoColumnDisplay(
    modifier: Modifier = Modifier,
    state: AddEditScorecardState,
    useCases: AddEditScorecardUseCases,
    onEvent: (event: AddEditScorecardUiEvent) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(start = 16.dp, end = 16.dp),

        ) {

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {


            var result: ValidationReason = useCases.validateTeamName(state.teamName)
            AddEditScorecardScreenField(
                modifier = Modifier.fillMaxWidth(),
                value = state.teamName,
                labelId = Res.string.team_label,
                isError = result !is ValidationReason.Succeeded && state.teamNameChanged,
                errorMessage = Res.string.missing_team_name,
                onValueChange = {
                    onEvent(AddEditScorecardUiEvent.EnteredTeamName(it))
                })

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(24.dp)
            ) {
                Text(
                    text = "vs",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                )
            }

            result = useCases.validateTeamName(state.opponentsName)
            AddEditScorecardScreenField(
                modifier = Modifier.fillMaxWidth(),
                value = state.opponentsName,
                labelId = Res.string.opponents_label,
                isError = (result !is ValidationReason.Succeeded) && state.opponentsNameChanged,
                errorMessage = Res.string.missing_team_name,
                onValueChange = {
                    onEvent(AddEditScorecardUiEvent.EnteredOpponentsName(it))
                },
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {

            var result = useCases.validateVenue(state.venue)
            AddEditScorecardScreenField(
                modifier = Modifier.fillMaxWidth(),
                value = state.venue,
                labelId = Res.string.venue_label,
                isError = (result !is ValidationReason.Succeeded) && state.venueChanged,
                errorMessage = Res.string.missing_venue,
                onValueChange = {
                    onEvent(AddEditScorecardUiEvent.EnteredVenue(it))
                },
            )
            Spacer(modifier = Modifier.width(24.dp))

            result = useCases.validateTitle(state.title)
            AddEditScorecardScreenField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,
                labelId = Res.string.title_label,
                isError = (result !is ValidationReason.Succeeded) && state.titleChanged,
                errorMessage = Res.string.missing_match_title,
                onValueChange = {
                    onEvent(AddEditScorecardUiEvent.EnteredTitle(it))
                },
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        var result = useCases.validateMatchDate(state.matchDate)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.matchDate,
            labelId = Res.string.date_label,
            isError = (result !is ValidationReason.Succeeded) && state.matchDateChanged,
            errorMessage = Res.string.missing_date,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredDate(it))
            },
        )
        Spacer(modifier = Modifier.width(24.dp))

        result = useCases.validateBattingSide(
            state.battingSide,
            teamName = state.teamName,
            opponentsName = state.opponentsName
        )
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.battingSide,
            labelId = Res.string.batting_side_label,
            isError = (result !is ValidationReason.Succeeded) && state.battingSideChanged,
            errorMessage = Res.string.invalid_batting_side_name,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredBattingSide(it))
            },
        )
    }
    Spacer(modifier = Modifier.height(4.dp))

    Row(modifier = Modifier.fillMaxWidth()) {
        AddEditScorecardScreenField(
            modifier = Modifier.weight(0.5f),
            value = state.umpire1Name ?: "",
            labelId = Res.string.umpire1_label,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredUmpire1Name(it))
            },
        )
        Spacer(modifier = Modifier.width(24.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.weight(0.5f),
            value = state.umpire2Name ?: "",
            labelId = Res.string.umpire2_label,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredUmpire2Name(it))
            },
        )
    }
    Spacer(modifier = Modifier.height(4.dp))

    Row(modifier = Modifier.fillMaxWidth()) {
        AddEditScorecardScreenField(
            modifier = Modifier.weight(0.5f),
            value = state.thirdUmpireName ?: "",
            labelId = Res.string.third_umpire_label,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredThirdUmpireName(it))
            },
        )
        Spacer(modifier = Modifier.width(24.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.weight(0.5f),
            value = state.refereeName ?: "",
            labelId = Res.string.referee_label,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredReferee(it))
            },
        )
    }
    Spacer(modifier = Modifier.height(4.dp))

    Row(modifier = Modifier.fillMaxWidth()) {
        val result = useCases.validateScorer(state.scorer1Name)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.scorer1Name,
            labelId = Res.string.scorer1_label,
            isError = (result !is ValidationReason.Succeeded) && state.scorer1NameChanged,
            errorMessage = Res.string.missing_scorer,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredScorer1Name(it))
            },
        )
        Spacer(modifier = Modifier.width(24.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.weight(0.5f),
            value = state.scorer2Name ?: "",
            labelId = Res.string.scorer2_label,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredScorer2Name(it))
            },
        )
    }
    Spacer(modifier = Modifier.height(4.dp))

    Row(modifier = Modifier.fillMaxWidth()) {
        var result = useCases.validateMatchLabel(state.typeOfMatch)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.typeOfMatch,
            labelId = Res.string.type_of_match_label,
            isError = (result !is ValidationReason.Succeeded) && state.typeOfMatchChanged,
            errorMessage = Res.string.missing_type_of_match,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredTypeOfMatch(it))
            },
        )
        Spacer(modifier = Modifier.width(24.dp))

        result = useCases.validateDuration(state.duration)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.duration,
            labelId = Res.string.duration_label,
            isError = (result !is ValidationReason.Succeeded) && state.durationChanged,
            errorMessage = Res.string.missing_duration,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredDuration(it))
            },
        )

    }
    Spacer(modifier = Modifier.height(4.dp))

    Row(modifier = Modifier.fillMaxWidth()) {
        var result = useCases.validateStartTime(state.startTime)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.startTime,
            labelId = Res.string.start_time_label,
            isError = (result !is ValidationReason.Succeeded) && state.startTimeChanged,
            errorMessage = Res.string.missing_start_time,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredStartTime(it))
            },
        )
        Spacer(modifier = Modifier.width(24.dp))

        result = useCases.validateTeamWinningToss(
            state.teamWinningToss,
            state.teamName,
            state.opponentsName
        )
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.teamWinningToss,
            labelId = Res.string.team_winning_toss_label,
            isError = (result !is ValidationReason.Succeeded) && state.teamWinningTossChanged,
            errorMessage = Res.string.invalid_team_winning_toss_name,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredTeamWinningToss(it))
            },
        )
    }
    Spacer(modifier = Modifier.height(4.dp))

    Row(modifier = Modifier.fillMaxWidth()) {
        AddEditScorecardScreenField(
            modifier = Modifier.weight(0.5f),
            value = state.weather ?: "",
            labelId = Res.string.weather_label,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredWeather(it))
            },
        )
        Spacer(modifier = Modifier.width(24.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.weight(0.5f),
            value = state.pitchCondition ?: "",
            labelId = Res.string.pitch_conditions_label,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredPitchCondition(it))
            },
        )
    }
}


@Composable
private fun SingleColumnDisplay(
    modifier: Modifier = Modifier,
    state: AddEditScorecardState,
    useCases: AddEditScorecardUseCases,
    onEvent: (event: AddEditScorecardUiEvent) -> Unit
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(start = 16.dp, end = 16.dp),

        ) {

        Spacer(modifier = Modifier.height(8.dp))

        var result = useCases.validateTeamName(state.teamName)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.teamName,
            labelId = Res.string.team_label,
            isError = (result !is ValidationReason.Succeeded) && state.teamNameChanged,
            errorMessage = Res.string.missing_team_name,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredTeamName(it))
            })
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateTeamName(state.opponentsName)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.opponentsName,
            labelId = Res.string.opponents_label,
            isError = (result !is ValidationReason.Succeeded) && state.opponentsNameChanged,
            errorMessage = Res.string.missing_team_name,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredOpponentsName(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateVenue(state.venue)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.venue,
            labelId = Res.string.venue_label,
            isError = (result !is ValidationReason.Succeeded) && state.venueChanged,
            errorMessage = Res.string.missing_type_of_match,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredVenue(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateTitle(state.title)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.title,
            labelId = Res.string.title_label,
            isError = (result !is ValidationReason.Succeeded) && state.titleChanged,
            errorMessage = Res.string.missing_match_title,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredTitle(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateMatchDate(state.matchDate)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.matchDate,
            labelId = Res.string.date_label,
            isError = (result !is ValidationReason.Succeeded) && state.matchDateChanged,
            errorMessage = Res.string.missing_date,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredDate(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateBattingSide(
            state.battingSide,
            teamName = state.teamName,
            opponentsName = state.opponentsName
        )
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.battingSide,
            labelId = Res.string.batting_side_label,
            isError = (result !is ValidationReason.Succeeded) && state.battingSideChanged,
            errorMessage = Res.string.invalid_batting_side_name,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredBattingSide(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.umpire1Name ?: "",
            labelId = Res.string.umpire1_label,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredUmpire1Name(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.umpire2Name ?: "",
            labelId = Res.string.umpire2_label,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredUmpire2Name(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.thirdUmpireName ?: "",
            labelId = Res.string.third_umpire_label,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredThirdUmpireName(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.refereeName ?: "",
            labelId = Res.string.referee_label,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredReferee(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateScorer(state.scorer1Name)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.scorer1Name,
            labelId = Res.string.scorer1_label,
            isError = (result !is ValidationReason.Succeeded) && state.scorer1NameChanged,
            errorMessage = Res.string.missing_scorer,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredScorer1Name(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.scorer2Name ?: "",
            labelId = Res.string.scorer2_label,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredScorer2Name(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateMatchLabel(state.typeOfMatch)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.typeOfMatch,
            labelId = Res.string.type_of_match_label,
            isError = (result !is ValidationReason.Succeeded) && state.typeOfMatchChanged,
            errorMessage = Res.string.missing_type_of_match,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredTypeOfMatch(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateDuration(state.duration)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.duration,
            labelId = Res.string.duration_label,
            isError = (result !is ValidationReason.Succeeded) && state.durationChanged,
            errorMessage = Res.string.missing_duration,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredDuration(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateStartTime(state.startTime)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.startTime,
            labelId = Res.string.start_time_label,
            isError = (result !is ValidationReason.Succeeded) && state.startTimeChanged,
            errorMessage = Res.string.missing_start_time,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredStartTime(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateTeamWinningToss(
            state.teamWinningToss,
            state.teamName,
            state.opponentsName
        )
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.teamWinningToss,
            labelId = Res.string.team_winning_toss_label,
            isError = (result !is ValidationReason.Succeeded) && state.teamWinningTossChanged,
            errorMessage = Res.string.invalid_team_winning_toss_name,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredTeamWinningToss(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.weather ?: "",
            labelId = Res.string.weather_label,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredWeather(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = state.pitchCondition ?: "",
            labelId = Res.string.pitch_conditions_label,
            onValueChange = {
                onEvent(AddEditScorecardUiEvent.EnteredPitchCondition(it))
            },
        )
    }
}

@Composable
fun AddEditScorecardScreenField(
    modifier: Modifier,
    value: String,
    labelId: StringResource,
    isError: Boolean = false,
    onValueChange: (String) -> Unit,
    errorMessage: StringResource? = null
) {
    val focusManager = LocalFocusManager.current
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .moveFocusOnTab(focusManager)
                .fillMaxWidth(),
            label = {
                Text(stringResource(labelId))
            },
            value = value,
            onValueChange = {
                onValueChange(it)

            },
            isError = isError,
            singleLine = true
        )
        if (isError && errorMessage != null) {
            Text(
                text = stringResource(errorMessage),
                color = MaterialTheme.colorScheme.error,
                fontSize = 16.sp
            )
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun AddEditScorecardScreenPreview() {
//    ScorerTheme {
//        Surface {
//            AddEditScorecardScreen(
//                onSaveOrCancel = {},
//                state = AddEditScorecardState(),
//                useCases = AddEditScorecardUseCases(ValidateTeamName(), UpsertScorecard())
//            )
//        }
//    }
//}
