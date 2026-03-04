package cricket.knowledgespike.scorer.di

import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.AddEditScorecardUseCases
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.ValidateTeamName
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.presentation.AddEditScorecardViewModel
import cricket.knowledgespike.scorer.feature.find.scorecard_list.presentation.ScorecardListVewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    singleOf(::ValidateTeamName)
    singleOf(::AddEditScorecardUseCases)
    viewModelOf(::AddEditScorecardViewModel)
    viewModelOf(::ScorecardListVewModel)
}