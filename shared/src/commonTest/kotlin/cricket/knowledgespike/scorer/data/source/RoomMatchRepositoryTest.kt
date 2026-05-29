package cricket.knowledgespike.scorer.data.source

import cricket.knowledgespike.scorer.data.entity.MatchEntity
import cricket.knowledgespike.scorer.data.entity.ScoreEventEntity
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSchedule
import cricket.knowledgespike.scorer.domain.matchsetup.MatchScheduleType
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner
import cricket.knowledgespike.scorer.domain.repository.MatchPersistenceError
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class RoomMatchRepositoryTest {

    @Test
    fun `given valid setup when creating match then stored match is returned`() = runTest {
        val matchLocalDataSource = FakeMatchLocalDataSource()
        val repository = RoomMatchRepository(
            matchLocalDataSource = matchLocalDataSource,
            scoreEventLocalDataSource = FakeScoreEventLocalDataSource(),
            nowEpochMillis = { 100L },
        )

        val createResult = repository.createMatchFromSetup(matchSetup())

        assertTrue(createResult.isRight())
        val storedMatch = createResult.getOrNull()
        assertEquals(1L, storedMatch?.id)
        assertEquals("Falcons", storedMatch?.matchSetup?.teamAName)
        assertEquals("Kings", storedMatch?.matchSetup?.teamBName)
    }

    @Test
    fun `given broken stored enum when listing matches then invalid data error is returned`() = runTest {
        val matchLocalDataSource = FakeMatchLocalDataSource(
            initialMatches = listOf(
                MatchEntity(
                    id = 1L,
                    teamAName = "Falcons",
                    teamBName = "Kings",
                    scheduleType = "Broken",
                    scheduleAmount = 20,
                    tossWinner = TossWinner.TeamA.name,
                    tossDecision = TossDecision.Bat.name,
                    matchDateEpochDays = LocalDate.parse("2026-05-25").toEpochDays(),
                    venue = null,
                    umpireOne = null,
                    umpireTwo = null,
                    weather = null,
                    createdAtEpochMillis = 10L,
                    updatedAtEpochMillis = 10L,
                ),
            ),
        )
        val repository = RoomMatchRepository(
            matchLocalDataSource = matchLocalDataSource,
            scoreEventLocalDataSource = FakeScoreEventLocalDataSource(),
        )

        val listResult = repository.listStoredMatches()

        assertTrue(listResult.isLeft())
        assertIs<MatchPersistenceError.InvalidStoredMatchData>(listResult.leftOrNull())
    }

    @Test
    fun `given local data source failure when creating match then write error is returned`() = runTest {
        val matchLocalDataSource = FakeMatchLocalDataSource(
            throwOnInsert = IllegalStateException("insert failed"),
        )
        val repository = RoomMatchRepository(
            matchLocalDataSource = matchLocalDataSource,
            scoreEventLocalDataSource = FakeScoreEventLocalDataSource(),
        )

        val createResult = repository.createMatchFromSetup(matchSetup())

        assertTrue(createResult.isLeft())
        assertIs<MatchPersistenceError.UnableToWriteMatch>(createResult.leftOrNull())
    }

    @Test
    fun `given no stored match when loading summary then not found error is returned`() = runTest {
        val repository = RoomMatchRepository(
            matchLocalDataSource = FakeMatchLocalDataSource(),
            scoreEventLocalDataSource = FakeScoreEventLocalDataSource(),
        )

        val summaryResult = repository.getMatchSummary(matchId = 99L)

        assertTrue(summaryResult.isLeft())
        assertEquals(MatchPersistenceError.MatchNotFound(99L), summaryResult.leftOrNull())
    }

    @Test
    fun `given stored match and events when loading summary then both are returned`() = runTest {
        val matchEntity = MatchEntity(
            id = 1L,
            teamAName = "Falcons",
            teamBName = "Kings",
            scheduleType = MatchScheduleType.Overs.name,
            scheduleAmount = 20,
            tossWinner = TossWinner.TeamA.name,
            tossDecision = TossDecision.Bat.name,
            matchDateEpochDays = LocalDate.parse("2026-05-25").toEpochDays(),
            venue = null,
            umpireOne = null,
            umpireTwo = null,
            weather = null,
            createdAtEpochMillis = 10L,
            updatedAtEpochMillis = 10L,
        )
        val repository = RoomMatchRepository(
            matchLocalDataSource = FakeMatchLocalDataSource(initialMatches = listOf(matchEntity)),
            scoreEventLocalDataSource = FakeScoreEventLocalDataSource(
                scoreEventsByMatchId = mapOf(
                    1L to listOf(
                        ScoreEventEntity(
                            id = 7L,
                            matchId = 1L,
                            sequenceNumber = 1L,
                            eventType = "DotBall",
                            payload = null,
                            createdAtEpochMillis = 50L,
                        ),
                    ),
                ),
            ),
        )

        val summaryResult = repository.getMatchSummary(matchId = 1L)

        assertTrue(summaryResult.isRight())
        val summary = summaryResult.getOrNull()
        assertEquals(1L, summary?.storedMatch?.id)
        assertEquals(1, summary?.scoreEvents?.size)
        assertEquals("DotBall", summary?.scoreEvents?.firstOrNull()?.eventType)
    }

    @Test
    fun `given stored match when deleting then success is returned`() = runTest {
        val repository = RoomMatchRepository(
            matchLocalDataSource = FakeMatchLocalDataSource(
                initialMatches = listOf(
                    MatchEntity(
                        id = 3L,
                        teamAName = "Falcons",
                        teamBName = "Kings",
                        scheduleType = MatchScheduleType.Overs.name,
                        scheduleAmount = 20,
                        tossWinner = TossWinner.TeamA.name,
                        tossDecision = TossDecision.Bat.name,
                        matchDateEpochDays = LocalDate.parse("2026-05-25").toEpochDays(),
                        venue = null,
                        umpireOne = null,
                        umpireTwo = null,
                        weather = null,
                        createdAtEpochMillis = 10L,
                        updatedAtEpochMillis = 10L,
                    ),
                ),
            ),
            scoreEventLocalDataSource = FakeScoreEventLocalDataSource(),
        )

        val deleteResult = repository.deleteMatch(matchId = 3L)

        assertTrue(deleteResult.isRight())
    }

    @Test
    fun `given missing match when deleting then not found error is returned`() = runTest {
        val repository = RoomMatchRepository(
            matchLocalDataSource = FakeMatchLocalDataSource(),
            scoreEventLocalDataSource = FakeScoreEventLocalDataSource(),
        )

        val deleteResult = repository.deleteMatch(matchId = 999L)

        assertTrue(deleteResult.isLeft())
        assertEquals(MatchPersistenceError.MatchNotFound(matchId = 999L), deleteResult.leftOrNull())
    }

    @Test
    fun `given local data source delete failure when deleting then delete error is returned`() = runTest {
        val repository = RoomMatchRepository(
            matchLocalDataSource = FakeMatchLocalDataSource(
                throwOnDelete = IllegalStateException("cannot delete"),
            ),
            scoreEventLocalDataSource = FakeScoreEventLocalDataSource(),
        )

        val deleteResult = repository.deleteMatch(matchId = 1L)

        assertTrue(deleteResult.isLeft())
        assertIs<MatchPersistenceError.UnableToDeleteMatch>(deleteResult.leftOrNull())
    }

    private fun matchSetup(): MatchSetup {
        return MatchSetup(
            teamAName = "Falcons",
            teamBName = "Kings",
            schedule = MatchSchedule(
                type = MatchScheduleType.Overs,
                amount = 20,
            ),
            tossWinner = TossWinner.TeamA,
            tossDecision = TossDecision.Bat,
            matchDate = LocalDate.parse("2026-05-25"),
            venue = null,
            umpireOne = null,
            umpireTwo = null,
            weather = null,
        )
    }
}

private class FakeMatchLocalDataSource(
    initialMatches: List<MatchEntity> = emptyList(),
    private val throwOnInsert: Exception? = null,
    private val throwOnRead: Exception? = null,
    private val throwOnDelete: Exception? = null,
) : MatchLocalDataSource {
    private val storedMatches = linkedMapOf<Long, MatchEntity>().apply {
        initialMatches.forEach { put(it.id, it) }
    }
    private var nextId = (storedMatches.keys.maxOrNull() ?: 0L) + 1

    override suspend fun insertMatch(matchEntity: MatchEntity): Long {
        throwOnInsert?.let { throw it }
        val assignedId = nextId++
        storedMatches[assignedId] = matchEntity.copy(id = assignedId)
        return assignedId
    }

    override suspend fun getMatchById(matchId: Long): MatchEntity? {
        throwOnRead?.let { throw it }
        return storedMatches[matchId]
    }

    override suspend fun listMatches(): List<MatchEntity> {
        throwOnRead?.let { throw it }
        return storedMatches.values.toList()
    }

    override suspend fun deleteMatch(matchId: Long): Boolean {
        throwOnDelete?.let { throw it }
        return storedMatches.remove(matchId) != null
    }
}

private class FakeScoreEventLocalDataSource(
    private val scoreEventsByMatchId: Map<Long, List<ScoreEventEntity>> = emptyMap(),
) : ScoreEventLocalDataSource {
    override suspend fun insertScoreEvent(scoreEventEntity: ScoreEventEntity): Long {
        return 1L
    }

    override suspend fun listScoreEvents(matchId: Long): List<ScoreEventEntity> {
        return scoreEventsByMatchId[matchId].orEmpty()
    }

    override suspend fun maxSequenceNumber(matchId: Long): Long {
        return scoreEventsByMatchId[matchId]?.maxOfOrNull { it.sequenceNumber } ?: 0L
    }
}
