package cricket.knowledgespike.scorer.home

import arrow.core.Either
import arrow.core.left
import arrow.core.right
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
import kotlin.test.assertIs

class HomeStateStoreTest {

    @Test
    fun `given stored matches when store is created then content state is emitted`() = runTest {
        val stateStore = HomeStateStore(
            matchRepository = FakeHomeMatchRepository(
                storedMatches = listOf(storedMatch(id = 10L)),
            ),
            onRouteRequested = {},
            coroutineScope = this,
        )

        testScheduler.advanceUntilIdle()

        val contentState = assertIs<HomeMatchListState.Content>(stateStore.screenState.value.listState)
        assertEquals(1, contentState.matches.size)
        assertEquals(10L, contentState.matches.first().id)
    }

    @Test
    fun `given no stored matches when store is created then empty state is emitted`() = runTest {
        val stateStore = HomeStateStore(
            matchRepository = FakeHomeMatchRepository(
                storedMatches = emptyList(),
            ),
            onRouteRequested = {},
            coroutineScope = this,
        )

        testScheduler.advanceUntilIdle()

        assertEquals(HomeMatchListState.Empty, stateStore.screenState.value.listState)
    }

    @Test
    fun `given repository read failure when store is created then error state is emitted`() = runTest {
        val stateStore = HomeStateStore(
            matchRepository = FakeHomeMatchRepository(
                listFailure = MatchPersistenceError.UnableToReadMatches("broken"),
            ),
            onRouteRequested = {},
            coroutineScope = this,
        )

        testScheduler.advanceUntilIdle()

        val errorState = assertIs<HomeMatchListState.Error>(stateStore.screenState.value.listState)
        assertEquals("Unable to load saved matches.", errorState.message)
    }

    @Test
    fun `given new match event when handled then route requests match setup`() = runTest {
        var requestedRoute: ScorerRoute? = null
        val stateStore = HomeStateStore(
            matchRepository = FakeHomeMatchRepository(),
            onRouteRequested = { requestedRoute = it },
            coroutineScope = this,
        )

        stateStore.onEvent(HomeScreenEvent.NewMatchRequested)

        assertEquals(ScorerRoute.MatchSetupRoute, requestedRoute)
    }

    @Test
    fun `given inline edit action when requested then route requests match setup`() = runTest {
        var requestedRoute: ScorerRoute? = null
        val stateStore = HomeStateStore(
            matchRepository = FakeHomeMatchRepository(storedMatches = listOf(storedMatch(id = 33L))),
            onRouteRequested = { requestedRoute = it },
            coroutineScope = this,
        )
        testScheduler.advanceUntilIdle()

        stateStore.onEvent(HomeScreenEvent.EditMatchRequested(matchId = 33L))

        assertEquals(ScorerRoute.MatchSetupRoute, requestedRoute)
        assertEquals(HomeMatchDialogState.Hidden, stateStore.screenState.value.dialogState)
    }

    @Test
    fun `given saved match open action when requested then route requests summary for match id`() = runTest {
        var requestedRoute: ScorerRoute? = null
        val stateStore = HomeStateStore(
            matchRepository = FakeHomeMatchRepository(storedMatches = listOf(storedMatch(id = 33L))),
            onRouteRequested = { requestedRoute = it },
            coroutineScope = this,
        )
        testScheduler.advanceUntilIdle()

        stateStore.onEvent(HomeScreenEvent.OpenSavedMatchRequested(matchId = 33L))

        assertEquals(ScorerRoute.MatchSummaryRoute(matchId = 33L), requestedRoute)
        assertEquals(HomeMatchDialogState.Hidden, stateStore.screenState.value.dialogState)
    }

    @Test
    fun `given inline delete action when requested then delete confirmation is shown`() = runTest {
        val stateStore = HomeStateStore(
            matchRepository = FakeHomeMatchRepository(storedMatches = listOf(storedMatch(id = 44L))),
            onRouteRequested = {},
            coroutineScope = this,
        )
        testScheduler.advanceUntilIdle()

        stateStore.onEvent(HomeScreenEvent.DeleteMatchRequested(matchId = 44L))

        assertIs<HomeMatchDialogState.DeleteConfirmation>(stateStore.screenState.value.dialogState).also {
            assertEquals(44L, it.matchId)
            assertEquals("Falcons vs Kings", it.matchTitle)
        }
    }

    @Test
    fun `given delete confirmed when delete succeeds then list refreshes`() = runTest {
        val fakeMatchRepository = FakeHomeMatchRepository(storedMatches = listOf(storedMatch(id = 44L)))
        val stateStore = HomeStateStore(
            matchRepository = fakeMatchRepository,
            onRouteRequested = {},
            coroutineScope = this,
        )
        testScheduler.advanceUntilIdle()

        stateStore.onEvent(HomeScreenEvent.DeleteMatchRequested(matchId = 44L))
        stateStore.onEvent(HomeScreenEvent.DeleteMatchConfirmed(matchId = 44L))
        testScheduler.advanceUntilIdle()

        assertEquals(listOf(44L), fakeMatchRepository.deletedMatchIds)
        assertEquals(HomeMatchListState.Empty, stateStore.screenState.value.listState)
        assertEquals(HomeMatchDialogState.Hidden, stateStore.screenState.value.dialogState)
    }

    @Test
    fun `given delete confirmed when delete fails then error state is shown`() = runTest {
        val stateStore = HomeStateStore(
            matchRepository = FakeHomeMatchRepository(
                storedMatches = listOf(storedMatch(id = 44L)),
                deleteFailure = MatchPersistenceError.UnableToDeleteMatch("locked"),
            ),
            onRouteRequested = {},
            coroutineScope = this,
        )
        testScheduler.advanceUntilIdle()

        stateStore.onEvent(HomeScreenEvent.DeleteMatchRequested(matchId = 44L))
        stateStore.onEvent(HomeScreenEvent.DeleteMatchConfirmed(matchId = 44L))
        testScheduler.advanceUntilIdle()

        val errorState = assertIs<HomeMatchListState.Error>(stateStore.screenState.value.listState)
        assertEquals("Unable to delete the saved match.", errorState.message)
        assertEquals(HomeMatchDialogState.Hidden, stateStore.screenState.value.dialogState)
    }

    private fun storedMatch(id: Long): StoredMatch {
        return StoredMatch(
            id = id,
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
        )
    }
}

private class FakeHomeMatchRepository(
    storedMatches: List<StoredMatch> = emptyList(),
    private val listFailure: MatchPersistenceError? = null,
    private val deleteFailure: MatchPersistenceError? = null,
) : MatchRepository {
    private val mutableStoredMatches = storedMatches.toMutableList()
    val deletedMatchIds = mutableListOf<Long>()

    override suspend fun createMatchFromSetup(matchSetup: MatchSetup): Either<MatchPersistenceError, StoredMatch> {
        return MatchPersistenceError.UnableToWriteMatch("not used").left()
    }

    override suspend fun listStoredMatches(): Either<MatchPersistenceError, List<StoredMatch>> {
        return listFailure?.left() ?: mutableStoredMatches.toList().right()
    }

    override suspend fun deleteMatch(matchId: Long): Either<MatchPersistenceError, Unit> {
        deleteFailure?.let {
            return it.left()
        }

        val deleted = mutableStoredMatches.removeAll { it.id == matchId }
        return if (deleted) {
            deletedMatchIds += matchId
            Unit.right()
        } else {
            MatchPersistenceError.MatchNotFound(matchId).left()
        }
    }

    override suspend fun getMatchSummary(matchId: Long): Either<MatchPersistenceError, MatchSummary> {
        return MatchPersistenceError.MatchNotFound(matchId).left()
    }
}
