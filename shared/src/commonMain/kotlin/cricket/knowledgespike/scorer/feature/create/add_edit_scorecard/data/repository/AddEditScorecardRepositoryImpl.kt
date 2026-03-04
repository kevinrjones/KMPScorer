package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.data.repository

import arrow.core.Either
import arrow.core.left
import com.knowledgespike.scorer.data.source.ScorecardDao
import cricket.knowledgespike.scorer.data.entity.toEntity
import cricket.knowledgespike.scorer.data.entity.toScorecardFullDetails
import cricket.knowledgespike.scorer.domain.ScorecardError
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardFullDetails
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.repository.AddEditScorecardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// todo: I hate the name Impl
class AddEditScorecardRepositoryImpl(val dao: ScorecardDao) : AddEditScorecardRepository {
    override fun getScorecards(): Flow<List<ScorecardFullDetails>> {
        return dao.getScorecards().map { it.map { it.toScorecardFullDetails() } }
    }

    override suspend fun getScorecard(id: Int): Either<ScorecardError.Local, ScorecardFullDetails> {
        dao.getScorecard(id)?.let {
            return Either.Right(it.toScorecardFullDetails())
        }
        return Either.Left(ScorecardError.Local.UnableToFindScorecard(id))
    }

    override suspend fun insertScorecard(scorecardDescription: ScorecardFullDetails): Either<ScorecardError.Local.UnableToInsertScorecard, Unit> =
        try {
            dao.upsertScorecard(scorecardDescription.toEntity())
            Either.Right(Unit)
        } catch (_: Exception) {
            ScorecardError.Local.UnableToInsertScorecard.left()
        }

    override suspend fun updateScorecard(scorecardDescription: ScorecardFullDetails): Either<ScorecardError.Local.UnableToInsertScorecard, Unit> =
        try {
            dao.upsertScorecard(scorecardDescription.toEntity())
            Either.Right(Unit)
        } catch (_: Exception) {
            ScorecardError.Local.UnableToInsertScorecard.left()
        }

    override suspend fun deleteScorecard(scorecardDescription: ScorecardFullDetails): Either<ScorecardError.Local.UnableToDeleteScorecard, Unit> =
        if (dao.deleteScorecard(scorecardDescription.toEntity()) == 1) {
            Either.Right(Unit)
        } else {
            ScorecardError.Local.UnableToDeleteScorecard(scorecardDescription.id).left()
        }
}