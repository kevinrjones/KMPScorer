package cricket.knowledgespike.scorer.data.entity

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSchedule
import cricket.knowledgespike.scorer.domain.matchsetup.MatchScheduleType
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner
import cricket.knowledgespike.scorer.domain.model.NewScoreEvent
import cricket.knowledgespike.scorer.domain.model.ScoreEvent
import cricket.knowledgespike.scorer.domain.model.StoredMatch
import cricket.knowledgespike.scorer.domain.repository.MatchPersistenceError
import kotlinx.datetime.LocalDate

fun MatchSetup.toEntity(nowEpochMillis: Long): MatchEntity {
    return MatchEntity(
        teamAName = teamAName,
        teamBName = teamBName,
        scheduleType = schedule.type.name,
        scheduleAmount = schedule.amount,
        tossWinner = tossWinner.name,
        tossDecision = tossDecision.name,
        matchDateEpochDays = matchDate.toEpochDays(),
        venue = venue,
        umpireOne = umpireOne,
        umpireTwo = umpireTwo,
        weather = weather,
        createdAtEpochMillis = nowEpochMillis,
        updatedAtEpochMillis = nowEpochMillis,
    )
}

fun MatchEntity.toDomain(): Either<MatchPersistenceError.InvalidStoredMatchData, StoredMatch> {
    val scheduleType = enumValueOrNull<MatchScheduleType>(scheduleType)
        ?: return MatchPersistenceError.InvalidStoredMatchData("Unsupported schedule type '$scheduleType'").left()
    val tossWinner = enumValueOrNull<TossWinner>(tossWinner)
        ?: return MatchPersistenceError.InvalidStoredMatchData("Unsupported toss winner '$tossWinner'").left()
    val tossDecision = enumValueOrNull<TossDecision>(tossDecision)
        ?: return MatchPersistenceError.InvalidStoredMatchData("Unsupported toss decision '$tossDecision'").left()

    return StoredMatch(
        id = id,
        matchSetup = MatchSetup(
            teamAName = teamAName,
            teamBName = teamBName,
            schedule = MatchSchedule(type = scheduleType, amount = scheduleAmount),
            tossWinner = tossWinner,
            tossDecision = tossDecision,
            matchDate = LocalDate.fromEpochDays(matchDateEpochDays),
            venue = venue,
            umpireOne = umpireOne,
            umpireTwo = umpireTwo,
            weather = weather,
        ),
        createdAtEpochMillis = createdAtEpochMillis,
        updatedAtEpochMillis = updatedAtEpochMillis,
    ).right()
}

fun NewScoreEvent.toEntity(
    matchId: Long,
    sequenceNumber: Long,
    nowEpochMillis: Long,
): ScoreEventEntity {
    return ScoreEventEntity(
        matchId = matchId,
        sequenceNumber = sequenceNumber,
        eventType = eventType,
        payload = payload,
        createdAtEpochMillis = nowEpochMillis,
    )
}

fun ScoreEventEntity.toDomain(): ScoreEvent {
    return ScoreEvent(
        id = id,
        matchId = matchId,
        sequenceNumber = sequenceNumber,
        eventType = eventType,
        payload = payload,
        createdAtEpochMillis = createdAtEpochMillis,
    )
}

private inline fun <reified T : Enum<T>> enumValueOrNull(rawValue: String): T? {
    return enumValues<T>().firstOrNull { it.name == rawValue }
}
