package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import com.knowledgespike.scorer.data.source.ScorecardDao
import cricket.knowledgespike.scorer.data.entity.Scorecard
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardFullDetails
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UpsertScorecardTest {

    private class FakeScorecardDao : ScorecardDao {
        var lastUpsertedScorecard: Scorecard? = null

        override fun getScorecards() = throw UnsupportedOperationException()
        override suspend fun getScorecard(id: Int) = throw UnsupportedOperationException()
        override suspend fun deleteScorecard(scorecard: Scorecard) = throw UnsupportedOperationException()
        
        override suspend fun upsertScorecard(scorecard: Scorecard) {
            lastUpsertedScorecard = scorecard
        }
    }

    @Test
    fun `test upsertScorecard calls dao upsert`() = runTest {
        val fakeDao = FakeScorecardDao()
        val upsertScorecard = UpsertScorecard(fakeDao)
        val details = ScorecardFullDetails(
            id = 1,
            teamName = "England",
            opponentsName = "Australia"
        )

        upsertScorecard(details)

        assertEquals(1, fakeDao.lastUpsertedScorecard?.id)
        assertEquals("England", fakeDao.lastUpsertedScorecard?.teamName)
        assertEquals("Australia", fakeDao.lastUpsertedScorecard?.opponentsName)
    }
}
