package cricket.knowledgespike.scorer.di

import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.data.repository.AddEditScorecardRepositoryImpl
import cricket.knowledgespike.scorer.domain.repository.AddEditScorecardRepository
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.AddEditScorecardUseCases
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.UpsertScorecard
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateBattingSide
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateDuration
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateMatchDate
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateMatchLabel
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateOpponentsName
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateScorer
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateStartTime
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateTeamName
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateTeamWinningToss
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateTitle
import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.ValidateVenue
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