package com.knowledgespike.scorer.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import cricket.knowledgespike.scorer.data.entity.Scorecard
import kotlinx.coroutines.flow.Flow

@Dao
interface ScorecardDao {
    @Query("SELECT * FROM Scorecards")
    fun getScorecards() : Flow<List<Scorecard>>

    @Query("SELECT * FROM Scorecards WHERE id = :id")
    suspend fun getScorecard(id: Int) : Scorecard?

    @Delete
    suspend fun deleteScorecard(scorecard: Scorecard)

    @Upsert
    suspend fun upsertScorecard(scorecard: Scorecard)

}