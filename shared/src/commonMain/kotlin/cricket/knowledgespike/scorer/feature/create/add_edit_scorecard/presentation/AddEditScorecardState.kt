package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.presentation

import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails

data class AddEditScorecardState(
    val id: Int?,

    val teamName: String = "",
    val teamNameChanged: Boolean = false,

    val opponentsName: String = "",
    val opponentsNameChanged: Boolean = false,

    val venue: String = "",
    val venueChanged: Boolean = false,

    val title: String = "",
    val titleChanged: Boolean = false,

    val matchDate: String = "",
    val matchDateChanged: Boolean = false,

    val battingSide: String = "",
    val battingSideChanged: Boolean = false,

    val umpire1Name: String? = null,
    val umpire2Name: String? = null,
    val thirdUmpireName: String? = null,
    val refereeName: String? = null,

    val scorer1Name: String = "",
    val scorer1NameChanged: Boolean = false,

    val scorer2Name: String? = null,

    val typeOfMatch: String = "",
    val typeOfMatchChanged: Boolean = false,

    val duration: String = "",
    val durationChanged: Boolean = false,

    val startTime: String = "",
    val startTimeChanged: Boolean = false,

    val teamWinningToss: String = "",
    val teamWinningTossChanged: Boolean = false,

    val weather: String? = null,
    val pitchCondition: String? = ""
)

fun AddEditScorecardState.toScorecardHeaderDetails(): ScorecardHeaderDetails {
    return ScorecardHeaderDetails(
        id = id,
        teamName = teamName,
        opponentsName = opponentsName,
        venue = venue,
        title = title,
        matchDate = matchDate,
        battingSide = battingSide,
        umpire1Name = umpire1Name,
        umpire2Name = umpire2Name,
        thirdUmpireName = thirdUmpireName,
        refereeName = refereeName,
        scorer1Name = scorer1Name,
        scorer2Name = scorer2Name,
        typeOfMatch = typeOfMatch,
        duration = duration,
        startTime = startTime,
        teamWinningToss = teamWinningToss,
        weather = weather,
        pitchCondition = pitchCondition
    )
}

fun ScorecardHeaderDetails.toScoreCardFullDetails(): AddEditScorecardState {
    return AddEditScorecardState(
        id = id,
        teamName = teamName,
        opponentsName = opponentsName,
        venue = venue,
        title = title,
        matchDate = matchDate,
        battingSide = battingSide,
        umpire1Name = umpire1Name,
        umpire2Name = umpire2Name,
        thirdUmpireName = thirdUmpireName,
        refereeName = refereeName,
        scorer1Name = scorer1Name,
        scorer2Name = scorer2Name,
        typeOfMatch = typeOfMatch,
        duration = duration,
        startTime = startTime,
        teamWinningToss = teamWinningToss,
        weather = weather,
        pitchCondition = pitchCondition
    )
}