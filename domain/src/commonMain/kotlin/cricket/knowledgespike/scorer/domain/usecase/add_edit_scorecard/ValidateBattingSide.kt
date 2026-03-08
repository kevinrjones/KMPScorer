package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.ValidationReason

class ValidateBattingSide {
    operator fun invoke(
        battingSide: String,
        teamName: String,
        opponentsName: String
    ): ValidationReason {
        if(battingSide.isBlank()) return ValidationReason.Empty

        return if (battingSide != teamName && battingSide != opponentsName) {
            ValidationReason.BattingSide
        } else {
            ValidationReason.Succeeded
        }
    }
}