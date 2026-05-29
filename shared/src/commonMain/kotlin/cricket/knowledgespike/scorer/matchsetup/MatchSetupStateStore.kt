package cricket.knowledgespike.scorer.matchsetup

import cricket.knowledgespike.scorer.domain.match.CreateAndSaveMatchUseCase
import cricket.knowledgespike.scorer.domain.matchsetup.CreateMatchSetupUseCase
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetup
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetupDraft
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetupValidationError
import cricket.knowledgespike.scorer.domain.repository.MatchPersistenceError
import cricket.knowledgespike.scorer.navigation.ScorerRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MatchSetupStateStore(
    private val createMatchSetupUseCase: CreateMatchSetupUseCase,
    private val createAndSaveMatchUseCase: CreateAndSaveMatchUseCase,
    private val onRouteRequested: (ScorerRoute) -> Unit = {},
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) {

    private val _screenState = MutableStateFlow(MatchSetupScreenState())
    val screenState: StateFlow<MatchSetupScreenState> = _screenState.asStateFlow()

    fun onEvent(event: MatchSetupScreenEvent) {
        var reducedState: MatchSetupScreenState? = null
        _screenState.update { currentState ->
            reduceMatchSetupScreenState(
                currentState = currentState,
                event = event,
                createMatchSetupUseCase = createMatchSetupUseCase,
            ).also { reducedState = it }
        }

        if (event == MatchSetupScreenEvent.StartMatchRequested) {
            val matchSetup = (reducedState?.startMatchResult as? MatchSetupStartMatchResult.Ready)?.matchSetup
            if (matchSetup != null) {
                persistAndNavigate(matchSetup)
            }
        }
    }

    private fun persistAndNavigate(matchSetup: MatchSetup) {
        _screenState.update {
            it.copy(startMatchResult = MatchSetupStartMatchResult.Saving)
        }

        coroutineScope.launch {
            createAndSaveMatchUseCase(matchSetup).fold(
                ifLeft = { persistenceError ->
                    _screenState.update {
                        it.copy(
                            startMatchResult = MatchSetupStartMatchResult.PersistenceError(
                                persistenceError.toUiMessage(),
                            ),
                        )
                    }
                },
                ifRight = { storedMatch ->
                    _screenState.update {
                        it.copy(startMatchResult = MatchSetupStartMatchResult.Saved(storedMatch.id))
                    }
                    onRouteRequested(ScorerRoute.ScoringEntryRoute(matchId = storedMatch.id))
                },
            )
        }
    }
}

fun reduceMatchSetupScreenState(
    currentState: MatchSetupScreenState,
    event: MatchSetupScreenEvent,
    createMatchSetupUseCase: CreateMatchSetupUseCase,
): MatchSetupScreenState {
    return when (event) {
        is MatchSetupScreenEvent.TeamANameChanged,
        is MatchSetupScreenEvent.TeamBNameChanged,
        is MatchSetupScreenEvent.ScheduleTypeChanged,
        is MatchSetupScreenEvent.ScheduleAmountChanged,
        is MatchSetupScreenEvent.MatchDateChanged,
        -> currentState.reduceCoreInfoEvent(event, createMatchSetupUseCase)

        is MatchSetupScreenEvent.TossWinnerChanged,
        is MatchSetupScreenEvent.TossDecisionChanged,
        -> currentState.reduceTossDetailsEvent(event, createMatchSetupUseCase)

        is MatchSetupScreenEvent.VenueChanged,
        is MatchSetupScreenEvent.UmpireOneChanged,
        is MatchSetupScreenEvent.UmpireTwoChanged,
        is MatchSetupScreenEvent.WeatherChanged,
        -> currentState.reduceOptionalMetadataEvent(event, createMatchSetupUseCase)

        MatchSetupScreenEvent.StartMatchRequested -> currentState.reduceStartMatchRequested(createMatchSetupUseCase)

        MatchSetupScreenEvent.ResetRequested -> MatchSetupScreenState()
    }
}

private fun MatchSetupScreenState.reduceCoreInfoEvent(
    event: MatchSetupScreenEvent,
    createMatchSetupUseCase: CreateMatchSetupUseCase,
): MatchSetupScreenState {
    return reduceFormEvent(createMatchSetupUseCase) {
        when (event) {
            is MatchSetupScreenEvent.TeamANameChanged -> copy(teamAName = event.value)
            is MatchSetupScreenEvent.TeamBNameChanged -> copy(teamBName = event.value)
            is MatchSetupScreenEvent.ScheduleTypeChanged -> copy(scheduleType = event.value)
            is MatchSetupScreenEvent.ScheduleAmountChanged -> copy(scheduleAmount = event.value)
            is MatchSetupScreenEvent.MatchDateChanged -> copy(matchDate = event.value)
            else -> this
        }
    }
}

private fun MatchSetupScreenState.reduceTossDetailsEvent(
    event: MatchSetupScreenEvent,
    createMatchSetupUseCase: CreateMatchSetupUseCase,
): MatchSetupScreenState {
    return reduceFormEvent(createMatchSetupUseCase) {
        when (event) {
            is MatchSetupScreenEvent.TossWinnerChanged -> copy(tossWinner = event.value)
            is MatchSetupScreenEvent.TossDecisionChanged -> copy(tossDecision = event.value)
            else -> this
        }
    }
}

private fun MatchSetupScreenState.reduceOptionalMetadataEvent(
    event: MatchSetupScreenEvent,
    createMatchSetupUseCase: CreateMatchSetupUseCase,
): MatchSetupScreenState {
    return reduceFormEvent(createMatchSetupUseCase) {
        when (event) {
            is MatchSetupScreenEvent.VenueChanged -> copy(venue = event.value)
            is MatchSetupScreenEvent.UmpireOneChanged -> copy(umpireOne = event.value)
            is MatchSetupScreenEvent.UmpireTwoChanged -> copy(umpireTwo = event.value)
            is MatchSetupScreenEvent.WeatherChanged -> copy(weather = event.value)
            else -> this
        }
    }
}

private fun MatchSetupScreenState.reduceStartMatchRequested(
    createMatchSetupUseCase: CreateMatchSetupUseCase,
): MatchSetupScreenState {
    val createMatchSetupResult = createMatchSetupUseCase(formState.toMatchSetupDraft())
    return createMatchSetupResult.fold(
        ifLeft = { validationError ->
            copy(
                canStartMatch = false,
                startMatchResult = MatchSetupStartMatchResult.ValidationError(validationError.toUiMessage()),
            )
        },
        ifRight = { setup ->
            copy(
                canStartMatch = true,
                startMatchResult = MatchSetupStartMatchResult.Ready(setup),
            )
        },
    )
}

private fun MatchSetupScreenState.reduceFormEvent(
    createMatchSetupUseCase: CreateMatchSetupUseCase,
    updateForm: MatchSetupFormState.() -> MatchSetupFormState,
): MatchSetupScreenState {
    return withUpdatedForm(updateForm).evaluatingStartGateWith(createMatchSetupUseCase)
}

private fun MatchSetupScreenState.withUpdatedForm(
    update: MatchSetupFormState.() -> MatchSetupFormState,
): MatchSetupScreenState {
    val updatedFormState = formState.update()
    return copy(
        formState = updatedFormState,
        startMatchResult = MatchSetupStartMatchResult.Idle,
    )
}

private fun MatchSetupScreenState.evaluatingStartGateWith(
    createMatchSetupUseCase: CreateMatchSetupUseCase,
): MatchSetupScreenState {
    return copy(
        canStartMatch = formState.isStartGateValid(createMatchSetupUseCase),
    )
}

private fun MatchSetupFormState.toMatchSetupDraft(): MatchSetupDraft {
    return MatchSetupDraft(
        teamAName = teamAName,
        teamBName = teamBName,
        scheduleType = scheduleType,
        scheduleAmount = scheduleAmount,
        tossWinner = tossWinner,
        tossDecision = tossDecision,
        matchDate = matchDate,
        venue = venue,
        umpireOne = umpireOne,
        umpireTwo = umpireTwo,
        weather = weather,
    )
}

private fun MatchSetupFormState.isStartGateValid(createMatchSetupUseCase: CreateMatchSetupUseCase): Boolean {
    return createMatchSetupUseCase(toMatchSetupDraft()).fold(
        ifLeft = { false },
        ifRight = { true },
    )
}

private fun MatchSetupValidationError.toUiMessage(): String {
    return when (this) {
        MatchSetupValidationError.MissingTeamAName -> "Team A name is required"
        MatchSetupValidationError.MissingTeamBName -> "Team B name is required"
        MatchSetupValidationError.TeamNamesMustDiffer -> "Team names must be different"
        MatchSetupValidationError.InvalidScheduleAmount -> "Schedule amount must be between 1 and 999"
        MatchSetupValidationError.MissingTossWinner -> "Choose the toss winner"
        MatchSetupValidationError.MissingTossDecision -> "Choose the toss decision"
        MatchSetupValidationError.InvalidMatchDate -> "Match date must use YYYY-MM-DD"
    }
}

private fun MatchPersistenceError.toUiMessage(): String {
    return when (this) {
        is MatchPersistenceError.UnableToWriteMatch -> "Unable to save the match. Please try again."
        is MatchPersistenceError.UnableToReadMatches -> "Unable to read saved matches. Please try again."
        is MatchPersistenceError.UnableToDeleteMatch -> "Unable to delete saved matches. Please try again."
        is MatchPersistenceError.MatchNotFound -> "Saved match not found. Please start the match again."
        is MatchPersistenceError.InvalidStoredMatchData -> "Saved match data is invalid. Please create a new match."
    }
}
