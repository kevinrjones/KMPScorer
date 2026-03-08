package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.ValidationReason

class ValidateVenue {
    operator fun invoke(venue: String) : ValidationReason {
        if(venue.isBlank()) return ValidationReason.Empty
        return ValidationReason.Succeeded
    }
}

