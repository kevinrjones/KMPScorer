package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.repository

import arrow.core.Either
import cricket.knowledgespike.scorer.domain.ScorecardError
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardFullDetails
import kotlinx.coroutines.flow.Flow

interface AddEditScorecardRepository {
    fun getScorecards(): Flow<List<ScorecardFullDetails>>
    suspend fun getScorecard(id: Int): Either<ScorecardError.Local, ScorecardFullDetails>
    suspend fun insertScorecard(scorecardDescription: ScorecardFullDetails): Either<ScorecardError.Local.UnableToInsertScorecard, Unit>
    suspend fun updateScorecard(scorecardDescription: ScorecardFullDetails): Either<ScorecardError.Local.UnableToInsertScorecard, Unit>
    suspend fun deleteScorecard(scorecardDescription: ScorecardFullDetails): Either<ScorecardError.Local.UnableToDeleteScorecard, Unit>
}