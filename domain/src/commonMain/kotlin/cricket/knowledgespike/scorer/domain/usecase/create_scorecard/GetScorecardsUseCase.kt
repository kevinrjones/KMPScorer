package cricket.knowledgespike.scorer.domain.usecase.create_scorecard

import cricket.knowledgespike.scorer.domain.repository.ListScorecardRepository

class GetScorecardsUseCase(private val repository: ListScorecardRepository) {
    // todo: getRemoteScorecardsv
    operator fun invoke(sortOrder: cricket.knowledgespike.scorer.domain.usecase.create_scorecard.SortOrder) =
        repository.getScorecards()

}

