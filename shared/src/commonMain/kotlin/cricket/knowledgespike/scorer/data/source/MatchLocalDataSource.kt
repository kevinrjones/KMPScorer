package cricket.knowledgespike.scorer.data.source

import cricket.knowledgespike.scorer.data.entity.MatchEntity

interface MatchLocalDataSource {
    suspend fun insertMatch(matchEntity: MatchEntity): Long

    suspend fun getMatchById(matchId: Long): MatchEntity?

    suspend fun listMatches(): List<MatchEntity>

    suspend fun deleteMatch(matchId: Long): Boolean
}

class RoomMatchLocalDataSource(
    private val matchDao: MatchDao,
) : MatchLocalDataSource {
    override suspend fun insertMatch(matchEntity: MatchEntity): Long {
        return matchDao.insert(matchEntity)
    }

    override suspend fun getMatchById(matchId: Long): MatchEntity? {
        return matchDao.getById(matchId)
    }

    override suspend fun listMatches(): List<MatchEntity> {
        return matchDao.listAll()
    }

    override suspend fun deleteMatch(matchId: Long): Boolean {
        return matchDao.deleteById(matchId) > 0
    }
}
