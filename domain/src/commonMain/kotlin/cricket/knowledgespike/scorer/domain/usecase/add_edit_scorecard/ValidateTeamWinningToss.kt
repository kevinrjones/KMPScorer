package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.ValidationReason

class ValidateTeamWinningToss {
    operator fun invoke(teamWinningToss: String, teamName: String, opponentsName: String) : ValidationReason {
        if(teamWinningToss.isBlank()) return ValidationReason.Empty

        return if (teamWinningToss != teamName && teamWinningToss != opponentsName) {
            ValidationReason.TeamWinningToss
        } else {
            ValidationReason.Succeeded
        }
    }
}