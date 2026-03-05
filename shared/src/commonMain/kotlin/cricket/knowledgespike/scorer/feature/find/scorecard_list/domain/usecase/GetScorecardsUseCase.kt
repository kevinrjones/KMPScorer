package cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.usecase

import cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.repository.ListScorecardRepository

class GetScorecardsUseCase(private val repository: ListScorecardRepository) {
    // todo: getRemoteScorecardsv
    operator fun invoke(sortOrder: SortOrder) =
        repository.getScorecards()

}

