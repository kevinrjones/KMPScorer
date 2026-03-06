package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import cricket.knowledgespike.scorer.foundation.compose.UiText
import cricket.knowledgespike.scorer.foundation.ValidationResult
import cricket.knowledgespike.scorer.foundation.isEmptyValidation
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.invalid_team_winning_toss_name

class ValidateTeamWinningToss {
    operator fun invoke(teamWinningToss: String, teamName: String, opponentsName: String) : ValidationResult {
        val result = isEmptyValidation(teamWinningToss, Res.string.invalid_team_winning_toss_name)

        if(!result.successful) return result

        return if (teamWinningToss != teamName && teamWinningToss != opponentsName) {
            ValidationResult(
                successful = false,
                errorMessage = UiText.StringResourceId(Res.string.invalid_team_winning_toss_name)
            )
        } else {
            ValidationResult(
                successful = true
            )
        }
    }
}