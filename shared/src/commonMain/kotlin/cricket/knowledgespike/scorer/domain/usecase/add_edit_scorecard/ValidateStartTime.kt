package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.ValidationResult
import cricket.knowledgespike.scorer.foundation.isEmptyValidation
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.start_time_label

class ValidateStartTime {
    operator fun invoke(startTime: String) : ValidationResult {
        return isEmptyValidation(startTime, Res.string.start_time_label)
    }
}

