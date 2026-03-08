package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.validation.ValidationReason

class ValidateStartTime {
    operator fun invoke(startTime: String) : ValidationReason {
        if(startTime.isBlank()) return ValidationReason.Empty
        return ValidationReason.Succeeded
    }
}

