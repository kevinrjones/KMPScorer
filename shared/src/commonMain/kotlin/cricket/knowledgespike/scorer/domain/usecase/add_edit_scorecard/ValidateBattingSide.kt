package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.compose.UiText
import cricket.knowledgespike.scorer.foundation.ValidationResult
import cricket.knowledgespike.scorer.foundation.isEmptyValidation
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.invalid_batting_side_name

class ValidateBattingSide {
    operator fun invoke(
        battingSide: String,
        teamName: String,
        opponentsName: String
    ): ValidationResult {
        val result = isEmptyValidation(battingSide, Res.string.invalid_batting_side_name)
        if (!result.successful) return result

        return if (battingSide != teamName && battingSide != opponentsName) {
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