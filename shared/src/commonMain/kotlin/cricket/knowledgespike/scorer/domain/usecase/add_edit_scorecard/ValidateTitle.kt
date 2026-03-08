package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.validation.ValidationReason

class ValidateTitle {
    operator fun invoke(title: String) : ValidationReason {
        if(title.isBlank()) return ValidationReason.Empty
        return ValidationReason.Succeeded
    }
}

