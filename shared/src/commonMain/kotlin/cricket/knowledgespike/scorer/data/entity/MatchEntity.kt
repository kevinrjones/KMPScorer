package cricket.knowledgespike.scorer.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match")
data class MatchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "team_a_name")
    val teamAName: String,
    @ColumnInfo(name = "team_b_name")
    val teamBName: String,
    @ColumnInfo(name = "schedule_type")
    val scheduleType: String,
    @ColumnInfo(name = "schedule_amount")
    val scheduleAmount: Int,
    @ColumnInfo(name = "toss_winner")
    val tossWinner: String,
    @ColumnInfo(name = "toss_decision")
    val tossDecision: String,
    @ColumnInfo(name = "match_date_epoch_days")
    val matchDateEpochDays: Long,
    @ColumnInfo(name = "venue")
    val venue: String?,
    @ColumnInfo(name = "umpire_one")
    val umpireOne: String?,
    @ColumnInfo(name = "umpire_two")
    val umpireTwo: String?,
    @ColumnInfo(name = "weather")
    val weather: String?,
    @ColumnInfo(name = "created_at_epoch_millis")
    val createdAtEpochMillis: Long,
    @ColumnInfo(name = "updated_at_epoch_millis")
    val updatedAtEpochMillis: Long,
)
