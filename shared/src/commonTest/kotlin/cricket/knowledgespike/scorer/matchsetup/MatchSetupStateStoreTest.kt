package cricket.knowledgespike.scorer.matchsetup

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cricket.knowledgespike.scorer.domain.match.CreateAndSaveMatchUseCase
import cricket.knowledgespike.scorer.domain.matchsetup.CreateMatchSetupUseCase
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSchedule
import cricket.knowledgespike.scorer.domain.matchsetup.MatchScheduleType
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.matchsetup.TossDecision
import cricket.knowledgespike.scorer.domain.matchsetup.TossWinner
import cricket.knowledgespike.scorer.domain.model.MatchSummary
import cricket.knowledgespike.scorer.domain.model.StoredMatch
import cricket.knowledgespike.scorer.domain.repository.MatchPersistenceError
import cricket.knowledgespike.scorer.domain.repository.MatchRepository
import cricket.knowledgespike.scorer.navigation.ScorerRoute
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MatchSetupStateStoreTest {

    private val createMatchSetupUseCase = CreateMatchSetupUseCase()

    @Test
    fun `given blank screen state when start match requested then validation error is shown`() {
        val reducedState = reduceMatchSetupScreenState(
            currentState = MatchSetupScreenState(),
            event = MatchSetupScreenEvent.StartMatchRequested,
            createMatchSetupUseCase = createMatchSetupUseCase,
        )

        assertFalse(reducedState.canStartMatch)
        val validationError = assertIs<MatchSetupStartMatchResult.ValidationError>(reducedState.startMatchResult)
        assertEquals("Team A name is required", validationError.message)
    }

    @Test
    fun `given required fields when start match requested then match setup is ready`() {
        val stateWithRequiredFields = MatchSetupScreenState(
            formState = MatchSetupFormState(
                teamAName = "Falcons",
                teamBName = "Kings",
                scheduleAmount = "20",
                tossWinner = TossWinner.TeamA,
                tossDecision = TossDecision.Bat,
                matchDate = "2026-05-25",
            ),
            canStartMatch = true,
        )

        val reducedState = reduceMatchSetupScreenState(
            currentState = stateWithRequiredFields,
            event = MatchSetupScreenEvent.StartMatchRequested,
            createMatchSetupUseCase = createMatchSetupUseCase,
        )

        assertTrue(reducedState.canStartMatch)
        assertIs<MatchSetupStartMatchResult.Ready>(reducedState.startMatchResult)
    }

    @Test
    fun `given matching team names when required fields are otherwise filled then start gate remains blocked until corrected`() {
        val stateWithMatchingTeams = MatchSetupScreenState(
            formState = MatchSetupFormState(
                teamAName = "Falcons",
                teamBName = "falcons",
                scheduleAmount = "20",
                tossWinner = TossWinner.TeamA,
                tossDecision = TossDecision.Bat,
                matchDate = "2026-05-25",
            ),
        )

        val blockedState = reduceMatchSetupScreenState(
            currentState = stateWithMatchingTeams,
            event = MatchSetupScreenEvent.TeamBNameChanged("falcons"),
            createMatchSetupUseCase = createMatchSetupUseCase,
        )

        val correctedState = reduceMatchSetupScreenState(
            currentState = blockedState,
            event = MatchSetupScreenEvent.TeamBNameChanged("Kings"),
            createMatchSetupUseCase = createMatchSetupUseCase,
        )

        assertFalse(blockedState.canStartMatch)
        assertTrue(correctedState.canStartMatch)
    }

    @Test
    fun `given ready result when form changes then result resets to idle and form is updated`() {
        val readyState = MatchSetupScreenState(
            formState = MatchSetupFormState(
                teamAName = "Falcons",
                teamBName = "Kings",
                scheduleAmount = "20",
                tossWinner = TossWinner.TeamA,
                tossDecision = TossDecision.Bat,
                matchDate = "2026-05-25",
            ),
            canStartMatch = true,
            startMatchResult = MatchSetupStartMatchResult.Ready(matchSetup()),
        )

        val reducedState = reduceMatchSetupScreenState(
            currentState = readyState,
            event = MatchSetupScreenEvent.TeamANameChanged("Warriors"),
            createMatchSetupUseCase = createMatchSetupUseCase,
        )

        assertEquals("Warriors", reducedState.formState.teamAName)
        assertTrue(reducedState.canStartMatch)
        assertEquals(MatchSetupStartMatchResult.Idle, reducedState.startMatchResult)
    }

    @Test
    fun `given blank or padded team names when toss winner labels are projected then labels are explicit and deterministic`() {
        val blankLabels = MatchSetupFormState().toTossWinnerOptionLabels()
        assertEquals("Team A (name pending)", blankLabels.teamA)
        assertEquals("Team B (name pending)", blankLabels.teamB)

        val projectedLabels = MatchSetupFormState(
            teamAName = "  Falcons  ",
            teamBName = "   ",
        ).toTossWinnerOptionLabels()

        assertEquals("Falcons", projectedLabels.teamA)
        assertEquals("Team B (name pending)", projectedLabels.teamB)
    }

    @Test
    fun `given selected toss winner when team names are edited repeatedly then winner remains selected and labels follow latest values`() {
        val selectedWinnerState = MatchSetupScreenState(
            formState = MatchSetupFormState(tossWinner = TossWinner.TeamA),
        )

        val firstRenameState = reduceMatchSetupScreenState(
            currentState = selectedWinnerState,
            event = MatchSetupScreenEvent.TeamANameChanged("Falcons"),
            createMatchSetupUseCase = createMatchSetupUseCase,
        )
        val secondRenameState = reduceMatchSetupScreenState(
            currentState = firstRenameState,
            event = MatchSetupScreenEvent.TeamANameChanged("Warriors"),
            createMatchSetupUseCase = createMatchSetupUseCase,
        )
        val repeatedRenameState = reduceMatchSetupScreenState(
            currentState = secondRenameState,
            event = MatchSetupScreenEvent.TeamANameChanged("Warriors"),
            createMatchSetupUseCase = createMatchSetupUseCase,
        )

        assertEquals(TossWinner.TeamA, secondRenameState.formState.tossWinner)
        assertEquals(TossWinner.TeamA, repeatedRenameState.formState.tossWinner)
        assertEquals("Warriors", secondRenameState.formState.toTossWinnerOptionLabels().teamA)
        assertEquals(
            secondRenameState.formState.toTossWinnerOptionLabels(),
            repeatedRenameState.formState.toTossWinnerOptionLabels(),
        )
    }

    @Test
    fun `given valid required details when match date changes to picker boundary values then start gate remains valid`() {
        val validRequiredState = MatchSetupScreenState(
            formState = MatchSetupFormState(
                teamAName = "Falcons",
                teamBName = "Kings",
                scheduleAmount = "20",
                tossWinner = TossWinner.TeamA,
                tossDecision = TossDecision.Bat,
                matchDate = "2026-05-25",
            ),
        )

        val earliestBoundaryState = reduceMatchSetupScreenState(
            currentState = validRequiredState,
            event = MatchSetupScreenEvent.MatchDateChanged("0001-01-01"),
            createMatchSetupUseCase = createMatchSetupUseCase,
        )
        val latestBoundaryState = reduceMatchSetupScreenState(
            currentState = validRequiredState,
            event = MatchSetupScreenEvent.MatchDateChanged("9999-12-31"),
            createMatchSetupUseCase = createMatchSetupUseCase,
        )

        assertTrue(earliestBoundaryState.canStartMatch)
        assertTrue(latestBoundaryState.canStartMatch)
        assertEquals("0001-01-01", earliestBoundaryState.formState.matchDate)
        assertEquals("9999-12-31", latestBoundaryState.formState.matchDate)
    }

    @Test
    fun `given invalid form when start match requested via store then route intent is not emitted`() = runTest {
        var requestedRoute: ScorerRoute? = null
        val fakeMatchRepository = FakeMatchRepository()
        val stateStore = MatchSetupStateStore(
            createMatchSetupUseCase = createMatchSetupUseCase,
            createAndSaveMatchUseCase = CreateAndSaveMatchUseCase(fakeMatchRepository),
            onRouteRequested = { requestedRoute = it },
            coroutineScope = this,
        )

        stateStore.onEvent(MatchSetupScreenEvent.StartMatchRequested)
        testScheduler.advanceUntilIdle()

        assertNull(requestedRoute)
        assertIs<MatchSetupStartMatchResult.ValidationError>(stateStore.screenState.value.startMatchResult)
        assertEquals(0, fakeMatchRepository.createCallsCount)
    }

    @Test
    fun `given valid form when start match requested via store then scoring entry route intent is emitted`() = runTest {
        var requestedRoute: ScorerRoute? = null
        val fakeMatchRepository = FakeMatchRepository(
            createResult = storedMatch(id = 45L).right(),
        )
        val stateStore = MatchSetupStateStore(
            createMatchSetupUseCase = createMatchSetupUseCase,
            createAndSaveMatchUseCase = CreateAndSaveMatchUseCase(fakeMatchRepository),
            onRouteRequested = { requestedRoute = it },
            coroutineScope = this,
        )

        stateStore.onEvent(MatchSetupScreenEvent.TeamANameChanged("Falcons"))
        stateStore.onEvent(MatchSetupScreenEvent.TeamBNameChanged("Kings"))
        stateStore.onEvent(MatchSetupScreenEvent.ScheduleAmountChanged("20"))
        stateStore.onEvent(MatchSetupScreenEvent.TossWinnerChanged(TossWinner.TeamA))
        stateStore.onEvent(MatchSetupScreenEvent.TossDecisionChanged(TossDecision.Bat))
        stateStore.onEvent(MatchSetupScreenEvent.MatchDateChanged("2026-05-25"))

        stateStore.onEvent(MatchSetupScreenEvent.StartMatchRequested)
        testScheduler.advanceUntilIdle()

        val latestState = stateStore.screenState.value
        assertTrue(latestState.canStartMatch)
        assertEquals(MatchSetupStartMatchResult.Saved(matchId = 45L), latestState.startMatchResult)
        val scoringRoute = assertIs<ScorerRoute.ScoringEntryRoute>(requestedRoute)
        assertEquals(45L, scoringRoute.matchId)
    }

    @Test
    fun `given valid form when start requested repeatedly then emitted route intents are deterministic`() = runTest {
        val requestedRoutes = mutableListOf<ScorerRoute>()
        val fakeMatchRepository = FakeMatchRepository(
            createResult = storedMatch(id = 77L).right(),
        )
        val stateStore = MatchSetupStateStore(
            createMatchSetupUseCase = createMatchSetupUseCase,
            createAndSaveMatchUseCase = CreateAndSaveMatchUseCase(fakeMatchRepository),
            onRouteRequested = { requestedRoutes.add(it) },
            coroutineScope = this,
        )

        stateStore.onEvent(MatchSetupScreenEvent.TeamANameChanged("Falcons"))
        stateStore.onEvent(MatchSetupScreenEvent.TeamBNameChanged("Kings"))
        stateStore.onEvent(MatchSetupScreenEvent.ScheduleAmountChanged("20"))
        stateStore.onEvent(MatchSetupScreenEvent.TossWinnerChanged(TossWinner.TeamA))
        stateStore.onEvent(MatchSetupScreenEvent.TossDecisionChanged(TossDecision.Bat))
        stateStore.onEvent(MatchSetupScreenEvent.MatchDateChanged("2026-05-25"))

        stateStore.onEvent(MatchSetupScreenEvent.StartMatchRequested)
        stateStore.onEvent(MatchSetupScreenEvent.StartMatchRequested)
        testScheduler.advanceUntilIdle()

        assertEquals(2, requestedRoutes.size)
        val firstRoute = assertIs<ScorerRoute.ScoringEntryRoute>(requestedRoutes[0])
        val secondRoute = assertIs<ScorerRoute.ScoringEntryRoute>(requestedRoutes[1])
        assertEquals(firstRoute, secondRoute)
        assertEquals(77L, firstRoute.matchId)
    }

    @Test
    fun `given validation error when user corrects fields and retries then store transitions to ready and emits route`() = runTest {
        var requestedRoute: ScorerRoute? = null
        val fakeMatchRepository = FakeMatchRepository(
            createResult = storedMatch(id = 88L).right(),
        )
        val stateStore = MatchSetupStateStore(
            createMatchSetupUseCase = createMatchSetupUseCase,
            createAndSaveMatchUseCase = CreateAndSaveMatchUseCase(fakeMatchRepository),
            onRouteRequested = { requestedRoute = it },
            coroutineScope = this,
        )

        stateStore.onEvent(MatchSetupScreenEvent.StartMatchRequested)
        assertIs<MatchSetupStartMatchResult.ValidationError>(stateStore.screenState.value.startMatchResult)

        stateStore.onEvent(MatchSetupScreenEvent.TeamANameChanged("Falcons"))
        stateStore.onEvent(MatchSetupScreenEvent.TeamBNameChanged("Kings"))
        stateStore.onEvent(MatchSetupScreenEvent.ScheduleAmountChanged("20"))
        stateStore.onEvent(MatchSetupScreenEvent.TossWinnerChanged(TossWinner.TeamA))
        stateStore.onEvent(MatchSetupScreenEvent.TossDecisionChanged(TossDecision.Bat))
        stateStore.onEvent(MatchSetupScreenEvent.MatchDateChanged("2026-05-25"))
        stateStore.onEvent(MatchSetupScreenEvent.StartMatchRequested)
        testScheduler.advanceUntilIdle()

        val latestState = stateStore.screenState.value
        assertTrue(latestState.canStartMatch)
        assertEquals(MatchSetupStartMatchResult.Saved(matchId = 88L), latestState.startMatchResult)
        val scoringRoute = assertIs<ScorerRoute.ScoringEntryRoute>(requestedRoute)
        assertEquals(88L, scoringRoute.matchId)
    }

    @Test
    fun `given persistence failure when start match requested then save error is shown and no route is emitted`() = runTest {
        var requestedRoute: ScorerRoute? = null
        val stateStore = MatchSetupStateStore(
            createMatchSetupUseCase = createMatchSetupUseCase,
            createAndSaveMatchUseCase = CreateAndSaveMatchUseCase(
                FakeMatchRepository(
                    createResult = MatchPersistenceError.UnableToWriteMatch("disk full").left(),
                ),
            ),
            onRouteRequested = { requestedRoute = it },
            coroutineScope = this,
        )

        stateStore.onEvent(MatchSetupScreenEvent.TeamANameChanged("Falcons"))
        stateStore.onEvent(MatchSetupScreenEvent.TeamBNameChanged("Kings"))
        stateStore.onEvent(MatchSetupScreenEvent.ScheduleAmountChanged("20"))
        stateStore.onEvent(MatchSetupScreenEvent.TossWinnerChanged(TossWinner.TeamA))
        stateStore.onEvent(MatchSetupScreenEvent.TossDecisionChanged(TossDecision.Bat))
        stateStore.onEvent(MatchSetupScreenEvent.MatchDateChanged("2026-05-25"))
        stateStore.onEvent(MatchSetupScreenEvent.StartMatchRequested)
        testScheduler.advanceUntilIdle()

        val latestState = stateStore.screenState.value
        assertTrue(latestState.canStartMatch)
        assertEquals(
            MatchSetupStartMatchResult.PersistenceError("Unable to save the match. Please try again."),
            latestState.startMatchResult,
        )
        assertNull(requestedRoute)
    }

    @Test
    fun `given started match state when reset requested then initial state is restored`() {
        val startedState = MatchSetupScreenState(
            formState = MatchSetupFormState(teamAName = "Falcons"),
            canStartMatch = true,
            startMatchResult = MatchSetupStartMatchResult.ValidationError("Sample error"),
        )

        val reducedState = reduceMatchSetupScreenState(
            currentState = startedState,
            event = MatchSetupScreenEvent.ResetRequested,
            createMatchSetupUseCase = createMatchSetupUseCase,
        )

        assertEquals(MatchSetupScreenState(), reducedState)
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
            venue = null,
            umpireOne = null,
            umpireTwo = null,
            weather = null,
        )
    }

    private fun storedMatch(id: Long): StoredMatch {
        return StoredMatch(
            id = id,
            matchSetup = matchSetup(),
            createdAtEpochMillis = 10L,
            updatedAtEpochMillis = 10L,
        )
    }
}

private class FakeMatchRepository(
    private val createResult: Either<MatchPersistenceError, StoredMatch> =
        StoredMatch(
            id = 1L,
            matchSetup = MatchSetup(
                teamAName = "Falcons",
                teamBName = "Kings",
                schedule = MatchSchedule(type = MatchScheduleType.Overs, amount = 20),
                tossWinner = TossWinner.TeamA,
                tossDecision = TossDecision.Bat,
                matchDate = LocalDate.parse("2026-05-25"),
                venue = null,
                umpireOne = null,
                umpireTwo = null,
                weather = null,
            ),
            createdAtEpochMillis = 10L,
            updatedAtEpochMillis = 10L,
        ).right(),
) : MatchRepository {
    var createCallsCount: Int = 0
        private set

    override suspend fun createMatchFromSetup(matchSetup: MatchSetup): Either<MatchPersistenceError, StoredMatch> {
        createCallsCount += 1
        return createResult
    }

    override suspend fun listStoredMatches(): Either<MatchPersistenceError, List<StoredMatch>> {
        return emptyList<StoredMatch>().right()
    }

    override suspend fun deleteMatch(matchId: Long): Either<MatchPersistenceError, Unit> {
        return MatchPersistenceError.UnableToDeleteMatch("not used").left()
    }

    override suspend fun getMatchSummary(matchId: Long): Either<MatchPersistenceError, MatchSummary> {
        return MatchPersistenceError.MatchNotFound(matchId).left()
    }
}
