package cricket.knowledgespike.scorer.data.source

import androidx.room.Room
import cricket.knowledgespike.scorer.foundation.room.ScorecardDatabase
import cricket.knowledgespike.scorer.foundation.room.buildScorecardDatabase
import java.io.File

fun createJvmScorecardDatabase(databaseDirectoryPath: String): ScorecardDatabase {
    val databaseDirectory = File(databaseDirectoryPath)
    databaseDirectory.mkdirs()
    val databasePath = File(databaseDirectory, ScorecardDatabase.FileName).absolutePath
    return buildScorecardDatabase(
        builder = Room.databaseBuilder<ScorecardDatabase>(
            name = databasePath,
        ),
    )
}
