package cricket.knowledgespike.scorer.feature.find.scorecard_list.domain.model

data class ScorecardIdentifyingDetails(
    val team: String,
    val opponents: String,
    val venue: String,
    val date: String,
)