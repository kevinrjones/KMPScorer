package cricket.knowledgespike.scorer.data.source

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cricket.knowledgespike.scorer.data.entity.toDomain
import cricket.knowledgespike.scorer.data.entity.toEntity
import cricket.knowledgespike.scorer.domain.model.NewScoreEvent
import cricket.knowledgespike.scorer.domain.model.ScoreEvent
import cricket.knowledgespike.scorer.domain.repository.ScoreEventPersistenceError
import cricket.knowledgespike.scorer.domain.repository.ScoreEventRepository
import kotlinx.coroutines.CancellationException
import kotlin.time.Clock

class RoomScoreEventRepository(
    private val scoreEventLocalDataSource: ScoreEventLocalDataSource,
    private val nowEpochMillis: () -> Long = { Clock.System.now().toEpochMilliseconds() },
) : ScoreEventRepository {

    override suspend fun appendScoreEvent(
        matchId: Long,
        newScoreEvent: NewScoreEvent,
    ): Either<ScoreEventPersistenceError, ScoreEvent> {
        if (newScoreEvent.eventType.isBlank()) {
            return ScoreEventPersistenceError.InvalidScoreEvent("Score event type cannot be blank").left()
        }

        return try {
            val nextSequenceNumber = scoreEventLocalDataSource.maxSequenceNumber(matchId) + 1
            val nowEpochMillis = nowEpochMillis()
            val entity = newScoreEvent.toEntity(
                matchId = matchId,
                sequenceNumber = nextSequenceNumber,
                nowEpochMillis = nowEpochMillis,
            )
            val insertedId = scoreEventLocalDataSource.insertScoreEvent(entity)

            entity.copy(id = insertedId).toDomain().right()
        } catch (error: Exception) {
            if (error is CancellationException) {
                throw error
            }
            ScoreEventPersistenceError.UnableToWriteScoreEvent(error.message).left()
        }
    }

    override suspend fun listScoreEvents(matchId: Long): Either<ScoreEventPersistenceError, List<ScoreEvent>> {
        return try {
            scoreEventLocalDataSource
                .listScoreEvents(matchId)
                .map { it.toDomain() }
                .right()
        } catch (error: Exception) {
            if (error is CancellationException) {
                throw error
            }
            ScoreEventPersistenceError.UnableToReadScoreEvents(error.message).left()
        }
    }
}
