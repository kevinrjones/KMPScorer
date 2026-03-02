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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cricket.knowledgespike.scorer.feature.find.match_list.presentation.MatchListVewModel
import cricket.knowledgespike.scorer.ui.theme.ScorerTheme
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
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddEditScorecardScreenRoot(
    viewModel: AddEditScorecardViewModel = koinViewModel<AddEditScorecardViewModel>(),
    isExpandedScreen: Boolean = false,
    onSaveOrCancel: () -> Unit,
) {
    AddEditScorecardScreen(
        isExpandedScreen = isExpandedScreen,
        onSaveOrCancel = onSaveOrCancel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScorecardScreen(
    modifier: Modifier = Modifier,
    isExpandedScreen: Boolean = false,
    onSaveOrCancel: () -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val savedMessage = stringResource(Res.string.saved_scorecard)
    val unableToSaveScorecardMessage = stringResource(Res.string.unable_to_save_scorecard)
    val viewModel = AddEditScorecardViewModel(null)

    LaunchedEffect(true) {
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
                        modifier = modifier,
                        viewModel = viewModel
                    )
                } else {
                    TwoColumnDisplay(viewModel = viewModel)
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
    viewModel: AddEditScorecardViewModel
) {
    val scorecard = viewModel.scorecard.value
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
                    viewModel.scorecard.value.teamName,
                    viewModel.fieldChanges.teamNameChanged
                ),
                errorMessageId = Res.string.missing_team_name,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredTeamName(it))
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

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.opponentsName,
                labelId = Res.string.opponents_label,
                isError = viewModel.isEmpty(
                    viewModel.scorecard.value.opponentsName,
                    viewModel.fieldChanges.opponentsNameChanged
                ),
                errorMessageId = Res.string.missing_opponents_name,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredOpponentsName(it))
                }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.venue,
                labelId = Res.string.venue_label,
                isError = viewModel.isEmpty(
                    viewModel.scorecard.value.venue,
                    viewModel.fieldChanges.venueChanged
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
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredTitle(it))
                },
                isError = viewModel.isEmpty(
                    viewModel.scorecard.value.title,
                    viewModel.fieldChanges.titleChanged
                ),
                errorMessageId = Res.string.missing_match_title
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.matchDate,
                labelId = Res.string.date_label,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredDate(it))
                },
                isError = viewModel.isEmpty(
                    viewModel.scorecard.value.matchDate,
                    viewModel.fieldChanges.dateChanged
                ),
                errorMessageId = Res.string.missing_date
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                labelId = Res.string.batting_side_label,
                value = scorecard.battingSide,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredBattingSide(it))
                },
                isError = viewModel.isEmpty(
                    viewModel.scorecard.value.battingSide,
                    viewModel.fieldChanges.battingSideChanged
                ) || !(viewModel.scorecard.value.battingSide == viewModel.scorecard.value.teamName
                        || viewModel.scorecard.value.battingSide == viewModel.scorecard.value.opponentsName),
                errorMessageId = Res.string.invalid_batting_side_name
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
                }
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.umpire2Name ?: "",
                labelId = Res.string.umpire2_label,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredUmpire2Name(it))
                }
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
                }
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.refereeName ?: "",
                labelId = Res.string.referee_label,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredReferee(it))
                }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.scorer1Name,
                labelId = Res.string.scorer1_label,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredScorer1Name(it))
                },
                isError = viewModel.isEmpty(
                    "viewModel.scorecard.value.scorer1Name",
                    viewModel.fieldChanges.scorer1NameChanged
                ),
                errorMessageId = Res.string.missing_scorer
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                value = scorecard.scorer2Name ?: "",
                labelId = Res.string.scorer2_label,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredScorer2Name(it))
                }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                labelId = Res.string.type_of_match_label,
                value = scorecard.typeOfMatch,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredTypeOfMatch(it))
                },
                isError = viewModel.isEmpty(
                    "viewModel.scorecard.value.typeOfMatch",
                    viewModel.fieldChanges.typeOfMatchChanged
                ),
                errorMessageId = Res.string.missing_type_of_match
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                labelId = Res.string.duration_label,
                value = scorecard.duration,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredDuration(it))
                },
                isError = viewModel.isEmpty(
                    "viewModel.scorecard.value.duration",
                    viewModel.fieldChanges.durationChanged
                ),
                errorMessageId = Res.string.missing_duration
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                labelId = Res.string.start_time_label,
                value = scorecard.startTime,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredStartTime(it))
                },
                isError = viewModel.isEmpty(
                    viewModel.scorecard.value.startTime,
                    viewModel.fieldChanges.startTimeChanged
                ),
                errorMessageId = Res.string.missing_start_time
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                labelId = Res.string.team_winning_toss_label,
                value = scorecard.teamWinningToss,
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredTeamWinningToss(it))
                },
                isError = viewModel.isEmpty(
                    viewModel.scorecard.value.teamWinningToss,
                    viewModel.fieldChanges.teamWinningTossChanged
                ) || !(viewModel.scorecard.value.teamWinningToss == viewModel.scorecard.value.teamName
                        || viewModel.scorecard.value.teamWinningToss == viewModel.scorecard.value.opponentsName),
                errorMessageId = Res.string.invalid_team_winning_toss_name
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                labelId = Res.string.weather_label,
                value = scorecard.weather ?: "",
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredWeather(it))
                }
            )
            Spacer(modifier = Modifier.width(24.dp))

            AddEditScorecardScreenField(
                modifier = Modifier.weight(0.5f),
                labelId = Res.string.pitch_conditions_label,
                value = scorecard.pitchCondition ?: "",
                onValueChange = {
                    viewModel.onEvent(AddEditScorecardUiEvent.EnteredPitchCondition(it))
                }
            )
        }

    }
}


@Composable
private fun SingleColumnDisplay(
    modifier: Modifier = Modifier,
    viewModel: AddEditScorecardViewModel
) {
    val scorecard = viewModel.scorecard.value
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(start = 16.dp, end = 16.dp),

        ) {

        Spacer(modifier = Modifier.height(8.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.teamName,
            labelId = Res.string.team_label,
            isError = viewModel.isEmpty(
                viewModel.scorecard.value.teamName,
                viewModel.fieldChanges.teamNameChanged
            ),
            errorMessageId = Res.string.missing_team_name,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredTeamName(it))
            })
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.opponentsName,
            labelId = Res.string.opponents_label,
            isError = viewModel.isEmpty(
                viewModel.scorecard.value.opponentsName,
                viewModel.fieldChanges.opponentsNameChanged
            ),
            errorMessageId = Res.string.missing_opponents_name,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredOpponentsName(it))
            }
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.venue,
            labelId = Res.string.venue_label,
            isError = viewModel.isEmpty(
                viewModel.scorecard.value.venue,
                viewModel.fieldChanges.venueChanged
            ),
            errorMessageId = Res.string.missing_venue,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredVenue(it))
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.title,
            labelId = Res.string.title_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredTitle(it))
            },
            isError = viewModel.isEmpty(
                viewModel.scorecard.value.title,
                viewModel.fieldChanges.titleChanged
            ),
            errorMessageId = Res.string.missing_match_title
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.matchDate,
            labelId = Res.string.date_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredDate(it))
            },
            isError = viewModel.isEmpty(
                viewModel.scorecard.value.matchDate,
                viewModel.fieldChanges.dateChanged
            ),
            errorMessageId = Res.string.missing_date
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            labelId = Res.string.batting_side_label,
            value = scorecard.battingSide,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredBattingSide(it))
            },
            isError = viewModel.isEmpty(
                viewModel.scorecard.value.battingSide,
                viewModel.fieldChanges.battingSideChanged
            ) || !(viewModel.scorecard.value.battingSide == viewModel.scorecard.value.teamName
                    || viewModel.scorecard.value.battingSide == viewModel.scorecard.value.opponentsName),
            errorMessageId = Res.string.invalid_batting_side_name
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.umpire1Name ?: "",
            labelId = Res.string.umpire1_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredUmpire1Name(it))
            }
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.umpire2Name ?: "",
            labelId = Res.string.umpire2_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredUmpire2Name(it))
            }
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.thirdUmpireName ?: "",
            labelId = Res.string.third_umpire_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredThirdUmpireName(it))
            }
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.refereeName ?: "",
            labelId = Res.string.referee_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredReferee(it))
            }
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.scorer1Name,
            labelId = Res.string.scorer1_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredScorer1Name(it))
            },
            isError = viewModel.isEmpty(
                viewModel.scorecard.value.scorer1Name,
                viewModel.fieldChanges.scorer1NameChanged
            ),
            errorMessageId = Res.string.missing_scorer
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            value = scorecard.scorer2Name ?: "",
            labelId = Res.string.scorer2_label,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredScorer2Name(it))
            }
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            labelId = Res.string.type_of_match_label,
            value = scorecard.typeOfMatch,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredTypeOfMatch(it))
            },
            isError = viewModel.isEmpty(
                viewModel.scorecard.value.typeOfMatch,
                viewModel.fieldChanges.typeOfMatchChanged
            ),
            errorMessageId = Res.string.missing_type_of_match
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            labelId = Res.string.duration_label,
            value = scorecard.duration,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredDuration(it))
            },
            isError = viewModel.isEmpty(
                viewModel.scorecard.value.duration,
                viewModel.fieldChanges.durationChanged
            ),
            errorMessageId = Res.string.missing_duration
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            labelId = Res.string.start_time_label,
            value = scorecard.startTime,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredStartTime(it))
            },
            isError = viewModel.isEmpty(
                viewModel.scorecard.value.startTime,
                viewModel.fieldChanges.startTimeChanged
            ),
            errorMessageId = Res.string.missing_start_time
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            labelId = Res.string.team_winning_toss_label,
            value = scorecard.teamWinningToss,
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredTeamWinningToss(it))
            },
            isError = viewModel.isEmpty(
                viewModel.scorecard.value.teamWinningToss,
                viewModel.fieldChanges.teamWinningTossChanged
            ) || !(viewModel.scorecard.value.teamWinningToss == viewModel.scorecard.value.teamName
                    || viewModel.scorecard.value.teamWinningToss == viewModel.scorecard.value.opponentsName),
            errorMessageId = Res.string.invalid_team_winning_toss_name
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            labelId = Res.string.weather_label,
            value = scorecard.weather ?: "",
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredWeather(it))
            }
        )
        Spacer(modifier = Modifier.height(4.dp))

        AddEditScorecardScreenField(
            modifier = Modifier.fillMaxWidth(),
            labelId = Res.string.pitch_conditions_label,
            value = scorecard.pitchCondition ?: "",
            onValueChange = {
                viewModel.onEvent(AddEditScorecardUiEvent.EnteredPitchCondition(it))
            }
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
    onValueChange: (String) -> Unit
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
                text = stringResource(errorMessageId),
                color = MaterialTheme.colorScheme.error,
                fontSize = 16.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddEditScorecardScreenPreview() {
    ScorerTheme { Surface { AddEditScorecardScreen(modifier = Modifier, onSaveOrCancel = {}) } }
}
