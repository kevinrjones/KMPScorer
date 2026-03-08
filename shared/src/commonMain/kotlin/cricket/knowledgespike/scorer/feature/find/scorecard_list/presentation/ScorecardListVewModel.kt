package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cricket.knowledgespike.scorer.domain.usecase.create_scorecard.ListScorecardUseCases
import cricket.knowledgespike.scorer.domain.usecase.create_scorecard.SortByDate
import cricket.knowledgespike.scorer.domain.usecase.create_scorecard.SortOrder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScorecardListVewModel(
    private val useCases: ListScorecardUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(ScorecardListState())
    val state = _state.asStateFlow()

    private val _listScorecardEvent = MutableSharedFlow<ListScorecardEvent>()
    val listScorecardEvent = _listScorecardEvent.asSharedFlow()

    private var _sortOrder: MutableState<SortOrder> = mutableStateOf(        SortByDate
    )
    var sortOrder: State<SortOrder> = _sortOrder

    var job: Job? = null

    init {
        loadScorecards(sortOrder.value)
    }

    fun updateExpandedState(selected: ScorecardDetailsView, newState: Boolean) {
        _state.update { state ->
            state.copy(scorecards = state.scorecards.map {
                if (it == selected) {
                    it.copy(isExpanded = newState)
                } else {
                    it
                }
            })
        }
    }


    fun onAction(action: ScorecardListAction) {
        when (action) {
            is ScorecardListAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        teamSearchName = action.team,
                        opponentsSearchName = action.opponents
                    )
                }
            }

            is ScorecardListAction.onAddOrEditScorecard -> Unit
            is ScorecardListAction.onScore -> Unit
        }
    }

    fun onEvent(event: ListScorecardUiEvent) {
        when (event) {
            is ListScorecardUiEvent.Order -> {
                _sortOrder.value = event.order
                loadScorecards(event.order)
            }

            is ListScorecardUiEvent.TryDelete -> {
                viewModelScope.launch {
                    if (useCases.deleteScorecard(event.scorecard)) {
                        _listScorecardEvent.emit(ListScorecardEvent.Deleted(event.scorecard))
                    } else {
                        _listScorecardEvent.emit(ListScorecardEvent.ErrorDeletingScorecard)
                    }
                }
            }
        }
    }

    private fun loadScorecards(sortOrder: SortOrder) {
        job?.cancel()

        job = useCases.getScorecards(sortOrder).onEach { scorecards ->
            _state.update {
                it.copy(scorecards = scorecards.map {
                    ScorecardDetailsView(it, false)
                })
            }


        }.launchIn(viewModelScope)
    }

}