package cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.usecase

import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.repository.AddEditScorecardRepository

class GetScorecardUseCase(private val repository: AddEditScorecardRepository) {
    suspend operator fun invoke(id: Int) =
        repository.getScorecard(id)

}