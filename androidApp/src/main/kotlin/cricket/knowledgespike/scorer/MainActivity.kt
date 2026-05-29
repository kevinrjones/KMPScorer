package cricket.knowledgespike.scorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import cricket.knowledgespike.scorer.data.source.createRoomMatchRepository
import cricket.knowledgespike.scorer.data.source.createAndroidScorecardDatabase
import cricket.knowledgespike.scorer.preferences.AppPreferencesStateStore
import cricket.knowledgespike.scorer.preferences.JsonPreferencesRepository
import cricket.knowledgespike.scorer.preferences.OkioPreferencesStorageDataSource
import java.io.File

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val scorecardDatabase = remember { createAndroidScorecardDatabase(this@MainActivity) }
            val matchRepository = remember(scorecardDatabase) {
                createRoomMatchRepository(scorecardDatabase)
            }
            val appPreferencesStateStore = remember {
                val preferencesFilePath = File(filesDir, "app_preferences.json").absolutePath
                AppPreferencesStateStore(
                    preferencesRepository = JsonPreferencesRepository(
                        preferencesStorageDataSource = OkioPreferencesStorageDataSource(preferencesFilePath),
                    ),
                )
            }
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            App(
                widthSizeClass = widthSizeClass,
                appPreferencesStateStore = appPreferencesStateStore,
                matchRepository = matchRepository,
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}