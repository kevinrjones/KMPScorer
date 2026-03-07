package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.data.repository.AddEditScorecardRepositoryImpl
import cricket.knowledgespike.scorer.domain.repository.AddEditScorecardRepository
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.presentation.AddEditScorecardViewModel
import cricket.knowledgespike.scorer.domain.usecase.create_scorecard.GetScorecardUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val addEditModule = module {
    singleOf(::ValidateTeamName)
    singleOf(::ValidateOpponentsName)
    singleOf(::ValidateVenue)
    singleOf(::ValidateTitle)
    singleOf(::ValidateScorer)
    singleOf(::ValidateMatchLabel)
    singleOf(::ValidateDuration)
    singleOf(::ValidateStartTime)
    singleOf(::ValidateTeamWinningToss)
    singleOf(::ValidateMatchDate)
    singleOf(::ValidateBattingSide)

    singleOf(::UpsertScorecard)
    singleOf(::GetScorecardUseCase)
    singleOf(::AddEditScorecardUseCases)

    singleOf(::AddEditScorecardRepositoryImpl).bind<AddEditScorecardRepository>()

    viewModelOf(::AddEditScorecardViewModel)
}