package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.ValidationResult
import cricket.knowledgespike.scorer.foundation.isEmptyValidation
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.missing_date

class ValidateMatchDate {
    operator fun invoke(date: String): ValidationResult {
        return isEmptyValidation(date, Res.string.missing_date)
    }
}

