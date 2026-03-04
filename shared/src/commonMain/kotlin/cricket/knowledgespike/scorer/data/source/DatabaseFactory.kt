package cricket.knowledgespike.scorer.data.source

import androidx.room.RoomDatabase
import com.knowledgespike.scorer.data.source.ScorecardDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<ScorecardDatabase>
}