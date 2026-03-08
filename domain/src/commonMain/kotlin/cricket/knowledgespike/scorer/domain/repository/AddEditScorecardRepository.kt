package cricket.knowledgespike.scorer.domain.repository

import arrow.core.Either
import cricket.knowledgespike.scorer.domain.ScorecardError
import cricket.knowledgespike.scorer.domain.model.ScorecardHeaderDetails

interface AddEditScorecardRepository {
    suspend fun getScorecard(id: Int): Either<ScorecardError.Local, ScorecardHeaderDetails>
    suspend fun insertScorecard(scorecardDescription: ScorecardHeaderDetails): Either<ScorecardError.Local.UnableToInsertScorecard, Unit>
    suspend fun updateScorecard(scorecardDescription: ScorecardHeaderDetails): Either<ScorecardError.Local.UnableToInsertScorecard, Unit>
    suspend fun upsertScorecard(scorecardDescription: ScorecardHeaderDetails): Either<ScorecardError.Local.UnableToInsertScorecard, Unit>
}