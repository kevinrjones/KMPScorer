package cricket.knowledgespike.scorer.di

import cricket.knowledgespike.scorer.data.source.DatabaseFactory
import org.koin.dsl.module

actual val platformModule: org.koin.core.module.Module
    get() = module {
        single { DatabaseFactory() }
    }