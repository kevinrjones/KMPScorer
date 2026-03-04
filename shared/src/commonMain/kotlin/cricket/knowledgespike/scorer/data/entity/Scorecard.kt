package cricket.knowledgespike.scorer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

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