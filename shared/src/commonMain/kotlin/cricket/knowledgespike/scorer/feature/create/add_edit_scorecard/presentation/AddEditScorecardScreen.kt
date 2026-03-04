package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.AddEditScorecardUseCases
import cricket.knowledgespike.scorer.foundation.UiText
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
import kmpscorer.shared.generated.resources.missing_opponents_name
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

    AddEditScorecardScreen(
        state = state,
        useCases = viewModel.addEditScorecardUseCases,
        isExpandedScreen = isExpandedScreen,
        onSaveOrCancel = onSaveOrCancel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScorecardScreen(
    state: AddEditScorecardState,
    useCases: AddEditScorecardUseCases,
    isExpandedScreen: Boolean = false,
    onSaveOrCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val savedMessage = stringResource(Res.string.saved_scorecard)
    val unableToSaveScorecardMessage = stringResource(Res.string.unable_to_save_scorecard)
    val viewModel = AddEditScorecardViewModel(null, useCases)

    LaunchedEffect(true) {
        viewModel.eventFlow.collectLatest { event ->
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

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(modifier = Modifier.weight(0.9f)) {
                if (!isExpandedScreen) {
                    SingleColumnDisplay(
                        useCases = useCases,
                        state = state,
                        modifier = modifier,
                        viewModel = viewModel
                    )
                } else {
                    TwoColumnDisplay(
                        useCases = useCases,
                        state = state,
                        viewModel = viewModel
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
                    viewModel.onEvent(AddEditScorecardUiEvent.SaveScorecard)
                }, enabled = viewModel.isValid) {
                    Text(text = stringResource(Res.string.save))
                }

                Button(onClick = onSaveOrCancel) {
                    Text(text = stringResource(Res.string.cancel))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

    }
}

@Composable
fun TwoColumnDisplay(
    modifier: Modifier = Modifier,
    state: AddEditScorecardState,
    useCases: AddEditScorecardUseCases,
    viewModel: AddEditScorecardViewModel
) {
    val scorecard = viewModel.state.value
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


            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.teamName,
                labelId = Res.string.team_label,
                isError = viewModel.isEmpty(
                    viewModel.state.value.teamName,
                    viewModel.state.value.teamNameChanged
                ),
                errorMessageId = Res.string.missing_team_name,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredTeamName(it))
                },
            )

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

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.opponentsName,
                labelId = Res.string.opponents_label,
                isError = viewModel.isEmpty(
                    viewModel.state.value.opponentsName,
                    viewModel.state.value.opponentsNameChanged
                ),
                errorMessageId = Res.string.missing_opponents_name,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredOpponentsName(it))
                },
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.venue,
                labelId = Res.string.venue_label,
                isError = viewModel.isEmpty(
                    viewModel.state.value.venue,
                    viewModel.state.value.venueChanged
                ),
                errorMessageId = Res.string.missing_venue,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredVenue(it))
                },
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.title,
                labelId = Res.string.title_label,
                isError = viewModel.isEmpty(
                    viewModel.state.value.title,
                    viewModel.state.value.titleChanged
                ),
                errorMessageId = Res.string.missing_match_title,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredTitle(it))
                },
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.matchDate,
                labelId = Res.string.date_label,
                isError = viewModel.isEmpty(
                    viewModel.state.value.matchDate,
                    viewModel.state.value.matchDateChanged
                ),
                errorMessageId = Res.string.missing_date,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredDate(it))
                },
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.battingSide,
                labelId = Res.string.batting_side_label,
                isError = viewModel.isEmpty(
                    viewModel.state.value.battingSide,
                    viewModel.state.value.battingSideChanged
                ) || !(viewModel.state.value.battingSide == viewModel.state.value.teamName
                        || viewModel.state.value.battingSide == viewModel.state.value.opponentsName),
                errorMessageId = Res.string.invalid_batting_side_name,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredBattingSide(it))
                },
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.umpire1Name ?: "",
                labelId = Res.string.umpire1_label,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredUmpire1Name(it))
                },
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.umpire2Name ?: "",
                labelId = Res.string.umpire2_label,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredUmpire2Name(it))
                },
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.thirdUmpireName ?: "",
                labelId = Res.string.third_umpire_label,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredThirdUmpireName(it))
                },
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.refereeName ?: "",
                labelId = Res.string.referee_label,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredReferee(it))
                },
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.scorer1Name,
                labelId = Res.string.scorer1_label,
                isError = viewModel.isEmpty(
                    "viewModel.scorecard.value.scorer1Name",
                    viewModel.state.value.scorer1NameChanged
                ),
                errorMessageId = Res.string.missing_scorer,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredScorer1Name(it))
                },
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.scorer2Name ?: "",
                labelId = Res.string.scorer2_label,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredScorer2Name(it))
                },
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.typeOfMatch,
                labelId = Res.string.type_of_match_label,
                isError = viewModel.isEmpty(
                    "viewModel.scorecard.value.typeOfMatch",
                    viewModel.state.value.typeOfMatchChanged
                ),
                errorMessageId = Res.string.missing_type_of_match,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredTypeOfMatch(it))
                },
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.duration,
                labelId = Res.string.duration_label,
                isError = viewModel.isEmpty(
                    "viewModel.scorecard.value.duration",
                    viewModel.state.value.durationChanged
                ),
                errorMessageId = Res.string.missing_duration,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredDuration(it))
                },
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.startTime,
                labelId = Res.string.start_time_label,
                isError = viewModel.isEmpty(
                    viewModel.state.value.startTime,
                    viewModel.state.value.startTimeChanged
                ),
                errorMessageId = Res.string.missing_start_time,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredStartTime(it))
                },
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.teamWinningToss,
                labelId = Res.string.team_winning_toss_label,
                isError = viewModel.isEmpty(
                    viewModel.state.value.teamWinningToss,
                    viewModel.state.value.teamWinningTossChanged
                ) || !(viewModel.state.value.teamWinningToss == viewModel.state.value.teamName
                        || viewModel.state.value.teamWinningToss == viewModel.state.value.opponentsName),
                errorMessageId = Res.string.invalid_team_winning_toss_name,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredTeamWinningToss(it))
                },
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.weather ?: "",
                labelId = Res.string.weather_label,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredWeather(it))
                },
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.pitchCondition ?: "",
                labelId = Res.string.pitch_conditions_label,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredPitchCondition(it))
                },
            )
        }

    }
}


@Composable
private fun SingleColumnDisplay(
    modifier: Modifier = Modifier,
    state: AddEditScorecardState,
    useCases: AddEditScorecardUseCases,
    viewModel: AddEditScorecardViewModel
) {
    val scorecard = viewModel.state.value
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
            value = scorecard.teamName,
            labelId = Res.string.team_label,
            isError = result.successful,
            errorMessage = result.errorMessage,
            errorMessageId = Res.string.missing_team_name,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredTeamName(it))
            })
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateTeamName(state.opponentsName)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.opponentsName,
            labelId = Res.string.opponents_label,
            isError = result.successful,
            errorMessage = result.errorMessage,
            errorMessageId = Res.string.missing_opponents_name,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredOpponentsName(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateVenue(state.venue)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.venue,
            labelId = Res.string.venue_label,
            isError = result.successful,
            errorMessage = result.errorMessage,
            errorMessageId = Res.string.missing_venue,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredVenue(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateTitle(state.title)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.title,
            labelId = Res.string.title_label,
            isError = result.successful,
            errorMessage = result.errorMessage,
            errorMessageId = Res.string.missing_match_title,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredTitle(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateMatchDate(state.matchDate)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.matchDate,
            labelId = Res.string.date_label,
            isError = result.successful,
            errorMessage = result.errorMessage,
            errorMessageId = Res.string.missing_date,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredDate(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateBattingSide(state.battingSide, teamName = state.teamName, opponentsName = state.opponentsName)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.battingSide,
            labelId = Res.string.batting_side_label,
            isError = result.successful,
            errorMessage = result.errorMessage,
            errorMessageId = Res.string.invalid_batting_side_name,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredBattingSide(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.umpire1Name ?: "",
            labelId = Res.string.umpire1_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredUmpire1Name(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.umpire2Name ?: "",
            labelId = Res.string.umpire2_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredUmpire2Name(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.thirdUmpireName ?: "",
            labelId = Res.string.third_umpire_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredThirdUmpireName(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.refereeName ?: "",
            labelId = Res.string.referee_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredReferee(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateScorer(state.scorer1Name)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.scorer1Name,
            labelId = Res.string.scorer1_label,
            isError = result.successful,
            errorMessage = result.errorMessage,
            errorMessageId = Res.string.missing_scorer,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredScorer1Name(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.scorer2Name ?: "",
            labelId = Res.string.scorer2_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredScorer2Name(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateMatchLabel(state.typeOfMatch)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.typeOfMatch,
            labelId = Res.string.type_of_match_label,
            isError = result.successful,
            errorMessage = result.errorMessage,
            errorMessageId = Res.string.missing_type_of_match,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredTypeOfMatch(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateDuration(state.duration)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.duration,
            labelId = Res.string.duration_label,
            isError = result.successful,
            errorMessage = result.errorMessage,
            errorMessageId = Res.string.missing_duration,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredDuration(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateStartTime(state.startTime)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.startTime,
            labelId = Res.string.start_time_label,
            isError = result.successful,
            errorMessage = result.errorMessage,
            errorMessageId = Res.string.missing_start_time,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredStartTime(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        result = useCases.validateTeamWinningToss(state.teamWinningToss, state.teamName, state.opponentsName)
        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.teamWinningToss,
            labelId = Res.string.team_winning_toss_label,
            isError = result.successful,
            errorMessage = result.errorMessage,
            errorMessageId = Res.string.invalid_team_winning_toss_name,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredTeamWinningToss(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.weather ?: "",
            labelId = Res.string.weather_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredWeather(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.pitchCondition ?: "",
            labelId = Res.string.pitch_conditions_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredPitchCondition(it))
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
    errorMessageId: StringResource? = null,
    onValueChange: (String) -> Unit,
    errorMessage: UiText? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
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
        if (isError && errorMessageId != null) {
            Text(
                text = errorMessage?.asString() ?: stringResource(errorMessageId),
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
