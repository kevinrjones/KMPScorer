package cricket.knowledgespike.scorer.data.source

import cricket.knowledgespike.scorer.data.entity.MatchEntity
import cricket.knowledgespike.scorer.data.entity.ScoreEventEntity
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class RoomLocalDataSourceTest {

    @Test
    fun `given room match local data source when reading and writing then dao methods are delegated`() = kotlinx.coroutines.test.runTest {
        val matchEntity = MatchEntity(
            id = 1L,
            teamAName = "Falcons",
            teamBName = "Kings",
            scheduleType = "Overs",
            scheduleAmount = 20,
            tossWinner = "TeamA",
            tossDecision = "Bat",
            matchDateEpochDays = LocalDate.parse("2026-05-25").toEpochDays(),
            venue = null,
            umpireOne = null,
            umpireTwo = null,
            weather = null,
            createdAtEpochMillis = 10L,
            updatedAtEpochMillis = 10L,
        )
        val fakeDao = FakeMatchDao(matchEntity)
        val dataSource = RoomMatchLocalDataSource(matchDao = fakeDao)

        val insertedId = dataSource.insertMatch(matchEntity)
        val loadedById = dataSource.getMatchById(1L)
        val loadedList = dataSource.listMatches()
        val deleted = dataSource.deleteMatch(1L)

        assertEquals(1L, insertedId)
        assertEquals(matchEntity, loadedById)
        assertEquals(listOf(matchEntity), loadedList)
        assertEquals(true, deleted)
    }

    @Test
    fun `given room score event local data source when reading and writing then dao methods are delegated`() = kotlinx.coroutines.test.runTest {
        val scoreEvent = ScoreEventEntity(
            id = 1L,
            matchId = 1L,
            sequenceNumber = 1L,
            eventType = "DotBall",
            payload = null,
            createdAtEpochMillis = 10L,
        )
        val fakeDao = FakeScoreEventDao(scoreEvent)
        val dataSource = RoomScoreEventLocalDataSource(scoreEventDao = fakeDao)

        val insertedId = dataSource.insertScoreEvent(scoreEvent)
        val loadedEvents = dataSource.listScoreEvents(matchId = 1L)
        val maxSequence = dataSource.maxSequenceNumber(matchId = 1L)

        assertEquals(1L, insertedId)
        assertEquals(listOf(scoreEvent), loadedEvents)
        assertEquals(1L, maxSequence)
    }
}

private class FakeMatchDao(
    private val matchEntity: MatchEntity,
) : MatchDao {
    override suspend fun insert(matchEntity: MatchEntity): Long {
        return 1L
    }

    override suspend fun getById(matchId: Long): MatchEntity? {
        return this.matchEntity
    }

    override suspend fun listAll(): List<MatchEntity> {
        return listOf(matchEntity)
    }

    override suspend fun deleteById(matchId: Long): Int {
        return 1
    }
}

private class FakeScoreEventDao(
    private val scoreEventEntity: ScoreEventEntity,
) : ScoreEventDao {
    override suspend fun insert(scoreEventEntity: ScoreEventEntity): Long {
        return 1L
    }

    override suspend fun listForMatch(matchId: Long): List<ScoreEventEntity> {
        return listOf(scoreEventEntity)
    }

    override suspend fun maxSequenceNumberForMatch(matchId: Long): Long {
        return scoreEventEntity.sequenceNumber
    }
}
