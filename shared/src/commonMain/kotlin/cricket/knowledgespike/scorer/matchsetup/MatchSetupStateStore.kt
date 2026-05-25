package cricket.knowledgespike.scorer.matchsetup

import cricket.knowledgespike.scorer.domain.matchsetup.CreateMatchSetupUseCase
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetupDraft
import cricket.knowledgespike.scorer.domain.matchsetup.MatchSetupValidationError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MatchSetupStateStore(
    private val createMatchSetupUseCase: CreateMatchSetupUseCase,
) {

    private val _screenState = MutableStateFlow(MatchSetupScreenState())
    val screenState: StateFlow<MatchSetupScreenState> = _screenState.asStateFlow()

    fun onEvent(event: MatchSetupScreenEvent) {
        _screenState.update { currentState ->
            reduceMatchSetupScreenState(
                currentState = currentState,
                event = event,
                createMatchSetupUseCase = createMatchSetupUseCase,
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
        is MatchSetupScreenEvent.TeamANameChanged -> {
            currentState.withUpdatedForm {
                copy(teamAName = event.value)
            }
        }

        is MatchSetupScreenEvent.TeamBNameChanged -> {
            currentState.withUpdatedForm {
                copy(teamBName = event.value)
            }
        }

        is MatchSetupScreenEvent.ScheduledOversChanged -> {
            currentState.withUpdatedForm {
                copy(scheduledOvers = event.value)
            }
        }

        is MatchSetupScreenEvent.TossWinnerChanged -> {
            currentState.withUpdatedForm {
                copy(tossWinner = event.value)
            }
        }

        is MatchSetupScreenEvent.TossDecisionChanged -> {
            currentState.withUpdatedForm {
                copy(tossDecision = event.value)
            }
        }

        is MatchSetupScreenEvent.MatchDateChanged -> {
            currentState.withUpdatedForm {
                copy(matchDate = event.value)
            }
        }

        is MatchSetupScreenEvent.VenueChanged -> {
            currentState.withUpdatedForm {
                copy(venue = event.value)
            }
        }

        is MatchSetupScreenEvent.UmpireOneChanged -> {
            currentState.withUpdatedForm {
                copy(umpireOne = event.value)
            }
        }

        is MatchSetupScreenEvent.UmpireTwoChanged -> {
            currentState.withUpdatedForm {
                copy(umpireTwo = event.value)
            }
        }

        is MatchSetupScreenEvent.WeatherChanged -> {
            currentState.withUpdatedForm {
                copy(weather = event.value)
            }
        }

        MatchSetupScreenEvent.StartMatchRequested -> {
            val createMatchSetupResult = createMatchSetupUseCase(currentState.formState.toMatchSetupDraft())
            createMatchSetupResult.fold(
                ifLeft = { validationError ->
                    currentState.copy(
                        canStartMatch = currentState.formState.canStartMatch(),
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

private fun MatchSetupScreenState.withUpdatedForm(update: MatchSetupFormState.() -> MatchSetupFormState): MatchSetupScreenState {
    val updatedFormState = formState.update()
    return copy(
        formState = updatedFormState,
        canStartMatch = updatedFormState.canStartMatch(),
        startMatchResult = MatchSetupStartMatchResult.Idle,
    )
}

private fun MatchSetupFormState.toMatchSetupDraft(): MatchSetupDraft {
    return MatchSetupDraft(
        teamAName = teamAName,
        teamBName = teamBName,
        scheduledOvers = scheduledOvers,
        tossWinner = tossWinner,
        tossDecision = tossDecision,
        matchDate = matchDate,
        venue = venue,
        umpireOne = umpireOne,
        umpireTwo = umpireTwo,
        weather = weather,
    )
}

private fun MatchSetupFormState.canStartMatch(): Boolean {
    return teamAName.isNotBlank() &&
        teamBName.isNotBlank() &&
        scheduledOvers.isNotBlank() &&
        tossWinner != null &&
        tossDecision != null &&
        matchDate.isNotBlank()
}

private fun MatchSetupValidationError.toUiMessage(): String {
    return when (this) {
        MatchSetupValidationError.MissingTeamAName -> "Team A name is required"
        MatchSetupValidationError.MissingTeamBName -> "Team B name is required"
        MatchSetupValidationError.TeamNamesMustDiffer -> "Team names must be different"
        MatchSetupValidationError.InvalidScheduledOvers -> "Scheduled overs must be greater than zero"
        MatchSetupValidationError.MissingTossWinner -> "Choose the toss winner"
        MatchSetupValidationError.MissingTossDecision -> "Choose the toss decision"
        MatchSetupValidationError.InvalidMatchDate -> "Match date must use YYYY-MM-DD"
    }
}
