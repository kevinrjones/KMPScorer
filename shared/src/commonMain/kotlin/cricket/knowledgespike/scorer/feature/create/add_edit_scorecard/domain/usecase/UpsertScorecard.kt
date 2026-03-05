package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.repository.AddEditScorecardRepository

class UpsertScorecard(private val repository: AddEditScorecardRepository) {

    suspend operator fun invoke(scorecard: ScorecardHeaderDetails) {
        repository.upsertScorecard(scorecard)
    }
}
