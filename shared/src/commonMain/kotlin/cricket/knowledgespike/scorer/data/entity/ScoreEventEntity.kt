package cricket.knowledgespike.scorer.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "score_event",
    foreignKeys = [
        ForeignKey(
            entity = MatchEntity::class,
            parentColumns = ["id"],
            childColumns = ["match_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["match_id"]),
        Index(value = ["match_id", "sequence_number"], unique = true),
    ],
)
data class ScoreEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "match_id")
    val matchId: Long,
    @ColumnInfo(name = "sequence_number")
    val sequenceNumber: Long,
    @ColumnInfo(name = "event_type")
    val eventType: String,
    @ColumnInfo(name = "payload")
    val payload: String?,
    @ColumnInfo(name = "created_at_epoch_millis")
    val createdAtEpochMillis: Long,
)
