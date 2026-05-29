package cricket.knowledgespike.scorer.data.entity

import cricket.knowledgespike.scorer.domain.matchsetup.MatchSchedule
import cricket.knowledgespike.scorer.domain.matchsetup.MatchScheduleType
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner
import cricket.knowledgespike.scorer.domain.model.NewScoreEvent
import cricket.knowledgespike.scorer.domain.repository.MatchPersistenceError
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class EntityMappingsTest {

    @Test
    fun `given match setup when mapping to entity then values are persisted-friendly`() {
        val nowEpochMillis = 1000L

        val entity = matchSetup().toEntity(nowEpochMillis = nowEpochMillis)

        assertEquals("Falcons", entity.teamAName)
        assertEquals("Kings", entity.teamBName)
        assertEquals("Overs", entity.scheduleType)
        assertEquals(20, entity.scheduleAmount)
        assertEquals("TeamA", entity.tossWinner)
        assertEquals("Bat", entity.tossDecision)
        assertEquals(LocalDate.parse("2026-05-25").toEpochDays(), entity.matchDateEpochDays)
        assertEquals(nowEpochMillis, entity.createdAtEpochMillis)
        assertEquals(nowEpochMillis, entity.updatedAtEpochMillis)
    }

    @Test
    fun `given valid match entity when mapping to domain then stored match is restored`() {
        val entity = MatchEntity(
            id = 9L,
            teamAName = "Falcons",
            teamBName = "Kings",
            scheduleType = MatchScheduleType.Overs.name,
            scheduleAmount = 20,
            tossWinner = TossWinner.TeamA.name,
            tossDecision = TossDecision.Bat.name,
            matchDateEpochDays = LocalDate.parse("2026-05-25").toEpochDays(),
            venue = "Oval",
            umpireOne = "U1",
            umpireTwo = "U2",
            weather = "Sunny",
            createdAtEpochMillis = 100L,
            updatedAtEpochMillis = 200L,
        )

        val mappingResult = entity.toDomain()

        assertTrue(mappingResult.isRight())
        val storedMatch = mappingResult.getOrNull()
        assertEquals(9L, storedMatch?.id)
        assertEquals("Falcons", storedMatch?.matchSetup?.teamAName)
        assertEquals("Kings", storedMatch?.matchSetup?.teamBName)
        assertEquals(LocalDate.parse("2026-05-25"), storedMatch?.matchSetup?.matchDate)
    }

    @Test
    fun `given invalid enum in match entity when mapping to domain then invalid data error is returned`() {
        val invalidEntity = MatchEntity(
            id = 1L,
            teamAName = "Falcons",
            teamBName = "Kings",
            scheduleType = "UnknownSchedule",
            scheduleAmount = 20,
            tossWinner = TossWinner.TeamA.name,
            tossDecision = TossDecision.Bat.name,
            matchDateEpochDays = LocalDate.parse("2026-05-25").toEpochDays(),
            venue = null,
            umpireOne = null,
            umpireTwo = null,
            weather = null,
            createdAtEpochMillis = 100L,
            updatedAtEpochMillis = 100L,
        )

        val mappingResult = invalidEntity.toDomain()

        assertTrue(mappingResult.isLeft())
        assertIs<MatchPersistenceError.InvalidStoredMatchData>(mappingResult.leftOrNull())
    }

    @Test
    fun `given new score event when mapping to entity and domain then values are preserved`() {
        val entity = NewScoreEvent(eventType = "DotBall", payload = "{}").toEntity(
            matchId = 42L,
            sequenceNumber = 3L,
            nowEpochMillis = 500L,
        )
        val domainEvent = entity.copy(id = 7L).toDomain()

        assertEquals(42L, entity.matchId)
        assertEquals(3L, entity.sequenceNumber)
        assertEquals("DotBall", entity.eventType)
        assertEquals(7L, domainEvent.id)
        assertEquals(42L, domainEvent.matchId)
        assertEquals(3L, domainEvent.sequenceNumber)
        assertEquals("DotBall", domainEvent.eventType)
    }

    private fun matchSetup(): MatchSetup {
        return MatchSetup(
            teamAName = "Falcons",
            teamBName = "Kings",
            schedule = MatchSchedule(
                type = MatchScheduleType.Overs,
                amount = 20,
            ),
            tossWinner = TossWinner.TeamA,
            tossDecision = TossDecision.Bat,
            matchDate = LocalDate.parse("2026-05-25"),
            venue = "Oval",
            umpireOne = "U1",
            umpireTwo = "U2",
            weather = "Sunny",
        )
    }
}
