package cricket.knowledgespike.scorer.domain.repository

import arrow.core.Either
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.model.MatchSummary
import cricket.knowledgespike.scorer.domain.model.StoredMatch

sealed interface MatchPersistenceError {
    data class UnableToWriteMatch(val reason: String?) : MatchPersistenceError

    data class UnableToReadMatches(val reason: String?) : MatchPersistenceError

    data class UnableToDeleteMatch(val reason: String?) : MatchPersistenceError

    data class MatchNotFound(val matchId: Long) : MatchPersistenceError

    data class InvalidStoredMatchData(val reason: String) : MatchPersistenceError
}

interface MatchRepository {
    suspend fun createMatchFromSetup(matchSetup: MatchSetup): Either<MatchPersistenceError, StoredMatch>

    suspend fun listStoredMatches(): Either<MatchPersistenceError, List<StoredMatch>>

    suspend fun deleteMatch(matchId: Long): Either<MatchPersistenceError, Unit>

    suspend fun getMatchSummary(matchId: Long): Either<MatchPersistenceError, MatchSummary>
}
