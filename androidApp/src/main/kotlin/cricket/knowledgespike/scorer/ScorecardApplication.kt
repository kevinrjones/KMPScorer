package cricket.knowledgespike.scorer

import android.app.Application
import cricket.knowledgespike.scorer.di.initKoin
import org.koin.android.ext.koin.androidContext

class ScorecardApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@ScorecardApplication)
        }
    }
}