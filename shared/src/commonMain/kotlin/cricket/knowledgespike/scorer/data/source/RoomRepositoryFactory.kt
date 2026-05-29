package cricket.knowledgespike.scorer.data.source

import cricket.knowledgespike.scorer.domain.repository.MatchRepository
import cricket.knowledgespike.scorer.domain.repository.ScoreEventRepository
import cricket.knowledgespike.scorer.foundation.room.ScorecardDatabase

fun createRoomMatchRepository(scorecardDatabase: ScorecardDatabase): MatchRepository {
    return RoomMatchRepository(
        matchLocalDataSource = RoomMatchLocalDataSource(scorecardDatabase.matchDao()),
        scoreEventLocalDataSource = RoomScoreEventLocalDataSource(scorecardDatabase.scoreEventDao()),
    )
}

fun createRoomScoreEventRepository(scorecardDatabase: ScorecardDatabase): ScoreEventRepository {
    return RoomScoreEventRepository(
        scoreEventLocalDataSource = RoomScoreEventLocalDataSource(scorecardDatabase.scoreEventDao()),
    )
}
