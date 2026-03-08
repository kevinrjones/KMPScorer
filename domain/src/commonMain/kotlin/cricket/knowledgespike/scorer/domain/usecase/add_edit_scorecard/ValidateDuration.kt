package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.ValidationReason

class ValidateDuration {
    operator fun invoke(duration: String) : ValidationReason {
        if(duration.isBlank()) return ValidationReason.Empty
        return ValidationReason.Succeeded
    }
}

