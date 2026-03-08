package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.ValidationReason

class ValidateOpponentsName {
    operator fun invoke(name: String) : ValidationReason {
        if (name.isBlank()) return ValidationReason.Empty
        return ValidationReason.Succeeded
    }

}

