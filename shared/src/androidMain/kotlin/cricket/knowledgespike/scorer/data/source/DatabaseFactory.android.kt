package cricket.knowledgespike.scorer.data.source

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.knowledgespike.scorer.data.source.ScorecardDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseFactory(val context: Context) {
    actual fun create(): RoomDatabase.Builder<ScorecardDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(ScorecardDatabase.DATABASE_NAME)
        return Room.databaseBuilder(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}