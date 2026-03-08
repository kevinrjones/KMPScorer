package cricket.knowledgespike.scorer.domain.usecase.create_scorecard

import cricket.knowledgespike.scorer.domain.repository.ListScorecardRepository
import kotlinx.coroutines.flow.map

class GetScorecardsUseCase(private val repository: ListScorecardRepository) {
    // todo: getRemoteScorecards
    operator fun invoke(orderBy: SortOrder = SortByTeam) =
        repository.getScorecards()
            .map { scorecards ->
                when (orderBy) {
                    SortByTeam -> scorecards.sortedBy { it.teamName }
                    SortByOpponents -> scorecards.sortedBy { it.opponentsName }
                    SortByDate -> scorecards.sortedBy { it.matchDate }
                }
            }


}

