package cricket.knowledgespike.scorer.feature.find.match_list.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MatchListVewModel : ViewModel() {
    private val _state = MutableStateFlow(MatchListState())
    val state = _state.asStateFlow()


    fun onAction(action: MatchListAction) {
        when(action) {
            is MatchListAction.OnMatchClick -> {

            }
            is MatchListAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        teamSearchName = action.team,
                        opponentsSearchName = action.opponents
                    )
                }
            }
        }
    }

}