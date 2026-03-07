package cricket.knowledgespike.scorer.domain.usecase.create_scorecard

import cricket.knowledgespike.scorer.domain.repository.AddEditScorecardRepository

class GetScorecardUseCase(private val repository: AddEditScorecardRepository) {
    suspend operator fun invoke(id: Int) =
        repository.getScorecard(id)

}