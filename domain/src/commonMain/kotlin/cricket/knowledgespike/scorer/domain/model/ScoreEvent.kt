package cricket.knowledgespike.scorer.domain.model

data class ScoreEvent(
    val id: Long,
    val matchId: Long,
    val sequenceNumber: Long,
    val eventType: String,
    val payload: String?,
    val createdAtEpochMillis: Long,
)

data class NewScoreEvent(
    val eventType: String,
    val payload: String?,
)
