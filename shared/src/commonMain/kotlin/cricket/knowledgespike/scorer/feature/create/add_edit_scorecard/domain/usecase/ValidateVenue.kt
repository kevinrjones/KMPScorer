package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import cricket.knowledgespike.scorer.foundation.ValidationResult
import cricket.knowledgespike.scorer.foundation.isEmptyValidation
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.missing_venue

class ValidateVenue {
    operator fun invoke(venue: String) : ValidationResult {
        return isEmptyValidation(venue, Res.string.missing_venue)
    }
}

