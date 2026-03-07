package cricket.knowledgespike.scorer.domain.usecase.create_scorecard

import cricket.knowledgespike.scorer.domain.model.ScorecardHeaderDetails
import cricket.knowledgespike.scorer.domain.repository.ListScorecardRepository

class DeleteScorecardUseCase(private val repository: ListScorecardRepository) {
    suspend operator fun invoke(scorecard: ScorecardHeaderDetails): Boolean {
        val result = repository.deleteScorecard(scorecard)
        return if(result.isRight())
            true
        else {
            // todo: log
            false
        }
    }

}