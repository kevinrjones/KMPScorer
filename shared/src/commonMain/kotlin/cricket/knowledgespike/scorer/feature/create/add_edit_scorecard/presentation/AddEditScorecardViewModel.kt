package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.AddEditScorecardUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AddEditScorecardViewModel(
    savedStateHandle: SavedStateHandle?,
    val addEditScorecardUseCases: AddEditScorecardUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditScorecardState())
    val state = _state.asStateFlow()

    private val _addEditScoorecardEvent = MutableSharedFlow<AddEditScorecardEvent>()
    val addEditScoorecardEvent = _addEditScoorecardEvent.asSharedFlow()


    init {
        val scorecardId = savedStateHandle?.get<Int>("scorecardId")
        findScorecard(scorecardId)
    }

    private fun findScorecard(scorecardId: Int?) {
    }

    val isValid: Boolean
        get() = isScorecardValid()


    fun onEvent(event: AddEditScorecardUiEvent) {
        when (event) {

            AddEditScorecardUiEvent.SaveScorecard -> {
                viewModelScope.launch {
                    if (isScorecardValid()) {
                        addEditScorecardUseCases.upsertScorecard(scorecard = state.value.toScoreCardFullDetails())
                        _addEditScoorecardEvent.emit(AddEditScorecardEvent.SavedScorecard)
                    } else {
                        _addEditScoorecardEvent.emit(AddEditScorecardEvent.ErrorSavingScorecard)
                    }
                }
            }

            is AddEditScorecardUiEvent.EnteredTeamName -> {
                        _state.update { it.copy(teamNameChanged = true, teamName = event.teamName) }
            }

            is AddEditScorecardUiEvent.EnteredOpponentsName -> {
                _state.update {
                    it.copy(
                        opponentsNameChanged = true,
                        opponentsName = event.opponentsName
                    )
                }
            }

            is AddEditScorecardUiEvent.EnteredDate -> {
                _state.update { it.copy(matchDateChanged = true, matchDate = event.matchDate) }
            }

            is AddEditScorecardUiEvent.EnteredBattingSide -> {
                _state.update {
                    it.copy(
                        battingSideChanged = true,
                        battingSide = event.battingSide
                    )
                }
            }

            is AddEditScorecardUiEvent.EnteredPitchCondition ->
                _state.update { it.copy(pitchCondition = event.pitchCondition) }

            is AddEditScorecardUiEvent.EnteredReferee ->
                _state.update { it.copy(refereeName = event.name) }

            is AddEditScorecardUiEvent.EnteredScorer1Name -> {
                _state.update { it.copy(scorer1NameChanged = true, scorer1Name = event.name) }
            }

            is AddEditScorecardUiEvent.EnteredScorer2Name -> _state.update { it.copy(scorer2Name = event.name) }

            is AddEditScorecardUiEvent.EnteredStartTime -> {
                _state.update { it.copy(startTimeChanged = true, startTime = event.startTime) }
            }

            is AddEditScorecardUiEvent.EnteredTeamWinningToss -> {
                _state.update {
                    it.copy(
                        teamWinningTossChanged = true,
                        teamWinningToss = event.teamWinningToss
                    )
                }
            }

            is AddEditScorecardUiEvent.EnteredThirdUmpireName -> _state.update {
                it.copy(
                    thirdUmpireName = event.name
                )
            }

            is AddEditScorecardUiEvent.EnteredTitle -> {
                _state.update { it.copy(titleChanged = true, title = event.title) }
            }

            is AddEditScorecardUiEvent.EnteredTypeOfMatch -> {
                _state.update {
                    it.copy(
                        typeOfMatchChanged = true,
                        typeOfMatch = event.typeOfMatch
                    )
                }
            }

            is AddEditScorecardUiEvent.EnteredUmpire1Name -> _state.update { it.copy(umpire1Name = event.name) }

            is AddEditScorecardUiEvent.EnteredUmpire2Name -> _state.update { it.copy(umpire2Name = event.name) }

            is AddEditScorecardUiEvent.EnteredVenue -> {
                _state.update { it.copy(venueChanged = true, venue = event.venue) }
            }

            is AddEditScorecardUiEvent.EnteredWeather -> _state.update { it.copy(weather = event.weather) }
            is AddEditScorecardUiEvent.EnteredDuration -> {
                _state.update { it.copy(durationChanged = true, duration = event.duration) }
            }
        }
    }

    private fun isScorecardValid(): Boolean {
        return addEditScorecardUseCases.validateTeamName(state.value.teamName).successful
                && addEditScorecardUseCases.validateTeamName(state.value.opponentsName).successful
    }

    fun isEmpty(value: String, changed: Boolean): Boolean {
        return value.isBlank() && changed
    }
}

sealed interface AddEditScorecardEvent {
    data object SavedScorecard : AddEditScorecardEvent
    data object ErrorSavingScorecard : AddEditScorecardEvent
}

sealed interface AddEditScorecardUiEvent {
    data class EnteredTeamName(val teamName: String) : AddEditScorecardUiEvent
    data class EnteredOpponentsName(val opponentsName: String) : AddEditScorecardUiEvent
    data class EnteredVenue(val venue: String) : AddEditScorecardUiEvent
    data class EnteredTitle(val title: String) : AddEditScorecardUiEvent
    data class EnteredDate(val matchDate: String) : AddEditScorecardUiEvent
    data class EnteredBattingSide(val battingSide: String) : AddEditScorecardUiEvent
    data class EnteredUmpire1Name(val name: String) : AddEditScorecardUiEvent
    data class EnteredUmpire2Name(val name: String) : AddEditScorecardUiEvent
    data class EnteredThirdUmpireName(val name: String) : AddEditScorecardUiEvent
    data class EnteredReferee(val name: String) : AddEditScorecardUiEvent
    data class EnteredScorer1Name(val name: String) : AddEditScorecardUiEvent
    data class EnteredScorer2Name(val name: String) : AddEditScorecardUiEvent
    data class EnteredDuration(val duration: String) : AddEditScorecardUiEvent
    data class EnteredTypeOfMatch(val typeOfMatch: String) : AddEditScorecardUiEvent
    data class EnteredStartTime(val startTime: String) : AddEditScorecardUiEvent
    data class EnteredTeamWinningToss(val teamWinningToss: String) : AddEditScorecardUiEvent
    data class EnteredWeather(val weather: String) : AddEditScorecardUiEvent
    data class EnteredPitchCondition(val pitchCondition: String) : AddEditScorecardUiEvent

    data object SaveScorecard : AddEditScorecardUiEvent
}


