package cricket.knowledgespike.scorer.di

import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.AddEditScorecardUseCases
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.UpsertScorecard
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.ValidateBattingSide
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.ValidateDuration
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.ValidateMatchDate
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.ValidateMatchLabel
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.ValidateOpponentsName
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.ValidateScorer
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.ValidateStartTime
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.ValidateTeamName
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.ValidateTeamWinningToss
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.ValidateTitle
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.ValidateVenue
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.presentation.AddEditScorecardViewModel
import cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation.ScorecardListVewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
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
//    singleOf(::UpsertScorecard)
    singleOf(::AddEditScorecardUseCases)
    viewModelOf(::AddEditScorecardViewModel)
    viewModelOf(::ScorecardListVewModel)
}
