package cricket.knowledgespike.scorer.domain.model

import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup

data class StoredMatch(
    val id: Long,
    val matchSetup: MatchSetup,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long,
)

data class MatchSummary(
    val storedMatch: StoredMatch,
    val scoreEvents: List<ScoreEvent>,
)
