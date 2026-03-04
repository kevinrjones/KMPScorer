package cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScorecardListVewModel : ViewModel() {
    private val _state = MutableStateFlow(ScorecardListState())
    val state = _state.asStateFlow()


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
}