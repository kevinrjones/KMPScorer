package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.validation.ValidationReason

class ValidateTeamWinningToss {
    operator fun invoke(teamWinningToss: String, teamName: String, opponentsName: String) : ValidationReason {
        if(teamWinningToss.isBlank()) return ValidationReason.Empty

        return if (teamWinningToss != teamName && teamWinningToss != opponentsName) {
            ValidationReason.BattingSide
        } else {
            ValidationReason.Succeeded
        }
    }
}