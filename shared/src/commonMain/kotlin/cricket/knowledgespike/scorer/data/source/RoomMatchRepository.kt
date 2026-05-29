package cricket.knowledgespike.scorer.data.source

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cricket.knowledgespike.scorer.data.entity.MatchEntity
import cricket.knowledgespike.scorer.data.entity.toDomain
import cricket.knowledgespike.scorer.data.entity.toEntity
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.model.MatchSummary
import cricket.knowledgespike.scorer.domain.model.StoredMatch
import cricket.knowledgespike.scorer.domain.repository.MatchPersistenceError
import cricket.knowledgespike.scorer.domain.repository.MatchRepository
import kotlinx.coroutines.CancellationException
import kotlin.time.Clock

class RoomMatchRepository(
    private val matchLocalDataSource: MatchLocalDataSource,
    private val scoreEventLocalDataSource: ScoreEventLocalDataSource,
    private val nowEpochMillis: () -> Long = { Clock.System.now().toEpochMilliseconds() },
) : MatchRepository {

    override suspend fun createMatchFromSetup(matchSetup: MatchSetup): Either<MatchPersistenceError, StoredMatch> {
        return try {
            val insertedId = matchLocalDataSource.insertMatch(matchSetup.toEntity(nowEpochMillis = nowEpochMillis()))
            val insertedMatchEntity: MatchEntity = matchLocalDataSource.getMatchById(insertedId)
                ?: return MatchPersistenceError.UnableToWriteMatch("Inserted match was not found").left()

            insertedMatchEntity.toDomain().mapLeftToMatchError()
        } catch (error: Exception) {
            if (error is CancellationException) {
                throw error
            }
            MatchPersistenceError.UnableToWriteMatch(error.message).left()
        }
    }

    override suspend fun listStoredMatches(): Either<MatchPersistenceError, List<StoredMatch>> {
        return try {
            matchLocalDataSource
                .listMatches()
                .toStoredMatchesOrError()
        } catch (error: Exception) {
            if (error is CancellationException) {
                throw error
            }
            MatchPersistenceError.UnableToReadMatches(error.message).left()
        }
    }

    override suspend fun deleteMatch(matchId: Long): Either<MatchPersistenceError, Unit> {
        return try {
            val wasDeleted = matchLocalDataSource.deleteMatch(matchId)
            if (wasDeleted) {
                Unit.right()
            } else {
                MatchPersistenceError.MatchNotFound(matchId).left()
            }
        } catch (error: Exception) {
            if (error is CancellationException) {
                throw error
            }
            MatchPersistenceError.UnableToDeleteMatch(error.message).left()
        }
    }

    override suspend fun getMatchSummary(matchId: Long): Either<MatchPersistenceError, MatchSummary> {
        return try {
            val matchEntity = matchLocalDataSource.getMatchById(matchId)
                ?: return MatchPersistenceError.MatchNotFound(matchId).left()
            val storedMatch = when (val matchResult = matchEntity.toDomain()) {
                is Either.Left -> return matchResult.value.left()
                is Either.Right -> matchResult.value
            }

            val scoreEvents = scoreEventLocalDataSource
                .listScoreEvents(matchId)
                .map { it.toDomain() }

            MatchSummary(
                storedMatch = storedMatch,
                scoreEvents = scoreEvents,
            ).right()
        } catch (error: Exception) {
            if (error is CancellationException) {
                throw error
            }
            MatchPersistenceError.UnableToReadMatches(error.message).left()
        }
    }
}

private fun List<MatchEntity>.toStoredMatchesOrError(): Either<MatchPersistenceError, List<StoredMatch>> {
    val matches = mutableListOf<StoredMatch>()
    for (matchEntity in this) {
        when (val mappedMatch = matchEntity.toDomain()) {
            is Either.Left -> return mappedMatch.value.left()
            is Either.Right -> matches += mappedMatch.value
        }
    }
    return matches.right()
}

private fun Either<MatchPersistenceError.InvalidStoredMatchData, StoredMatch>.mapLeftToMatchError(): Either<MatchPersistenceError, StoredMatch> {
    return when (this) {
        is Either.Left -> value.left()
        is Either.Right -> value.right()
    }
}
