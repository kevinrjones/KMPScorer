package cricket.knowledgespike.scorer.di

import cricket.knowledgespike.scorer.domain.repository.ListScorecardRepository
import cricket.knowledgespike.scorer.domain.usecase.create_scorecard.DeleteScorecardUseCase
import cricket.knowledgespike.scorer.domain.usecase.create_scorecard.GetScorecardsUseCase
import cricket.knowledgespike.scorer.domain.usecase.create_scorecard.ListScorecardUseCases
import cricket.knowledgespike.scorer.feature.find.scorecard_list.data.repository.ListScorecardRepositoryImpl
import cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation.ScorecardListVewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val scorecardListEditModule = module {
    singleOf(::ListScorecardUseCases)
    singleOf(::GetScorecardsUseCase)
    singleOf(::DeleteScorecardUseCase)

    singleOf(::ListScorecardRepositoryImpl).bind<ListScorecardRepository>()

    viewModelOf(::ScorecardListVewModel)
}