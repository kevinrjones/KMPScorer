@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package cricket.knowledgespike.scorer.data.source

import androidx.room.RoomDatabaseConstructor
import com.knowledgespike.scorer.data.source.ScorecardDatabase


@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object ScorecardDatabaseConstructor: RoomDatabaseConstructor<ScorecardDatabase> {
    override fun initialize(): ScorecardDatabase
}
