package cricket.knowledgespike.scorer.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cricket.knowledgespike.scorer.data.entity.MatchEntity

@Dao
interface MatchDao {
    @Insert
    suspend fun insert(matchEntity: MatchEntity): Long

    @Query("SELECT * FROM `match` WHERE id = :matchId LIMIT 1")
    suspend fun getById(matchId: Long): MatchEntity?

    @Query("SELECT * FROM `match` ORDER BY created_at_epoch_millis DESC")
    suspend fun listAll(): List<MatchEntity>

    @Query("DELETE FROM `match` WHERE id = :matchId")
    suspend fun deleteById(matchId: Long): Int
}
