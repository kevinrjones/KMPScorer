package cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.usecase

import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails
import cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.repository.ListScorecardRepository

class DeleteScorecardUseCase(private val repository: ListScorecardRepository) {
    suspend operator fun invoke(scorecard: ScorecardHeaderDetails) =
        repository.deleteScorecard(scorecard)

}