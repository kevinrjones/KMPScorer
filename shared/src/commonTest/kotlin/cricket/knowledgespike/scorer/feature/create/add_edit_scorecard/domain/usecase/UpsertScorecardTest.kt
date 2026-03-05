package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import arrow.core.Either
import cricket.knowledgespike.scorer.domain.ScorecardError
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.repository.AddEditScorecardRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UpsertScorecardTest {

    private class FakeAddEditScorecardRepository : AddEditScorecardRepository {
        var lastUpsertedScorecard: ScorecardHeaderDetails? = null

        override suspend fun getScorecard(id: Int): Either<ScorecardError.Local, ScorecardHeaderDetails> {
            throw UnsupportedOperationException()
        }

        override suspend fun insertScorecard(scorecardDescription: ScorecardHeaderDetails): Either<ScorecardError.Local.UnableToInsertScorecard, Unit> {
            throw UnsupportedOperationException()
        }

        override suspend fun updateScorecard(scorecardDescription: ScorecardHeaderDetails): Either<ScorecardError.Local.UnableToInsertScorecard, Unit> {
            throw UnsupportedOperationException()
        }

        override suspend fun upsertScorecard(scorecardDescription: ScorecardHeaderDetails): Either<ScorecardError.Local.UnableToInsertScorecard, Unit> {
            lastUpsertedScorecard = scorecardDescription
            return Either.Right(Unit)
        }
    }

    @Test
    fun `test upsertScorecard calls repository upsert`() = runTest {
        val fakeRepository = FakeAddEditScorecardRepository()
        val upsertScorecard = UpsertScorecard(fakeRepository)
        val details = ScorecardHeaderDetails(
            id = 1,
            teamName = "England",
            opponentsName = "Australia"
        )

        upsertScorecard(details)

        assertEquals(1, fakeRepository.lastUpsertedScorecard?.id)
        assertEquals("England", fakeRepository.lastUpsertedScorecard?.teamName)
        assertEquals("Australia", fakeRepository.lastUpsertedScorecard?.opponentsName)
    }
}
