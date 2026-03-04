package com.knowledgespike.scorer.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import cricket.knowledgespike.scorer.data.entity.ScorecardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScorecardDao {
    @Query("SELECT * FROM Scorecards")
    fun getScorecards() : Flow<List<ScorecardEntity>>

    @Query("SELECT * FROM Scorecards WHERE id = :id")
    suspend fun getScorecard(id: Int) : ScorecardEntity?

    @Delete
    suspend fun deleteScorecard(scorecard: ScorecardEntity) : Int

    @Upsert
    suspend fun upsertScorecard(scorecard: ScorecardEntity)

}