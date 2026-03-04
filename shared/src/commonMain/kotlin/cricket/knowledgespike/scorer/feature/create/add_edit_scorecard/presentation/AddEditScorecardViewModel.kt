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

    private val _eventFlow = MutableSharedFlow<AddEditScorecardEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        val scorecardId = savedStateHandle?.get<Int>("scorecardId")
        findScorecard(scorecardId)
    }

    private fun findScorecard(scorecardId: Int?) {
    }

    val isValid: Boolean
        get() = true


    fun onEvent(event: AddEditScorecardUiEvent) {
        when (event) {

            AddEditScorecardUiEvent.SaveScorecard -> {
                viewModelScope.launch {
                    // todo: log the issues here - need to add analytics
                }
            }

            is AddEditScorecardUiEvent.EnteredTeamName -> {
                _state.update { it.copy(teamNameChanged = true) }
            }

            is AddEditScorecardUiEvent.EnteredOpponentsName -> {
                _state.update { it.copy(opponentsNameChanged = true) }
            }

            is AddEditScorecardUiEvent.EnteredDate -> {
                _state.update { it.copy(matchDateChanged = true) }
            }

            is AddEditScorecardUiEvent.EnteredBattingSide -> {
                _state.update { it.copy(battingSideChanged = true) }
            }

            is AddEditScorecardUiEvent.EnteredPitchCondition -> Unit

            is AddEditScorecardUiEvent.EnteredReferee -> Unit

            is AddEditScorecardUiEvent.EnteredScorer1Name -> {
                _state.update { it.copy(scorer1NameChanged = true) }
            }

            is AddEditScorecardUiEvent.EnteredScorer2Name -> Unit

            is AddEditScorecardUiEvent.EnteredStartTime -> {
                _state.update { it.copy(startTimeChanged = true) }
            }

            is AddEditScorecardUiEvent.EnteredTeamWinningToss -> {
                _state.update { it.copy(teamWinningTossChanged = true) }
            }

            is AddEditScorecardUiEvent.EnteredThirdUmpireName -> Unit

            is AddEditScorecardUiEvent.EnteredTitle -> {
                _state.update { it.copy(titleChanged = true) }
            }

            is AddEditScorecardUiEvent.EnteredTypeOfMatch -> {
                _state.update { it.copy(typeOfMatchChanged = true) }
            }

            is AddEditScorecardUiEvent.EnteredUmpire1Name -> Unit

            is AddEditScorecardUiEvent.EnteredUmpire2Name -> Unit

            is AddEditScorecardUiEvent.EnteredVenue -> {
                _state.update { it.copy(venueChanged = true) }
            }

            is AddEditScorecardUiEvent.EnteredWeather -> Unit
            is AddEditScorecardUiEvent.EnteredDuration -> {
                _state.update { it.copy(durationChanged = true) }
            }
        }
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


