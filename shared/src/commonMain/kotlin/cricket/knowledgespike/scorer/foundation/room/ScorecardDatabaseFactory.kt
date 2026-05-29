package cricket.knowledgespike.scorer.foundation.room

import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

fun buildScorecardDatabase(builder: RoomDatabase.Builder<ScorecardDatabase>): ScorecardDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .addScorecardMigrations()
        .build()
}

object ScorecardDatabaseMigrations {
    val all: List<Migration> = emptyList()
}

private fun RoomDatabase.Builder<ScorecardDatabase>.addScorecardMigrations(): RoomDatabase.Builder<ScorecardDatabase> {
    if (ScorecardDatabaseMigrations.all.isEmpty()) {
        return this
    }
    return addMigrations(*ScorecardDatabaseMigrations.all.toTypedArray())
}
