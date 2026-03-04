package cricket.knowledgespike.scorer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardFullDetails

@Entity(tableName = "Scorecards")
data class Scorecard(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
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
    val pitchCondition: String?
)

fun ScorecardFullDetails.toEntity(): Scorecard {
    return Scorecard(
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
