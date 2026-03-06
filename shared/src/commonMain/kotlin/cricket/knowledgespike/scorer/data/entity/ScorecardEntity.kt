package cricket.knowledgespike.scorer.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.model.ScorecardHeaderDetails
import kotlin.time.Clock
import kotlin.time.Instant

@Entity(tableName = "Scorecards")
data class ScorecardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val teamName: String,
    val opponentsName: String,
    val venue: String,
    val title: String,
    val matchDate: String,
    val battingSide: String,
    val umpire1Name: String? = null,
    val umpire2Name: String? = null,
    val thirdUmpireName: String? = null,
    val refereeName: String? = null,
    val scorer1Name: String,
    val scorer2Name: String? = null,
    val typeOfMatch: String,
    val duration: String,
    val startTime: String,
    val teamWinningToss: String,
    val weather: String? = null,
    val pitchCondition: String? = null,
    @ColumnInfo(defaultValue = "0")
    val dateSaved: Instant = Clock.System.now(),
    @ColumnInfo(defaultValue = "0")
    val deleted: Boolean
)

fun ScorecardHeaderDetails.toEntity(): ScorecardEntity {
    return ScorecardEntity(
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
        pitchCondition = pitchCondition,
        dateSaved = Clock.System.now(),
        deleted = false
    )

}

fun ScorecardEntity.toScorecardHeaderDetails(): ScorecardHeaderDetails {
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
