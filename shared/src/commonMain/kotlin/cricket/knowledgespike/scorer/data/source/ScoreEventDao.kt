package cricket.knowledgespike.scorer.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cricket.knowledgespike.scorer.data.entity.ScoreEventEntity

@Dao
interface ScoreEventDao {
    @Insert
    suspend fun insert(scoreEventEntity: ScoreEventEntity): Long

    @Query("SELECT * FROM score_event WHERE match_id = :matchId ORDER BY sequence_number ASC")
    suspend fun listForMatch(matchId: Long): List<ScoreEventEntity>

    @Query("SELECT COALESCE(MAX(sequence_number), 0) FROM score_event WHERE match_id = :matchId")
    suspend fun maxSequenceNumberForMatch(matchId: Long): Long
}
