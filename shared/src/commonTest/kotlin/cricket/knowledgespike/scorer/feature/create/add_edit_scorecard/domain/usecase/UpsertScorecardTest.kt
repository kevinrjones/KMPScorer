package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import com.knowledgespike.scorer.data.source.ScorecardDao
import cricket.knowledgespike.scorer.data.entity.ScorecardEntity
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UpsertScorecardTest {

    private class FakeScorecardDao : ScorecardDao {
        var lastUpsertedScorecard: ScorecardEntity? = null

        override fun getScorecards() = throw UnsupportedOperationException()
        override suspend fun getScorecard(id: Int) = throw UnsupportedOperationException()
        override suspend fun deleteScorecard(scorecard: ScorecardEntity) = throw UnsupportedOperationException()
        
        override suspend fun upsertScorecard(scorecard: ScorecardEntity) {
            lastUpsertedScorecard = scorecard
        }
    }

    @Test
    fun `test upsertScorecard calls dao upsert`() = runTest {
        val fakeDao = FakeScorecardDao()
        val upsertScorecard = UpsertScorecard(fakeDao)
        val details = ScorecardHeaderDetails(
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
