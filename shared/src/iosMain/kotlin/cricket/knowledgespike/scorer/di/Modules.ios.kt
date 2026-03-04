package cricket.knowledgespike.scorer.di

import cricket.knowledgespike.scorer.data.source.DatabaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { DatabaseFactory() }
    }