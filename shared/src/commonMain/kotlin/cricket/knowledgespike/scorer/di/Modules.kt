package cricket.knowledgespike.scorer.di

import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.presentation.AddEditScorecardViewModel
import cricket.knowledgespike.scorer.feature.find.match_list.presentation.MatchListVewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    viewModelOf(::AddEditScorecardViewModel)
    viewModelOf(::MatchListVewModel)
}