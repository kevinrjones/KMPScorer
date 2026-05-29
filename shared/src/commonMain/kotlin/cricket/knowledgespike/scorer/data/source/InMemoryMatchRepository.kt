package cricket.knowledgespike.scorer.data.source

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.model.MatchSummary
import cricket.knowledgespike.scorer.domain.model.StoredMatch
import cricket.knowledgespike.scorer.domain.repository.MatchPersistenceError
import cricket.knowledgespike.scorer.domain.repository.MatchRepository
import kotlin.time.Clock

class InMemoryMatchRepository(
    private val clock: Clock = Clock.System,
) : MatchRepository {
    private val storedMatches = mutableListOf<StoredMatch>()
    private var nextId = 1L

    override suspend fun createMatchFromSetup(matchSetup: MatchSetup): Either<MatchPersistenceError, StoredMatch> {
        val now = clock.now().toEpochMilliseconds()
        val storedMatch = StoredMatch(
            id = nextId++,
            matchSetup = matchSetup,
            createdAtEpochMillis = now,
            updatedAtEpochMillis = now,
        )
        storedMatches += storedMatch
        return storedMatch.right()
    }

    override suspend fun listStoredMatches(): Either<MatchPersistenceError, List<StoredMatch>> {
        return storedMatches
            .asReversed()
            .right()
    }

    override suspend fun deleteMatch(matchId: Long): Either<MatchPersistenceError, Unit> {
        val removedMatch = storedMatches.removeAll { it.id == matchId }
        return if (removedMatch) {
            Unit.right()
        } else {
            MatchPersistenceError.MatchNotFound(matchId).left()
        }
    }

    override suspend fun getMatchSummary(matchId: Long): Either<MatchPersistenceError, MatchSummary> {
        val storedMatch = storedMatches.firstOrNull { it.id == matchId }
            ?: return MatchPersistenceError.MatchNotFound(matchId).left()
        return MatchSummary(
            storedMatch = storedMatch,
            scoreEvents = emptyList(),
        ).right()
    }
}
