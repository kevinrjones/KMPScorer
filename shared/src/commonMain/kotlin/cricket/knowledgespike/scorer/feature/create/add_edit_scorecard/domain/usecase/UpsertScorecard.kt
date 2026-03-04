package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import com.knowledgespike.scorer.data.source.ScorecardDao
import cricket.knowledgespike.scorer.data.entity.toEntity
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardFullDetails

class UpsertScorecard(private val dao: ScorecardDao) {

    suspend operator fun invoke(scorecard: ScorecardFullDetails) {
        dao.upsertScorecard(scorecard.toEntity())
    }
}
