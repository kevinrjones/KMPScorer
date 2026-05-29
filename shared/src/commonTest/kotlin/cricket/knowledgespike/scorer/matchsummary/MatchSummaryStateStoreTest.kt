package cricket.knowledgespike.scorer.matchsummary

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSchedule
import cricket.knowledgespike.scorer.domain.matchsetup.MatchScheduleType
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner
import cricket.knowledgespike.scorer.domain.model.MatchSummary
import cricket.knowledgespike.scorer.domain.model.ScoreEvent
import cricket.knowledgespike.scorer.domain.model.StoredMatch
import cricket.knowledgespike.scorer.domain.repository.MatchPersistenceError
import cricket.knowledgespike.scorer.domain.repository.MatchRepository
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class MatchSummaryStateStoreTest {

    @Test
    fun `given summary exists when store is created then content state is emitted`() = runTest {
        val stateStore = MatchSummaryStateStore(
            matchId = 7L,
            matchRepository = FakeSummaryMatchRepository(
                summaryResult = summary(matchId = 7L).right(),
            ),
            coroutineScope = this,
        )

        testScheduler.advanceUntilIdle()

        val contentState = assertIs<MatchSummaryState.Content>(stateStore.screenState.value.summaryState)
        assertEquals(7L, contentState.summary.id)
        assertEquals("Falcons vs Kings", contentState.summary.title)
        assertEquals(1, contentState.summary.scoreEventCount)
    }

    @Test
    fun `given summary load failure when store is created then error state is emitted`() = runTest {
        val stateStore = MatchSummaryStateStore(
            matchId = 5L,
            matchRepository = FakeSummaryMatchRepository(
                summaryResult = MatchPersistenceError.MatchNotFound(5L).left(),
            ),
            coroutineScope = this,
        )

        testScheduler.advanceUntilIdle()

        val errorState = assertIs<MatchSummaryState.Error>(stateStore.screenState.value.summaryState)
        assertEquals("Match #5 was not found.", errorState.message)
    }

    @Test
    fun `given error state when retry requested then summary is loaded again`() = runTest {
        val fakeRepository = FakeSummaryMatchRepository(
            summaryResult = MatchPersistenceError.UnableToReadMatches("broken").left(),
        )
        val stateStore = MatchSummaryStateStore(
            matchId = 9L,
            matchRepository = fakeRepository,
            coroutineScope = this,
        )
        testScheduler.advanceUntilIdle()

        fakeRepository.summaryResult = summary(matchId = 9L).right()

        stateStore.onEvent(MatchSummaryEvent.RetryRequested)
        testScheduler.advanceUntilIdle()

        val contentState = assertIs<MatchSummaryState.Content>(stateStore.screenState.value.summaryState)
        assertEquals(9L, contentState.summary.id)
    }

    private fun summary(matchId: Long): MatchSummary {
        return MatchSummary(
            storedMatch = StoredMatch(
                id = matchId,
                matchSetup = MatchSetup(
                    teamAName = "Falcons",
                    teamBName = "Kings",
                    schedule = MatchSchedule(type = MatchScheduleType.Overs, amount = 20),
                    tossWinner = TossWinner.TeamA,
                    tossDecision = TossDecision.Bat,
                    matchDate = LocalDate.parse("2026-05-25"),
                    venue = "Oval",
                    umpireOne = null,
                    umpireTwo = null,
                    weather = null,
                ),
                createdAtEpochMillis = 10L,
                updatedAtEpochMillis = 10L,
            ),
            scoreEvents = listOf(
                ScoreEvent(
                    id = 1L,
                    matchId = matchId,
                    sequenceNumber = 1L,
                    eventType = "DotBall",
                    payload = null,
                    createdAtEpochMillis = 12L,
                ),
            ),
        )
    }
}

private class FakeSummaryMatchRepository(
    var summaryResult: Either<MatchPersistenceError, MatchSummary>,
) : MatchRepository {
    override suspend fun createMatchFromSetup(matchSetup: MatchSetup): Either<MatchPersistenceError, StoredMatch> {
        return MatchPersistenceError.UnableToWriteMatch("not used").left()
    }

    override suspend fun listStoredMatches(): Either<MatchPersistenceError, List<StoredMatch>> {
        return emptyList<StoredMatch>().right()
    }

    override suspend fun deleteMatch(matchId: Long): Either<MatchPersistenceError, Unit> {
        return MatchPersistenceError.UnableToDeleteMatch("not used").left()
    }

    override suspend fun getMatchSummary(matchId: Long): Either<MatchPersistenceError, MatchSummary> {
        return summaryResult
    }
}
