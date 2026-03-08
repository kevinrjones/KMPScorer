package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.validation.ValidationReason

class ValidateMatchLabel {
    operator fun invoke(match: String) : ValidationReason {
        if(match.isBlank()) return ValidationReason.Empty
        return ValidationReason.Succeeded
    }
}

