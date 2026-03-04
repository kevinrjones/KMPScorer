package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import cricket.knowledgespike.scorer.foundation.UiText
import cricket.knowledgespike.scorer.foundation.ValidationResult
import cricket.knowledgespike.scorer.foundation.isEmptyValidation
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.invalid_batting_side_name
import kmpscorer.shared.generated.resources.team_winning_toss_label

class ValidateTeamWinningToss {
    operator fun invoke(teamWinningToss: String, teamName: String, opponentsName: String) : ValidationResult {
        val result = isEmptyValidation(teamWinningToss, Res.string.team_winning_toss_label)

        if(!result.successful) return result

        return if (teamWinningToss != teamName && teamWinningToss != opponentsName) {
            ValidationResult(
                successful = false,
                errorMessage = UiText.StringResourceId(Res.string.invalid_batting_side_name)
            )
        } else {
            ValidationResult(
                successful = true
            )
        }
    }
}