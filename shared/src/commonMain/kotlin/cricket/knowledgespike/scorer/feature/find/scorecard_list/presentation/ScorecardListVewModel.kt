package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.usecase.ListScorecardUseCases
import cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.usecase.SortByDate
import cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.usecase.SortOrder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ScorecardListVewModel(
    private val useCases: ListScorecardUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(ScorecardListState())
    val state = _state.asStateFlow()

    private var _sortOrder: MutableState<SortOrder> = mutableStateOf(SortByDate)
    var sortOrder: State<SortOrder> = _sortOrder

    var job: Job? = null

    init {
        loadScorecards(sortOrder.value)
    }


    fun onAction(action: ScorecardListAction) {
        when(action) {
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

    fun onEvent(event: ListScorecardEvent) {
        when (event) {
            is ListScorecardEvent.Order -> {
                _sortOrder.value = event.order
                loadScorecards(event.order)
            }
        }
    }

    private fun loadScorecards(sortOrder: SortOrder) {
        job?.cancel()

        job = useCases.getScorecards(sortOrder).onEach { scorecards ->

            _state.update { it.copy(scorecards = scorecards) }


        }.launchIn(viewModelScope)
    }

}