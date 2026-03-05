package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.data.repository

import arrow.core.Either
import arrow.core.left
import com.knowledgespike.scorer.data.source.ScorecardDao
import cricket.knowledgespike.scorer.data.entity.toEntity
import cricket.knowledgespike.scorer.data.entity.toScorecardHeaderDetails
import cricket.knowledgespike.scorer.domain.ScorecardError.Local
import cricket.knowledgespike.scorer.domain.ScorecardError.Local.UnableToInsertScorecard
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.repository.AddEditScorecardRepository

// todo: I hate the name Impl
class AddEditScorecardRepositoryImpl(val dao: ScorecardDao) : AddEditScorecardRepository {

    override suspend fun insertScorecard(scorecardDescription: ScorecardHeaderDetails): Either<UnableToInsertScorecard, Unit> =
        try {
            dao.upsertScorecard(scorecardDescription.toEntity())
            Either.Right(Unit)
        } catch (e: Exception) {
            UnableToInsertScorecard((e.message)).left()
        }

    override suspend fun updateScorecard(scorecardDescription: ScorecardHeaderDetails): Either<UnableToInsertScorecard, Unit> =
        try {
            dao.upsertScorecard(scorecardDescription.toEntity())
            Either.Right(Unit)
        } catch (e: Exception) {
            UnableToInsertScorecard(e.message).left()
        }

    override suspend fun upsertScorecard(scorecardDescription: ScorecardHeaderDetails): Either<UnableToInsertScorecard, Unit> =
        try {
            dao.upsertScorecard(scorecardDescription.toEntity())
            Either.Right(Unit)
        } catch (e: Exception) {
            UnableToInsertScorecard(e.message).left()
        }

    override suspend fun getScorecard(id: Int) =
        try {
            val result = dao.getScorecard(id)
            if (result != null)
                Either.Right(result.toScorecardHeaderDetails())
            else
                Local.UnableToFindScorecard(id).left()
        } catch (e: Exception) {
            Local.UnableToFindScorecard(id, e.message).left()
        }

}