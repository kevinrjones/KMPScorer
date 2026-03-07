package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.domain.model.ScorecardHeaderDetails
import cricket.knowledgespike.scorer.domain.repository.AddEditScorecardRepository

class UpsertScorecard(private val repository: AddEditScorecardRepository) {

    suspend operator fun invoke(scorecard: ScorecardHeaderDetails) {
        repository.upsertScorecard(scorecard)
    }
}
