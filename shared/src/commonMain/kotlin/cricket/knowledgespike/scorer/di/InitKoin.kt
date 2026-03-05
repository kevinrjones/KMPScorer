package cricket.knowledgespike.scorer.di

import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase.addEditModule
import cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.usecase.scorecardListEditModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            sharedModule,
            addEditModule,
            scorecardListEditModule,
            platformModule
        )
    }
