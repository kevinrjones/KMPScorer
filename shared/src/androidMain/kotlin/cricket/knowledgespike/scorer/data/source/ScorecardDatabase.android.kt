package cricket.knowledgespike.scorer.data.source

import android.content.Context
import androidx.room.Room
import cricket.knowledgespike.scorer.foundation.room.ScorecardDatabase
import cricket.knowledgespike.scorer.foundation.room.buildScorecardDatabase

fun createAndroidScorecardDatabase(context: Context): ScorecardDatabase {
    val databasePath = context.getDatabasePath(ScorecardDatabase.FileName).absolutePath
    return buildScorecardDatabase(
        builder = Room.databaseBuilder<ScorecardDatabase>(
            context = context,
            name = databasePath,
        ),
    )
}
