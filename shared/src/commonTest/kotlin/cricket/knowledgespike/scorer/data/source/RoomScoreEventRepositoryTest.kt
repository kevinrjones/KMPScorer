package cricket.knowledgespike.scorer.data.source

import cricket.knowledgespike.scorer.data.entity.ScoreEventEntity
import cricket.knowledgespike.scorer.domain.model.NewScoreEvent
import cricket.knowledgespike.scorer.domain.repository.ScoreEventPersistenceError
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class RoomScoreEventRepositoryTest {

    @Test
    fun `given blank event type when appending score event then invalid event error is returned`() = runTest {
        val repository = RoomScoreEventRepository(
            scoreEventLocalDataSource = FakeScoreEventLocalDataSourceForRepository(),
        )

        val appendResult = repository.appendScoreEvent(
            matchId = 1L,
            newScoreEvent = NewScoreEvent(eventType = "   ", payload = null),
        )

        assertTrue(appendResult.isLeft())
        assertIs<ScoreEventPersistenceError.InvalidScoreEvent>(appendResult.leftOrNull())
    }

    @Test
    fun `given existing events when appending score event then next sequence number is used`() = runTest {
        val localDataSource = FakeScoreEventLocalDataSourceForRepository(
            scoreEventsByMatchId = mutableMapOf(
                1L to mutableListOf(
                    ScoreEventEntity(
                        id = 10L,
                        matchId = 1L,
                        sequenceNumber = 1L,
                        eventType = "DotBall",
                        payload = null,
                        createdAtEpochMillis = 100L,
                    ),
                ),
            ),
        )
        val repository = RoomScoreEventRepository(
            scoreEventLocalDataSource = localDataSource,
            nowEpochMillis = { 200L },
        )

        val appendResult = repository.appendScoreEvent(
            matchId = 1L,
            newScoreEvent = NewScoreEvent(eventType = "Single", payload = "{}"),
        )

        assertTrue(appendResult.isRight())
        val appendedEvent = appendResult.getOrNull()
        assertEquals(2L, appendedEvent?.sequenceNumber)
        assertEquals("Single", appendedEvent?.eventType)
        assertEquals(200L, appendedEvent?.createdAtEpochMillis)
    }

    @Test
    fun `given local failure when appending score event then write error is returned`() = runTest {
        val repository = RoomScoreEventRepository(
            scoreEventLocalDataSource = FakeScoreEventLocalDataSourceForRepository(
                throwOnWrite = IllegalStateException("write failed"),
            ),
        )

        val appendResult = repository.appendScoreEvent(
            matchId = 1L,
            newScoreEvent = NewScoreEvent(eventType = "Single", payload = null),
        )

        assertTrue(appendResult.isLeft())
        assertIs<ScoreEventPersistenceError.UnableToWriteScoreEvent>(appendResult.leftOrNull())
    }

    @Test
    fun `given local failure when listing score events then read error is returned`() = runTest {
        val repository = RoomScoreEventRepository(
            scoreEventLocalDataSource = FakeScoreEventLocalDataSourceForRepository(
                throwOnRead = IllegalStateException("read failed"),
            ),
        )

        val listResult = repository.listScoreEvents(matchId = 1L)

        assertTrue(listResult.isLeft())
        assertIs<ScoreEventPersistenceError.UnableToReadScoreEvents>(listResult.leftOrNull())
    }
}

private class FakeScoreEventLocalDataSourceForRepository(
    scoreEventsByMatchId: MutableMap<Long, MutableList<ScoreEventEntity>> = mutableMapOf(),
    private val throwOnWrite: Exception? = null,
    private val throwOnRead: Exception? = null,
) : ScoreEventLocalDataSource {
    private val scoreEventsByMatchId = scoreEventsByMatchId
    private var nextId = scoreEventsByMatchId.values
        .flatMap { it }
        .maxOfOrNull { it.id }
        ?.plus(1L)
        ?: 1L

    override suspend fun insertScoreEvent(scoreEventEntity: ScoreEventEntity): Long {
        throwOnWrite?.let { throw it }
        val assignedId = nextId++
        val updatedEntity = scoreEventEntity.copy(id = assignedId)
        val existing = scoreEventsByMatchId.getOrPut(scoreEventEntity.matchId) { mutableListOf() }
        existing += updatedEntity
        return assignedId
    }

    override suspend fun listScoreEvents(matchId: Long): List<ScoreEventEntity> {
        throwOnRead?.let { throw it }
        return scoreEventsByMatchId[matchId].orEmpty()
    }

    override suspend fun maxSequenceNumber(matchId: Long): Long {
        throwOnRead?.let { throw it }
        return scoreEventsByMatchId[matchId].orEmpty().maxOfOrNull { it.sequenceNumber } ?: 0L
    }
}
