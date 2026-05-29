package cricket.knowledgespike.scorer.domain.repository

import arrow.core.Either
import cricket.knowledgespike.scorer.domain.model.NewScoreEvent
import cricket.knowledgespike.scorer.domain.model.ScoreEvent

sealed interface ScoreEventPersistenceError {
    data class UnableToWriteScoreEvent(val reason: String?) : ScoreEventPersistenceError

    data class UnableToReadScoreEvents(val reason: String?) : ScoreEventPersistenceError

    data class InvalidScoreEvent(val reason: String) : ScoreEventPersistenceError
}

interface ScoreEventRepository {
    suspend fun appendScoreEvent(
        matchId: Long,
        newScoreEvent: NewScoreEvent,
    ): Either<ScoreEventPersistenceError, ScoreEvent>

    suspend fun listScoreEvents(matchId: Long): Either<ScoreEventPersistenceError, List<ScoreEvent>>
}
