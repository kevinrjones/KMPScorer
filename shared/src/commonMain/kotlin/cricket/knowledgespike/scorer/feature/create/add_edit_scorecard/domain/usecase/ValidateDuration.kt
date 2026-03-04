package cricket.knowledgespike.scorer.feature.create.add_edit_scorecard.domain.usecase

import cricket.knowledgespike.scorer.foundation.ValidationResult
import cricket.knowledgespike.scorer.foundation.isEmptyValidation
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.duration_label

class ValidateDuration {
    operator fun invoke(duration: String) : ValidationResult {
        return isEmptyValidation(duration, Res.string.duration_label)
    }
}

