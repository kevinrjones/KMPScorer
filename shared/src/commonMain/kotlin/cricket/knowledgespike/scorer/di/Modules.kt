package cricket.knowledgespike.scorer.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.knowledgespike.scorer.data.source.ScorecardDatabase
import cricket.knowledgespike.scorer.data.source.DatabaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<ScorecardDatabase>().scorecardDao }

}
