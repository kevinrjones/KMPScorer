package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.validation.ValidationReason

class ValidateMatchDate {
    operator fun invoke(date: String): ValidationReason {
        if(date.isBlank()) return ValidationReason.Empty
        return ValidationReason.Succeeded
    }
}

