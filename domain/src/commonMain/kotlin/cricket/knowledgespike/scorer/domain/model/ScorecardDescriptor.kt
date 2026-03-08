package cricket.knowledgespike.scorer.domain.model

data class ScorecardDescriptor(
    val id: Int?,
    val teamName: String,
    val opponentsName: String,
    val venue: String,
    val title: String,
    val matchDate: String,
    val battingSide: String,
    val umpire1Name: String?,
    val umpire2Name: String?,
    val thirdUmpireName: String?,
    val refereeName: String?,
    val scorer1Name: String,
    val scorer2Name: String?,
    val typeOfMatch: String,
    val duration: String,
    val startTime: String,
    val teamWinningToss: String,
    val weather: String?,
    val pitchCondition: String?,
)