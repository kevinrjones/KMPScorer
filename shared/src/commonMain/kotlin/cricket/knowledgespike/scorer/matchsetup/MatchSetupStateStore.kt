package cricket.knowledgespike.scorer.matchsetup

import cricket.knowledgespike.scorer.domain.matchsetup.CreateMatchSetupUseCase
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetupDraft
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetupValidationError
import cricket.knowledgespike.scorer.navigation.ScorerRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MatchSetupStateStore(
    private val createMatchSetupUseCase: CreateMatchSetupUseCase,
    private val onRouteRequested: (ScorerRoute) -> Unit = {},
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
                onRouteRequested(ScorerRoute.ScoringEntryRoute(matchSetup))
            }
        }
    }
}

fun reduceMatchSetupScreenState(
    currentState: MatchSetupScreenState,
    event: MatchSetupScreenEvent,
    createMatchSetupUseCase: CreateMatchSetupUseCase,
): MatchSetupScreenState {
    return when (event) {
        is MatchSetupScreenEvent.TeamANameChanged -> {
            currentState.withUpdatedForm {
                copy(teamAName = event.value)
            }.evaluatingStartGateWith(createMatchSetupUseCase)
        }

        is MatchSetupScreenEvent.TeamBNameChanged -> {
            currentState.withUpdatedForm {
                copy(teamBName = event.value)
            }.evaluatingStartGateWith(createMatchSetupUseCase)
        }

        is MatchSetupScreenEvent.ScheduleTypeChanged -> {
            currentState.withUpdatedForm {
                copy(scheduleType = event.value)
            }.evaluatingStartGateWith(createMatchSetupUseCase)
        }

        is MatchSetupScreenEvent.ScheduleAmountChanged -> {
            currentState.withUpdatedForm {
                copy(scheduleAmount = event.value)
            }.evaluatingStartGateWith(createMatchSetupUseCase)
        }

        is MatchSetupScreenEvent.TossWinnerChanged -> {
            currentState.withUpdatedForm {
                copy(tossWinner = event.value)
            }.evaluatingStartGateWith(createMatchSetupUseCase)
        }

        is MatchSetupScreenEvent.TossDecisionChanged -> {
            currentState.withUpdatedForm {
                copy(tossDecision = event.value)
            }.evaluatingStartGateWith(createMatchSetupUseCase)
        }

        is MatchSetupScreenEvent.MatchDateChanged -> {
            currentState.withUpdatedForm {
                copy(matchDate = event.value)
            }.evaluatingStartGateWith(createMatchSetupUseCase)
        }

        is MatchSetupScreenEvent.VenueChanged -> {
            currentState.withUpdatedForm {
                copy(venue = event.value)
            }.evaluatingStartGateWith(createMatchSetupUseCase)
        }

        is MatchSetupScreenEvent.UmpireOneChanged -> {
            currentState.withUpdatedForm {
                copy(umpireOne = event.value)
            }.evaluatingStartGateWith(createMatchSetupUseCase)
        }

        is MatchSetupScreenEvent.UmpireTwoChanged -> {
            currentState.withUpdatedForm {
                copy(umpireTwo = event.value)
            }.evaluatingStartGateWith(createMatchSetupUseCase)
        }

        is MatchSetupScreenEvent.WeatherChanged -> {
            currentState.withUpdatedForm {
                copy(weather = event.value)
            }.evaluatingStartGateWith(createMatchSetupUseCase)
        }

        MatchSetupScreenEvent.StartMatchRequested -> {
            val createMatchSetupResult = createMatchSetupUseCase(currentState.formState.toMatchSetupDraft())
            createMatchSetupResult.fold(
                ifLeft = { validationError ->
                    currentState.copy(
                        canStartMatch = false,
                        startMatchResult = MatchSetupStartMatchResult.ValidationError(validationError.toUiMessage()),
                    )
                },
                ifRight = { setup ->
                    currentState.copy(
                        canStartMatch = true,
                        startMatchResult = MatchSetupStartMatchResult.Ready(setup),
                    )
                },
            )
        }

        MatchSetupScreenEvent.ResetRequested -> MatchSetupScreenState()
    }
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
