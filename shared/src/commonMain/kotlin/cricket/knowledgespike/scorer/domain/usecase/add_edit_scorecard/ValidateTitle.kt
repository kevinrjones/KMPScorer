package cricket.knowledgespike.scorer.domain.usecase.add_edit_scorecard

import cricket.knowledgespike.scorer.foundation.ValidationResult
import cricket.knowledgespike.scorer.foundation.isEmptyValidation
import kmpscorer.shared.generated.resources.Res
import kmpscorer.shared.generated.resources.missing_match_title

class ValidateTitle {
    operator fun invoke(title: String) : ValidationResult {
        return isEmptyValidation(title, Res.string.missing_match_title)
    }
}

