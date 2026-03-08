package cricket.knowledgespike.scorer.domain.model

data class ScorecardHeaderDetails(
    val id: Int? = null,
    val teamName: String = "",
    val opponentsName: String = "",
    val venue: String = "",
    val title: String = "",
    val matchDate: String = "",
    val battingSide: String = "",
    val umpire1Name: String? = null,
    val umpire2Name: String? = null,
    val thirdUmpireName: String? = null,
    val refereeName: String? = null,
    val scorer1Name: String = "",
    val scorer2Name: String? = null,
    val typeOfMatch: String = "",
    val duration: String = "",
    val startTime: String = "",
    val teamWinningToss: String = "",
    val weather: String? = null,
    val pitchCondition: String? = "",
)