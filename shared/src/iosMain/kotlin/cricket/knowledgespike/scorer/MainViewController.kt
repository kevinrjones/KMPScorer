@file:Suppress("FunctionName", "unused")

package cricket.knowledgespike.scorer

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import cricket.knowledgespike.scorer.preferences.AppPreferencesStateStore
import cricket.knowledgespike.scorer.preferences.JsonPreferencesRepository
import cricket.knowledgespike.scorer.preferences.OkioPreferencesStorageDataSource
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun MainViewController() = ComposeUIViewController(
    configure = {  }
) {
    val appPreferencesStateStore = remember {
        val documentsDirectory = NSSearchPathForDirectoriesInDomains(
            directory = NSDocumentDirectory,
            domainMask = NSUserDomainMask,
            expandTilde = true,
        ).firstOrNull() as? String ?: NSTemporaryDirectory()

        val preferencesFilePath = "$documentsDirectory/KMPScorer/app_preferences.json"

        AppPreferencesStateStore(
            preferencesRepository = JsonPreferencesRepository(
                preferencesStorageDataSource = OkioPreferencesStorageDataSource(preferencesFilePath),
            ),
        )
    }

    val widthSizeClass = calculateWindowSizeClass().widthSizeClass
    App(
        widthSizeClass = widthSizeClass,
        appPreferencesStateStore = appPreferencesStateStore,
    )
}