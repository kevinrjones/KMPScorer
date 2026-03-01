package cricket.knowledgespike.scorer.add_edit_scorecard.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

data class FieldChanges(
    val teamNameChanged: Boolean = false,
    val opponentsNameChanged: Boolean = false,
    val dateChanged: Boolean = false,
    val venueChanged: Boolean = false,
    val titleChanged: Boolean = false,
    val matchDateChanged: Boolean = false,
    val battingSideChanged: Boolean = false,
    val scorer1NameChanged: Boolean = false,
    val durationChanged: Boolean = false,
    val typeOfMatchChanged: Boolean = false,
    val startTimeChanged: Boolean = false,
    val teamWinningTossChanged: Boolean = false,
)

class AddEditScorecardViewModel(
    savedStateHandle: SavedStateHandle?
) : ViewModel() {

    private val _scorecard = mutableStateOf(
        VmScorecard()
    )
    val scorecard: State<VmScorecard> = _scorecard

    private val _eventFlow = MutableSharedFlow<AddEditScorecardEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var fieldChanges = FieldChanges()

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
                fieldChanges = fieldChanges.copy(teamNameChanged = true)
            }

            is AddEditScorecardUiEvent.EnteredOpponentsName -> {
                fieldChanges = fieldChanges.copy(opponentsNameChanged = true)
            }

            is AddEditScorecardUiEvent.EnteredDate -> {
                fieldChanges = fieldChanges.copy(dateChanged = true)
            }

            is AddEditScorecardUiEvent.EnteredBattingSide -> {
                fieldChanges = fieldChanges.copy(battingSideChanged = true)
            }

            is AddEditScorecardUiEvent.EnteredPitchCondition -> Unit

            is AddEditScorecardUiEvent.EnteredReferee -> Unit

            is AddEditScorecardUiEvent.EnteredScorer1Name -> {
                fieldChanges = fieldChanges.copy(scorer1NameChanged = true)
            }

            is AddEditScorecardUiEvent.EnteredScorer2Name -> Unit

            is AddEditScorecardUiEvent.EnteredStartTime -> {
                fieldChanges = fieldChanges.copy(startTimeChanged = true)
            }

            is AddEditScorecardUiEvent.EnteredTeamWinningToss -> {
                fieldChanges = fieldChanges.copy(teamWinningTossChanged = true)
            }

            is AddEditScorecardUiEvent.EnteredThirdUmpireName -> Unit

            is AddEditScorecardUiEvent.EnteredTitle -> {
                fieldChanges = fieldChanges.copy(titleChanged = true)
            }

            is AddEditScorecardUiEvent.EnteredTypeOfMatch -> {
                fieldChanges = fieldChanges.copy(typeOfMatchChanged = true)
            }

            is AddEditScorecardUiEvent.EnteredUmpire1Name ->  Unit

            is AddEditScorecardUiEvent.EnteredUmpire2Name ->  Unit

            is AddEditScorecardUiEvent.EnteredVenue -> {
                fieldChanges = fieldChanges.copy(venueChanged = true)
            }

            is AddEditScorecardUiEvent.EnteredWeather ->  Unit
            is AddEditScorecardUiEvent.EnteredDuration -> {
                fieldChanges = fieldChanges.copy(durationChanged = true)
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


data class VmScorecard(
    val id: Int? = null,
    val teamName: String = "",
    val opponentsName: String = "",
    val venue: String = "",
    val title: String = "",
    val matchDate: String = "",
    val battingSide: String = "",
    val umpire1Name: String? = null,
    val umpire2Name: String? = null,
    val thirdUmpireName: String? = null,
    val refereeName: String? = null,
    val scorer1Name: String = "",
    val scorer2Name: String? = null,
    val typeOfMatch: String = "",
    val duration: String = "",
    val startTime: String = "",
    val teamWinningToss: String = "",
    val weather: String? = null,
    val pitchCondition: String? = "",
)
