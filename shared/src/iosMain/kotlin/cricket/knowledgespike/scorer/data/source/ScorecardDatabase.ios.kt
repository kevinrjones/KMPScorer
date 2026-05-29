package cricket.knowledgespike.scorer.data.source

import androidx.room.Room
import cricket.knowledgespike.scorer.foundation.room.ScorecardDatabase
import cricket.knowledgespike.scorer.foundation.room.buildScorecardDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun createIosScorecardDatabase(): ScorecardDatabase {
    val databasePath = documentDirectory() + "/" + ScorecardDatabase.FileName
    return buildScorecardDatabase(
        builder = Room.databaseBuilder<ScorecardDatabase>(
            name = databasePath,
        ),
    )
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
