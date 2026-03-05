package cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.repository

import arrow.core.Either
import cricket.knowledgespike.scorer.domain.ScorecardError
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails
import kotlinx.coroutines.flow.Flow

interface ListScorecardRepository {
    fun getScorecards(): Flow<List<ScorecardHeaderDetails>>
    suspend fun deleteScorecard(scorecard: ScorecardHeaderDetails): Either<ScorecardError.Local.UnableToDeleteScorecard, Unit>
}