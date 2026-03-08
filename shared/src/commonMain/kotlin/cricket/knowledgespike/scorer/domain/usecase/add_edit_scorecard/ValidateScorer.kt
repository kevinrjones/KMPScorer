package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.validation.ValidationReason

class ValidateScorer {
    operator fun invoke(name: String) : ValidationReason {
        if(name.isBlank()) return ValidationReason.Empty
        return ValidationReason.Succeeded
    }
}

