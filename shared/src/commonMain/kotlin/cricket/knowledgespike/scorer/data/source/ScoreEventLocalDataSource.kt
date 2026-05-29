package cricket.knowledgespike.scorer.data.source

import cricket.knowledgespike.scorer.data.entity.ScoreEventEntity

interface ScoreEventLocalDataSource {
    suspend fun insertScoreEvent(scoreEventEntity: ScoreEventEntity): Long

    suspend fun listScoreEvents(matchId: Long): List<ScoreEventEntity>

    suspend fun maxSequenceNumber(matchId: Long): Long
}

class RoomScoreEventLocalDataSource(
    private val scoreEventDao: ScoreEventDao,
) : ScoreEventLocalDataSource {
    override suspend fun insertScoreEvent(scoreEventEntity: ScoreEventEntity): Long {
        return scoreEventDao.insert(scoreEventEntity)
    }

    override suspend fun listScoreEvents(matchId: Long): List<ScoreEventEntity> {
        return scoreEventDao.listForMatch(matchId)
    }

    override suspend fun maxSequenceNumber(matchId: Long): Long {
        return scoreEventDao.maxSequenceNumberForMatch(matchId)
    }
}
