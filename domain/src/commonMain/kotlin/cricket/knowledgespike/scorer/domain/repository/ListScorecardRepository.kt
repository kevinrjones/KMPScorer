package cricket.knowledgespike.scorer.domain.repository

import arrow.core.Either
import cricket.knowledgespike.scorer.domain.ScorecardError
import cricket.knowledgespike.scorer.domain.model.ScorecardHeaderDetails
import kotlinx.coroutines.flow.Flow

interface ListScorecardRepository {
    fun getScorecards(): Flow<List<ScorecardHeaderDetails>>
    suspend fun deleteScorecard(scorecard: ScorecardHeaderDetails): Either<ScorecardError.Local.UnableToDeleteScorecard, Unit>
}