package cricket.knowledgespike.scorer.feature.find.scorecard_list.data.repository

import arrow.core.Either
import arrow.core.left
import com.knowledgespike.scorer.data.source.ScorecardDao
import cricket.knowledgespike.scorer.data.entity.toEntity
import cricket.knowledgespike.scorer.data.entity.toScorecardHeaderDetails
import cricket.knowledgespike.scorer.domain.ScorecardError
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails
import cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.repository.ListScorecardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ListScorecardRepositoryImpl(val dao: ScorecardDao) : ListScorecardRepository {
    override fun getScorecards(): Flow<List<ScorecardHeaderDetails>> {
        return dao.getScorecards().map { scorecard -> scorecard.map { it.toScorecardHeaderDetails() } }
    }

    override suspend fun deleteScorecard(scorecard: ScorecardHeaderDetails): Either<ScorecardError.Local.UnableToDeleteScorecard, Unit> =
        if (dao.deleteScorecard(scorecard.toEntity()) == 1) {
            Either.Right(Unit)
        } else {
            ScorecardError.Local.UnableToDeleteScorecard(scorecard.id).left()
        }
}