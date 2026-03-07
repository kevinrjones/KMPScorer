package cricket.knowledgespike.scorer.di

import cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard.addEditModule
import cricket.knowledgespike.scorer.domain.usecase.create_scorecard.scorecardListEditModule
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
