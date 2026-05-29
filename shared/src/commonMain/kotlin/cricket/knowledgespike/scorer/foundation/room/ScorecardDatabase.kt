package cricket.knowledgespike.scorer.foundation.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import cricket.knowledgespike.scorer.data.entity.MatchEntity
import cricket.knowledgespike.scorer.data.entity.ScoreEventEntity
import cricket.knowledgespike.scorer.data.source.MatchDao
import cricket.knowledgespike.scorer.data.source.ScoreEventDao

@Database(
    entities = [
        MatchEntity::class,
        ScoreEventEntity::class,
    ],
    version = ScorecardDatabaseSchemaVersion,
    exportSchema = true,
)
@ConstructedBy(ScorecardDatabaseConstructor::class)
abstract class ScorecardDatabase : RoomDatabase() {
    abstract fun matchDao(): MatchDao

    abstract fun scoreEventDao(): ScoreEventDao

    companion object {
        const val FileName = "scorecard.db"
    }
}

const val ScorecardDatabaseSchemaVersion = 1

@Suppress("KotlinNoActualForExpect")
expect object ScorecardDatabaseConstructor : RoomDatabaseConstructor<ScorecardDatabase> {
    override fun initialize(): ScorecardDatabase
}
